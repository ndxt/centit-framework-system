package com.centit.framework.system.dao;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.FVUserRoles;
import com.centit.framework.model.basedata.UserRole;
import com.centit.framework.model.basedata.UserRoleId;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userRoleDao")
public class UserRoleDao extends BaseDaoImpl<UserRole, UserRoleId> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("roleCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("roleName", CodeBook.LIKE_HQL_ID);
        filterField.put("roleUnitCode", "ROLE_CODE in (select ro.ROLE_CODE from f_roleinfo ro " +
                "where (ro.ROLE_TYPE = 'G' or ro.UNIT_CODE = :roleUnitCode))");
        filterField.put("unitCode", "USER_CODE in (select uu.USER_CODE from F_USERUNIT uu where uu.UNIT_CODE = :unitCode)");
        filterField.put("topUnit", "ROLE_CODE in (select ro.ROLE_CODE from f_roleinfo ro where ROLE_TYPE = 'G' or ro.UNIT_CODE = :topUnit)");
        filterField.put("userCode_isValid", "userCode in (select us.USER_CODE from f_userinfo us " +
                "where us.IS_VALID = :userCode_isValid)");
        //filterField.put(CodeBook.ORDER_BY_HQL_ID, " userCode ");
        filterField.put("(like)userName", "userCode in (select us.USER_CODE from f_userinfo us " +
                "where (us.USER_NAME like :userName or us.LOGIN_NAME like :userName))");
        filterField.put("(STARTWITH)unitPath", "userCode in (select us.USER_CODE from f_userunit us where us.UNIT_CODE in " +
            "(select un.UNIT_CODE from f_unitinfo un where un.UNIT_PATH like :unitPath))");

        filterField.put("userValid", "userCode in (select us.USER_CODE from f_userinfo us " +
            "where us.IS_VALID = :userValid)");
        filterField.put("roleValid", "roleCode in (select us.ROLE_CODE from f_roleinfo us " +
            "where us.IS_VALID = :roleValid)");
        return filterField;
    }

    // 将F_V_USERROLES试图提出增加条件查询提高性能
    //  b.CREATE_DATE, b.UPDATE_DATE
    private static final String f_v_user_appoint_roles_sql =
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, " +
            " b.UNIT_CODE, b.ROLE_DESC, a.USER_CODE, null as INHERITED_FROM, c.user_name, d.unit_name " +
        "from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
            "join f_userinfo c on a.user_code=c.user_code "+
            "left join f_unitinfo d on c.primary_unit=d.unit_code "+
        "where [:currentDateTime | a.OBTAIN_DATE <=  :currentDateTime and] " +
            "(a.SECEDE_DATE is null [:currentDateTime | or a.SECEDE_DATE > :currentDateTime]) " +
            "and b.IS_VALID='T' " +
            "[:userCode | and a.USER_CODE = :userCode]" +
            "[:(startwith)roleName | and b.ROLE_NAME like :roleName]" +
            "[:roleCode | and a.ROLE_CODE = :roleCode]";

    private static final String f_v_user_inherited_roles_sql =
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'I' as OBTAIN_TYPE, b.ROLE_TYPE, " +
            "b.UNIT_CODE, b.ROLE_DESC, c.USER_CODE, a.UNIT_CODE as INHERITED_FROM,d.user_name,e.unit_name " +
        "from F_UNITROLE a join F_ROLEINFO b on (a.ROLE_CODE = b.ROLE_CODE) " +
            "JOIN F_USERUNIT c on( a.UNIT_CODE = c.UNIT_CODE) " +
            "join f_userinfo d on c.user_code=d.user_code "+
            "join f_unitinfo e on a.unit_code=e.unit_code "+
        "where [:currentDateTime | a.OBTAIN_DATE <=  :currentDateTime and] " +
            "(a.SECEDE_DATE is null [:currentDateTime | or a.SECEDE_DATE > :currentDateTime]) " +
            "and b.IS_VALID='T' " +
            "[:userCode | and c.USER_CODE = :userCode]" +
            "[:(startwith)roleName | and b.ROLE_NAME like :roleName]" +
            "[:roleCode | and a.ROLE_CODE = :roleCode]";
    private static final String f_v_userroles_sql =
        f_v_user_appoint_roles_sql + " union all " + f_v_user_inherited_roles_sql;


    private static final String f_v_topunit_user_role =
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, " +
            " b.UNIT_CODE, b.ROLE_DESC, a.USER_CODE, null as INHERITED_FROM, c.user_name, d.unit_name " +
            "from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
            "join f_userinfo c on a.user_code=c.user_code "+
            "left join f_unitinfo d on c.primary_unit=d.unit_code "+
            "where a.USER_CODE = :userCode and a.OBTAIN_DATE <= :currentDateTime and " +
             " (a.SECEDE_DATE is null  or a.SECEDE_DATE > :currentDateTime) " +
                "and b.IS_VALID='T' " +
            " and (b.ROLE_TYPE = 'G' or b.UNIT_CODE = :unitCode)";

    private static final String f_v_topunit_role_user =
        "select b.ROLE_CODE, b.ROLE_NAME, b.IS_VALID, 'D' as OBTAIN_TYPE, b.ROLE_TYPE, " +
            " b.UNIT_CODE, b.ROLE_DESC, a.USER_CODE, null as INHERITED_FROM,c.user_name, d.unit_name " +
            "from F_USERROLE a join F_ROLEINFO b on (a.ROLE_CODE=b.ROLE_CODE) " +
            "join f_userinfo c on a.user_code=c.user_code "+
            "left join f_unitinfo d on c.primary_unit=d.unit_code "+
            "where a.ROLE_CODE = :roleCode and a.OBTAIN_DATE <= :currentDateTime and " +
            " (a.SECEDE_DATE is null  or a.SECEDE_DATE > :currentDateTime) " +
            "and b.IS_VALID='T' " +
            " and (b.ROLE_TYPE = 'G' or b.UNIT_CODE = :unitCode)";

    @Transactional
    public List<UserRole> listUserRoles(String userCode) {
        return super.listObjectsByProperties(CollectionsOpt.createHashMap("userCode",userCode));
    }

    @Transactional
    public List<UserRole> listRoleUsers(String roleCode) {
        return super.listObjectsByProperties(CollectionsOpt.createHashMap("roleCode",roleCode));
    }

    @Transactional
    public void deleteObjectById(UserRoleId id) {
          super.deleteObjectById(id);
      }

    @Transactional
    public UserRole getObjectById(UserRoleId id) {
        return super.getObjectById(id);
    }

    @Transactional
    public void deleteByRoleId(String roid) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("roleCode",roid));
    }

    @Transactional
    public void deleteByUserId(String usid) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("userCode",usid));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listUserRolesByUserCode(String userCode) {
        /*return getJdbcTemplate().execute(
          (ConnectionCallback<List<FVUserRoles>>) conn ->
            OrmDaoUtils.listObjectsByProperties(conn,
              CollectionsOpt.createHashMap("userCode",userCode) ,
              FVUserRoles.class));*/

        Map<String,Object> map = CollectionsOpt.createHashMap("userCode",userCode,
            "currentDateTime", DatetimeOpt.currentSqlDate());
        QueryAndNamedParams qap = QueryUtils.translateQuery(f_v_userroles_sql, map);
        return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, qap.getQuery(), qap.getParams(), FVUserRoles.class));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listRoleUsersByRoleCode(String roleCode) {
        // 之前走 F_V_USERROLES 试图方式
      /*return getJdbcTemplate().execute(
        (ConnectionCallback<List<FVUserRoles>>) conn ->
          OrmDaoUtils.listObjectsByProperties(conn,
            CollectionsOpt.createHashMap("roleCode",roleCode) ,
            FVUserRoles.class));*/

        Map<String,Object> map = CollectionsOpt.createHashMap("roleCode",roleCode,
            "currentDateTime", DatetimeOpt.currentSqlDate());
        QueryAndNamedParams qap = QueryUtils.translateQuery(f_v_userroles_sql, map);
        return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, qap.getQuery(), qap.getParams(), FVUserRoles.class));
    }

    @Transactional
    public List<FVUserRoles> listUserRolesByTopUnit(String topUnit, String userCode) {
        Map<String,Object> map = CollectionsOpt.createHashMap("userCode",userCode,
            "currentDateTime", DatetimeOpt.currentSqlDate(),
            "unitCode", topUnit);
        //topUnit
        return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, f_v_topunit_user_role, map, FVUserRoles.class));
    }

    @Transactional
    public List<FVUserRoles> listRoleUsersByTopUnit(String topUnit, String roleCode) {
        Map<String,Object> map = CollectionsOpt.createHashMap("roleCode", roleCode,
            "currentDateTime", DatetimeOpt.currentSqlDate(),
            "unitCode", topUnit);
        //topUnit
        return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, f_v_topunit_role_user, map, FVUserRoles.class));
    }

    @Transactional
    public int pageCountUserRole(Map<String, Object> filterDescMap) {
        String sql = "select count(*) as cnt from (" ;
        String obtainType = StringBaseOpt.castObjectToString(filterDescMap.get("obtainType"));
        if("D".equals(obtainType)){
            sql = sql + f_v_user_appoint_roles_sql + ") u ";
        } else if("I".equals(obtainType)){
            sql = sql + f_v_user_inherited_roles_sql + ") u ";
        } else{
            sql = sql + f_v_userroles_sql + ") u ";
        }

        filterDescMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql , filterDescMap);
        return jdbcTemplate.execute(
            (ConnectionCallback<Integer>) conn ->
                OrmDaoUtils.fetchObjectsCount(conn, qap.getQuery(), qap.getParams()));
    }

    @Transactional
    public JSONArray pageQueryUserRole(Map<String, Object> filterMap, PageDesc pageDesc) {
        String querySql;
        String obtainType = StringBaseOpt.castObjectToString(filterMap.get("obtainType"));
        if("D".equals(obtainType)){
            querySql =  f_v_user_appoint_roles_sql ;
        } else if("I".equals(obtainType)){
            querySql = f_v_user_inherited_roles_sql;
        } else{
            querySql = f_v_userroles_sql;
        }
        filterMap.put("currentDateTime", DatetimeOpt.currentSqlDate());
        QueryAndNamedParams qap = QueryUtils.translateQuery(querySql, filterMap);
        return
            DatabaseOptUtils.listObjectsByNamedSqlAsJson(this,qap.getQuery(), qap.getParams(), pageDesc);
        /*return jdbcTemplate.execute(
            (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
                .queryObjectsByNamedParamsSql(conn, qap.getQuery(), qap.getParams(), FVUserRoles.class,
                    pageDesc.getRowStart(), pageDesc.getPageSize()));*/
    }

    @Transactional
    public void deleteByRoleCodeAndUserCode(String roleCode,String userCode) {
        super.deleteObjectsByProperties(
                CollectionsOpt.createHashMap("userCode",userCode,"roleCode",roleCode));
    }

    @Transactional
    public UserRole getValidUserRole(String userCode, String rolecode) {
        String sql =
            "select u.USER_CODE, u.ROLE_CODE, u.OBTAIN_DATE, u.CHANGE_DESC, u.CREATE_DATE, " +
                "u.CREATOR, u.UPDATOR, u.UPDATE_DATE " +
            "from F_USERROLE u " +
            "where u.id.userCode=:userCode and u.id.roleCode = :roleCode " +
            "ORDER BY obtainDate";

        /*String hql = "FROM UserRole ur where ur.id.userCode=? and ur.id.roleCode = ? " +
             "ORDER BY obtainDate";
*/
        List<UserRole> urlt = listObjectsBySql(sql, CollectionsOpt.createHashMap(
                "userCode", userCode, "roleCode", rolecode));
        if (CollectionUtils.isEmpty(urlt)) {
            return null;
        }
        return urlt.get(0);
    }

    @Transactional
    public void updateUserRole(UserRole userRole){
        super.updateObject(userRole);
    }

    /**
     * 合并
     *
     * @param dbUserRole 用户角色
     */
    @Transactional
    public void mergeUserRole(UserRole dbUserRole) {
        super.mergeObject(dbUserRole);
    }

}
