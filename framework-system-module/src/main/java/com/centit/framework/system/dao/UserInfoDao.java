package com.centit.framework.system.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.FVUserOptList;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userInfoDao")
public class UserInfoDao extends BaseDaoImpl<UserInfo, String> {

    // 将F_V_USERROLES试图提出增加条件查询提高性能
    //private static String currentDateTime = QueryUtils.buildDatetimeStringForQuery(DatetimeOpt.currentUtilDate());

    private static String f_v_userroles_sql = "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, " +
        "b.ROLE_TYPE, b.UNIT_CODE,b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, null as INHERITED_FROM " +
        "from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
        "where a.OBTAIN_DATE <= :currentDateTime " +
        " and (a.SECEDE_DATE is null or a.SECEDE_DATE > :currentDateTime )" +
        " and b.IS_VALID='T' and b.ROLE_CODE = :roleCode " +
        "union " +
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE, " +
        "b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM " +
        "from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) " +
        "JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE) " +
        "where a.OBTAIN_DATE <= :currentDateTime " +
        " and (a.SECEDE_DATE is null or a.SECEDE_DATE > :currentDateTime )" +
        " and b.IS_VALID='T' and a.ROLE_CODE = :roleCode ";

    private static final String f_v_topunit_user_powers =
        "select DISTINCT a.USER_CODE, c.OPT_CODE, c.OPT_NAME, c.OPT_ID, c.OPT_METHOD " +
            "from F_V_USERROLES a " +
            "JOIN F_ROLEPOWER b ON ( a.Role_Code = b.Role_Code ) " +
            "JOIN F_OPTDEF c ON ( b.OPT_CODE = c.OPT_CODE ) " +
            "where USER_CODE= :userCode and OPT_METHOD is not null and a.role_code in ( " +
            "select b.ROLE_CODE from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
            "where a.USER_CODE = :userCode and a.OBTAIN_DATE <= :currentDateTime and " +
            " (a.SECEDE_DATE is null  or a.SECEDE_DATE > :currentDateTime) and b.IS_VALID='T' " +
            " and ( ROLE_TYPE = 'G' or (ROLE_TYPE='D' and b.UNIT_CODE = :unitCode ) ) )";

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put(CodeRepositoryUtil.USER_CODE, CodeBook.LIKE_HQL_ID);
        filterField.put("USERCODE_EQ", CodeBook.EQUAL_HQL_ID);
        filterField.put("userName", CodeBook.LIKE_HQL_ID);
        filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
        filterField.put("LOGINNAME", CodeBook.LIKE_HQL_ID);
        filterField.put("USERSTATE", CodeBook.EQUAL_HQL_ID);
        filterField.put("USERORDER", CodeBook.EQUAL_HQL_ID);
        filterField.put("USERTAG", CodeBook.EQUAL_HQL_ID);
        filterField.put("USERWORD", CodeBook.EQUAL_HQL_ID);

        filterField.put("topUnit", ( " USER_CODE IN ( SELECT DISTINCT UN.USER_CODE FROM F_USERUNIT UN WHERE UN.TOP_UNIT = :topUnit ) " ));
        filterField.put("(like)likeUserOrLoginName", "(User_Name LIKE :likeUserOrLoginName OR LOGIN_NAME LIKE :likeUserOrLoginName)");
        filterField.put("byUnderUnit", "userCode in " +
            "(select us.USER_CODE from f_userunit us where us.UNIT_CODE = :byUnderUnit ) ");
        filterField.put("roleCode", "[(isNotEmpty(roleCode))(roleCode, currentDateTime) | and USER_CODE in " +
            "(select v.USER_CODE from ( " + f_v_userroles_sql + " ) v where v.ROLE_CODE = :roleCode) ]");
        filterField.put("queryByUnit", "userCode in " +
            "(select us.USER_CODE from f_userunit us where us.UNIT_CODE = :queryByUnit ) ");
        filterField.put("queryByGW", "userCode in " +
            "(select us.USER_CODE from f_userunit us where us.User_Station = :queryByGW )");
        filterField.put("queryByXZ", "userCode in " +
            "(select us.USER_CODE from f_userunit us where us.USER_RANK = :queryByXZ )");
        filterField.put("queryByRole", "userCode in " +
            "(select r.USER_CODE from f_userrole r join f_roleinfo i on r.ROLE_CODE = i.ROLE_CODE " +
            "where r.ROLE_CODE = :queryByRole and i.IS_VALID = 'T')");

//            filterField.put(CodeBook.ORDER_BY_HQL_ID, "userOrder asc");

