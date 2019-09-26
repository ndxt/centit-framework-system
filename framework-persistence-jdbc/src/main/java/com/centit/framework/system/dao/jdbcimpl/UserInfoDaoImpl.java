package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends BaseDaoImpl<UserInfo, String> implements UserInfoDao {

    // 将F_V_USERROLES试图提出增加条件查询提高性能
    //private static String currentDateTime = QueryUtils.buildDatetimeStringForQuery(DatetimeOpt.currentUtilDate());

    private static String f_v_userroles_sql = "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, " +
        "b.ROLE_TYPE, b.UNIT_CODE,b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,a.USER_CODE, null as INHERITED_FROM " +
        "from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
        "where a.OBTAIN_DATE <= :currentDateTime "+
        " and (a.SECEDE_DATE is null or a.SECEDE_DATE > :currentDateTime " +
        " ) and b.IS_VALID='T' and b.ROLE_CODE = :roleCode " +
        "union " +
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, b.UNIT_CODE, " +
        "b.ROLE_DESC, b.CREATE_DATE, b.UPDATE_DATE ,c.USER_CODE, a.UNIT_CODE as INHERITED_FROM " +
        "from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) " +
        "JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE) " +
        "where a.OBTAIN_DATE <= :currentDateTime " +
        " and (a.SECEDE_DATE is null or a.SECEDE_DATE > :currentDateTime " +
        " )" +
        " and b.IS_VALID='T' and a.ROLE_CODE = :roleCode ";

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put(CodeRepositoryUtil.USER_CODE, CodeBook.LIKE_HQL_ID);
            filterField.put("USERCODE_EQ", CodeBook.EQUAL_HQL_ID);
            filterField.put("userName", CodeBook.LIKE_HQL_ID);
            filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
            filterField.put("LOGINNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("USERSTATE", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERORDER", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERTAG", CodeBook.EQUAL_HQL_ID);
            filterField.put("USERWORD", CodeBook.EQUAL_HQL_ID);

            filterField.put("(like)likeUserOrLoginName","(User_Name LIKE :likeUserOrLoginName OR LOGIN_NAME LIKE :likeUserOrLoginName)");
            filterField.put("byUnderUnit", "userCode in " +
                    "(select us.USER_CODE from f_userunit us where us.UNIT_CODE = :byUnderUnit ) ");
            filterField.put("roleCode", "USER_CODE in " +
                "(select v.USER_CODE from ( " + f_v_userroles_sql + " ) v where v.ROLE_CODE = :roleCode) ");
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
        }
        return filterField;
    }

    @Transactional
    @Override
    public List<UserInfo> listObjects(Map<String, Object> filterMap){
        filterMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        return super.listObjects(filterMap);
    }



    @Transactional
    @Override
    public int pageCount(Map<String, Object> filterDescMap){
        filterDescMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        return super.pageCount(filterDescMap);
    }


    @Transactional
    @Override
    public List<UserInfo> pageQuery(Map<String, Object> pageQueryMap){
        pageQueryMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        return super.pageQuery(pageQueryMap);
    }

    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
          DatabaseOptUtils.getSequenceNextValue(this, "S_USERCODE"));
    }


    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserOptList> getAllOptMethodByUser(String userCode) {
        String sql = "select USER_CODE,OPT_CODE,OPT_NAME,OPT_ID,OPT_METHOD " +
                "from F_V_USEROPTLIST " +
                "where USER_CODE=?";

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<FVUserOptList>>) conn ->
                        OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                                new Object[]{userCode}, FVUserOptList.class));
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
    public UserInfo getUserByCode(String userCode) {
        return super.getObjectById(userCode);
    }

    @Transactional
    public UserInfo getUserByLoginName(String loginName) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap(
                "loginName",loginName));
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
    public UserInfo getUserByIdCardNo(String idCardNo){
        return super.getObjectByProperties(CollectionsOpt.createHashMap("idCardNo", idCardNo));
    }

    @Override
    public void deleteObjectById(String userCode) {
        super.deleteObjectById(userCode);
    }

    @Override
    public List<UserInfo> listUsersByRoleCode(String roleCode) {
        return super.listObjects(CollectionsOpt.createHashMap("roleCode", roleCode) );
    }

    public int isLoginNameExist(String userCode, String loginName){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.LOGINNAME = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
                    new Object[]{userCode, loginName}));
    }
    public int isCellPhoneExist(String userCode, String cellPhone){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGCELLPHONE = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
                    new Object[]{userCode, cellPhone}));
    }
    public int isEmailExist(String userCode, String email){
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USERCODE <> ? and t.REGEMAIL = ?";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
                    new Object[]{userCode, email}));
    }

    public int isAnyOneExist(String userCode, String loginName,String regPhone,String regEmail) {
        String sql = "select count(*) as usersCount from F_USERINFO t " +
                "where t.USER_CODE != ? and " +
                "(t.LOGIN_NAME = ? or t.REG_CELL_PHONE= ? or t.Reg_Email = ?)";
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, sql,
                    new Object[]{userCode, loginName, regPhone, regEmail}));
    }

    @Override
    public void updateUser(UserInfo userInfo){
        super.updateObject(userInfo);
    }

}
