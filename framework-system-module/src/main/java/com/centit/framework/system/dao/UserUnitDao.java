package com.centit.framework.system.dao;

import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.UserUnit;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userUnitDao")
public class UserUnitDao extends BaseDaoImpl<UserUnit, String> {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(20);
        filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("userStation", CodeBook.EQUAL_HQL_ID);
        filterField.put("userRank", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("isPrimary", CodeBook.EQUAL_HQL_ID);
        filterField.put("unitName", CodeBook.LIKE_HQL_ID);
        filterField.put("topUnit", CodeBook.EQUAL_HQL_ID);
        filterField.put("relType", CodeBook.EQUAL_HQL_ID);
        filterField.put("(like)userName", "user_code in (select us.USER_CODE from f_userinfo us where" +
                " us.USER_NAME like :userName)");
        filterField.put("isValid", "user_code in (select us.USER_CODE from f_userinfo us where " +
                "us.IS_VALID = :isValid)");
        filterField.put("userType", "user_code in (select us.USER_CODE from f_userinfo us where " +
            "us.user_type = :userType)");
        filterField.put("(STARTWITH)unitPath","UNIT_CODE IN (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPath)]");
        filterField.put(CodeBook.SELF_ORDER_BY, "userOrder asc");
        return filterField;
    }

    public List<UserUnit> listObjectsAll(Map<String, Object> filterMap) {
        return listObjectsByProperties(filterMap);
    }

    public UserUnit getObjectById(String userUnitId) {
        return super.getObjectById(userUnitId);
    }

    /**
     * 更新
     *
     * @param userUnit 用户机构
     */
    public void updateUserUnit(UserUnit userUnit) {
        super.updateObject(userUnit);
    }

    @Transactional
    public void deleteObjectById(String userUnitId) {
        super.deleteObjectById(userUnitId);
    }

    @Transactional
    public void deleteObjectForceById(String id) {
        super.deleteObjectForceById(id);
    }

    @Transactional
    public List<UserUnit> listUserUnitsByUserCode(String userId) {
        List<UserUnit> ls = listObjectsByProperties(CollectionsOpt.createHashMap("userCode", userId));
        /*
         * for (FUserunit usun : ls) {
         * usun.setUnitname(CodeRepositoryUtil.getValue
         * ("unitCode",usun.getId().getUnitcode() )); }
         */
        return ls;
    }

    @Transactional
    public List<UserUnit> listUserUnitsByUserCode(String unitCode, String userCode) {
        String sql = "select a.* " +
            "from F_USERUNIT a join F_UNITINFO b on (a.UNIT_CODE = b.UNIT_CODE) " +
            "where a.USER_CODE =? and b.TOP_UNIT = ?";
        return getJdbcTemplate().execute(
            (ConnectionCallback<List<UserUnit>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                    new Object[]{userCode, unitCode}, UserUnit.class));
    }