        filterField.put("unitCode", "userCode in (select us.USER_CODE from f_userunit us where us.UNIT_CODE in " +
            "(select un.UNIT_CODE from f_unitinfo un where un.UNIT_CODE = :unitCode or un.PARENT_UNIT = :unitCode))");

        filterField.put("(STARTWITH)unitPath", "userCode in (select us.USER_CODE from f_userunit us where us.UNIT_CODE in " +
            "(select un.UNIT_CODE from f_unitinfo un where un.UNIT_PATH like :unitPath))");
        return filterField;
    }

    @Transactional
    public List<UserInfo> listObjects(Map<String, Object> filterMap) {
        filterMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        return super.listObjectsByProperties(filterMap);
    }


    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserOptList> listUserOptMethods(String userCode) {
        String sql = "select USER_CODE, OPT_CODE, OPT_NAME, OPT_ID, OPT_METHOD " +
            "from F_V_USEROPTLIST " +
            "where USER_CODE=? and OPT_METHOD is not null";

        return getJdbcTemplate().execute(
            (ConnectionCallback<List<FVUserOptList>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql,
                    new Object[]{userCode}, FVUserOptList.class));
    }

    @Transactional
    public List<FVUserOptList> listUserPowers(String topUnit, String userCode) {
        Map<String, Object> map = CollectionsOpt.createHashMap("userCode", userCode,
            "currentDateTime", DatetimeOpt.currentSqlDate(),
            "unitCode", topUnit);
        return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserOptList>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, f_v_topunit_user_powers, map, FVUserOptList.class));
    }

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap) {
        return this.listObjects(filterMap);
    }

    @Transactional
    public List<UserInfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        return this.listObjectsByProperties(filterMap, pageDesc);
    }

    @Transactional
    public JSONArray listObjectsByUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        String querySql = "SELECT a.* FROM F_USERINFO a " +
            "JOIN " +
            "(SELECT * FROM f_userunit WHERE 1=1 "
            + "[:queryByUnit | AND unit_code=:queryByUnit] [:topUnit | AND top_unit=:topUnit] [ :relType | AND REL_TYPE = :relType ] ) b " +
            "ON a.USER_CODE=b.user_code " +
            "where 1=1 [:(like)userName | and (User_Name LIKE :userName OR LOGIN_NAME LIKE :userName)]" +
            "ORDER BY b.user_order";
        QueryAndNamedParams qap = QueryUtils.translateQuery(querySql, filterMap);
        return DatabaseOptUtils.listObjectsByNamedSqlAsJson(this, qap.getQuery(), qap.getParams(), pageDesc);

    }

    /**
     * 根据 topUnit，userName，queryByUnit 查询用户信息
     * 用户信息包括用户基本信息，用户所属单位code和名称
     * @param filterMap 过滤条件
     * @param pageDesc PageDesc
     * @return JSONArray
     */
    @Transactional
    public JSONArray listUsersWithUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        String querySql = " SELECT A.USER_CODE, A.USER_TYPE, A.LOGIN_NAME, A.USER_NAME, A.USER_TAG," +
            " A.ENGLISH_NAME, A.USER_DESC, A.TOP_UNIT, A.REG_EMAIL, A.REG_CELL_PHONE," +
            " A.PRIMARY_UNIT, A.USER_WORD, A.USER_ORDER, B.USER_UNIT_ID, B.UNIT_CODE," +
            " B.USER_STATION, B.USER_RANK, C.UNIT_NAME " +
            " FROM F_USERINFO A JOIN F_USERUNIT B ON A.USER_CODE = B.USER_CODE " +
            " LEFT JOIN F_UNITINFO C ON B.UNIT_CODE = C.UNIT_CODE " +
            " WHERE 1 = 1 [ :topUnit | AND B.TOP_UNIT = :topUnit ] " +
            " [:(like)userName | AND ( A.USER_NAME LIKE :userName OR A.LOGIN_NAME LIKE :userName)] " +
            " [:queryByUnit | AND B.UNIT_CODE = :queryByUnit ] " +
            " ORDER BY A.USER_ORDER ";
        QueryAndNamedParams qap = QueryUtils.translateQuery(querySql, filterMap);
        return DatabaseOptUtils.listObjectsByNamedSqlAsJson(this, qap.getQuery(), qap.getParams(), pageDesc);

    }
    @Transactional
    public UserInfo getUserByCode(String userCode) {
        return super.getObjectById(userCode);
    }

    @Transactional
    public UserInfo getUserByLoginName(String loginName) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap(
            "loginName", loginName));
    }

    @Transactional
    public UserInfo getUserByRegEmail(String regEmail) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("regEmail", regEmail));
    }

    @Transactional
    public UserInfo getUserByRegCellPhone(String regCellPhone) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("regCellPhone", regCellPhone));
    }

    @Transactional
    public UserInfo getUserByTag(String userTag) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("userTag", userTag));
    }

    @Transactional
    public UserInfo getUserByUserWord(String userWord) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("userWord", userWord));
    }

    @Transactional
    public UserInfo getUserByIdCardNo(String idCardNo) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap("idCardNo", idCardNo));
    }

    @Transactional
    public void deleteObjectById(String userCode) {
        super.deleteObjectById(userCode);
    }

    public List<UserInfo> listUsersByRoleCode(String roleCode) {
        return super.listObjectsByProperties(
            CollectionsOpt.createHashMap("roleCode", roleCode,
                "currentDateTime", DatetimeOpt.currentSqlDate()));
    }

    public int isLoginNameExist(String userCode, String loginName) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
            "where t.USERCODE <> ? and t.LOGINNAME = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
            new Object[]{userCode, loginName}));
    }

    public int isCellPhoneExist(String userCode, String cellPhone) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
            "where t.USERCODE <> ? and t.REGCELLPHONE = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
            new Object[]{userCode, cellPhone}));
    }

    public int isEmailExist(String userCode, String email) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
            "where t.USERCODE <> ? and t.REGEMAIL = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
            new Object[]{userCode, email}));
    }

    public int isAnyOneExist(String userCode, String loginName, String regPhone, String regEmail) {
        Pair<String, Object[]> slqAndParams = anyOneExistSqlAndParams(userCode, loginName, regPhone, regEmail);
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, slqAndParams.getLeft(),slqAndParams.getRight()));
    }

    @Transactional
    public void updateUser(UserInfo userInfo) {
        super.updateObject(userInfo);
    }

    private  Pair<String,Object[]> anyOneExistSqlAndParams(String userCode, String loginName, String regPhone, String regEmail){
        HashMap<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(userCode)){
            map.put("USER_CODE",userCode);
        }
        if (StringUtils.isNotBlank(loginName)){
            map.put("LOGIN_NAME",loginName);
        }
        if (StringUtils.isNotBlank(regPhone)){
            map.put("REG_CELL_PHONE",regPhone);
        }
        if (StringUtils.isNotBlank(regEmail)){
            map.put("REG_EMAIL",regEmail);
        }
        Object[] params = new String[map.size()];
        StringBuilder stringBuilder = new StringBuilder(" SELECT COUNT(1) AS USERSCOUNT FROM F_USERINFO WHERE ");
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (i != 0){
                stringBuilder.append(" OR ");
            }
            stringBuilder.append(entry.getKey()).append(" = ? ");
            params[i] = entry.getValue();
            i++;
        }
        return Pair.of(stringBuilder.toString(),params);
    }
}