    @Transactional
    public List<UserUnit> listObjectByUserUnit(String userCode, String unitCode){
        List<UserUnit> ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "userCode", userCode,"unitCode",unitCode));
        return ls;
    }

    @Transactional
    public void deleteUserUnitByUser(String userCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("userCode", userCode);
        super.deleteObjectsByProperties(map);
    }

    @Transactional
    public void deleteUserUnitByUnit(String unitCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("unitCode", unitCode);
        super.deleteObjectsByProperties(map);
    }

    @Transactional
    public UserUnit getPrimaryUnitByUserId(String userId, String topUnit) {
        List<UserUnit> list = null;
        if (StringUtils.isBlank(topUnit) || GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)) {
            list = super.listObjectsByProperties(CollectionsOpt.createHashMap(
                "userCode", userId, "relType", "T"));
        } else {
            list = super.listObjectsByProperties(CollectionsOpt.createHashMap(
                "userCode", userId, "relType", "T", "topUnit", topUnit));
        }
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public List<UserUnit> listUnitUsersByUnitCode(String unitCode) {
        return listObjectsByProperties(CollectionsOpt.createHashMap("unitCode", unitCode));
    }

    /**
     * unitcode不为null就是某个处室的某个角色，为NULL就是所有处室的某个角色
     *
     * @param roleType roleType
     * @param roleCode roleCode
     * @param unitCode unitCode
     * @return List
     */

    @Transactional
    public List<UserUnit> listUserUnitsByRoleAndUnit(String roleType,
                                                   String roleCode, String unitCode) {
        List<UserUnit> ls = null;
        if (unitCode != null && !"".equals(unitCode)) {
            if ("gw".equals(roleType)) {
              ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "unitCode", unitCode, "userStation", roleCode));
            } else if ("xz".equals(roleType)) {
              ls = listObjectsByProperties(CollectionsOpt.createHashMap(
                "unitCode", unitCode, "userRank", roleCode));
            }
        } else {
            if ("gw".equals(roleType)) {
              ls = listObjectsByProperties(CollectionsOpt.createHashMap("userStation", roleCode));
            } else if ("xz".equals(roleType)) {
              ls = listObjectsByProperties(CollectionsOpt.createHashMap("userRank", roleCode));
            }
        }
        return ls;
    }

    public String getNextKey() {
        return UuidOpt.getUuidAsString22();
    }


    public List<UserUnit> querySubUserUnits(Map<String, Object> filterDescMap, PageDesc pageDesc) {

        String sql = "select count(*) FROM F_USERUNIT WHERE 1=1 " +
            "[:(STARTWITH)unitPath | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPath)]"+
            "[:(like)userName | and USER_CODE in (select USER_CODE from f_userinfo where USER_NAME like :userName or LOGIN_NAME like :userName)]" +
            "[:isValid | and USER_CODE in (select us.USER_CODE from f_userinfo us where us.IS_VALID = :isValid)]"+
            "[:userCode | and USER_CODE = :userCode]"+
            "[:relType | and rel_type = :relType]"+
            "[:unitIsValid | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where IS_VALID = :unitIsValid)]";

        QueryAndNamedParams qap = QueryUtils.translateQuery(sql, filterDescMap);
        Integer rowCount = jdbcTemplate.execute(
            (ConnectionCallback<Integer>) conn ->
                OrmDaoUtils.fetchObjectsCount(conn, qap.getQuery(), qap.getParams()));
        pageDesc.setTotalRows(rowCount);

        sql = "select USER_UNIT_ID, UNIT_CODE, USER_CODE, REL_TYPE, USER_STATION, USER_RANK, RANK_MEMO, USER_ORDER, " +
          "UPDATE_DATE, CREATE_DATE, CREATOR, UPDATOR " +
          "FROM F_USERUNIT WHERE 1=1 " +
          "[:(STARTWITH)unitPath | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPath)]"+
          "[:(like)userName | and USER_CODE in (select USER_CODE from f_userinfo where USER_NAME like :userName or LOGIN_NAME like :userName)]" +
          "[:isValid | and USER_CODE in (select us.USER_CODE from f_userinfo us where us.IS_VALID = :isValid)]"+
          "[:userCode | and USER_CODE = :userCode]"+
            "[:relType | and rel_type = :relType]"+
          "[:unitIsValid | and UNIT_CODE IN (select UNIT_CODE from f_unitinfo where IS_VALID = :unitIsValid)]";


        String selfOrderBy = GeneralJsonObjectDao.fetchSelfOrderSql(sql, filterDescMap);
        if (StringUtils.isNotBlank(selfOrderBy)) {
          sql = QueryUtils.removeOrderBy(sql) + " order by " + selfOrderBy;
        }
        QueryAndNamedParams qap2 = QueryUtils.translateQuery(sql, filterDescMap);
        return jdbcTemplate.execute(
          /** 这个地方可以用replaceField 已提高效率
           *  pageDesc.setTotalRows(OrmDaoUtils.fetchObjectsCount(conn,
           QueryUtils.buildGetCountSQLByReplaceFields(qap.getSql()),qap.getParams()));
           * */
          (ConnectionCallback<List<UserUnit>>) conn -> OrmDaoUtils
            .queryObjectsByNamedParamsSql(conn, qap2.getQuery(), qap2.getParams(), (Class<UserUnit>) getPoClass(),
              pageDesc.getRowStart(), pageDesc.getPageSize()));
    }

    /**
     * 统计单位下的用户数量
     * @param topUnit 租户信息
     * @return 用户个数
     */
    public int countUserByTopUnit(String topUnit) {
        String sql = " SELECT COUNT(DISTINCT USER_CODE) COUNT FROM F_USERUNIT A JOIN F_UNITINFO B ON ( A.UNIT_CODE = B.UNIT_CODE )  " +
            "WHERE 1=1 [ :topUnit | AND B.TOP_UNIT = :topUnit ] ";
        Map<String, Object> params  = StringUtils.isBlank(topUnit)? new HashMap<>():CollectionsOpt.createHashMap("topUnit",topUnit);
        QueryAndParams queryAndParams = QueryAndParams.createFromQueryAndNamedParams(QueryUtils.translateQuery(sql, params));
        logger.info("sql: {},参数：{}",queryAndParams.getQuery(),topUnit);
        return NumberBaseOpt.castObjectToInteger(DatabaseOptUtils.getScalarObjectQuery(this, queryAndParams.getQuery(),queryAndParams.getParams()));
    }

}
