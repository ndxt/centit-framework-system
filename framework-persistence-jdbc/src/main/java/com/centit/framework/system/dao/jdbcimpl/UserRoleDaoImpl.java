package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.support.algorithm.CollectionsOpt;
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
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole, UserRoleId>
    implements UserRoleDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("roleCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("roleName", CodeBook.LIKE_HQL_ID);
            filterField.put("roleUnitCode", "ROLE_CODE in (select ro.ROLE_CODE from f_roleinfo ro " +
                    "where (ro.ROLE_TYPE = 'P' or (ro.ROLE_TYPE = 'D' and ro.UNIT_CODE = :roleUnitCode)))");
            filterField.put("unitCode", "USER_CODE in (select uu.USER_CODE from F_USERUNIT uu where uu.UNIT_CODE = :unitCode)");
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
        }
        return filterField;
    }

    @Override
    @Transactional
    public List<UserRole> listUserRoles(String userCode) {
        return super.listObjects(CollectionsOpt.createHashMap("userCode",userCode));
    }

    @Override
    @Transactional
    public List<UserRole> listRoleUsers(String roleCode) {
        return super.listObjects(CollectionsOpt.createHashMap("roleCode",roleCode));
    }

    @Override
    @Transactional
    public void deleteObjectById(UserRoleId id) {
          super.deleteObjectById(id);
      }

    @Override
    @Transactional
    public UserRole getObjectById(UserRoleId id) {
        return super.getObjectById(id);
    }

    @Override
    @Transactional
    public void deleteByRoleId(String roid) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("roleCode",roid));
    }

    @Override
    @Transactional
    public void deleteByUserId(String usid) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("userCode",usid));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listUserRolesByUserCode(String userCode) {
        return getJdbcTemplate().execute(
          (ConnectionCallback<List<FVUserRoles>>) conn ->
            OrmDaoUtils.listObjectsByProperties(conn,
              CollectionsOpt.createHashMap("userCode",userCode) ,
              FVUserRoles.class));
    }


    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listRoleUsersByRoleCode(String roleCode) {
      return getJdbcTemplate().execute(
        (ConnectionCallback<List<FVUserRoles>>) conn ->
          OrmDaoUtils.listObjectsByProperties(conn,
            CollectionsOpt.createHashMap("roleCode",roleCode) ,
            FVUserRoles.class));
    }

    @Override
    @Transactional
    public int pageCountUserRole(Map<String, Object> filterDescMap) {
        String sql = "select count(*) as cnt from F_V_USERROLES u " +
          "where 1=1 [:roleCode | and u.ROLE_CODE = :roleCode] " +
          "[:userCode | and u.USER_CODE = :userCode]" +
          "[:obtainType | and u.OBTAIN_TYPE = :obtainType] ";
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql , filterDescMap);
        return jdbcTemplate.execute(
          (ConnectionCallback<Integer>) conn ->
            OrmDaoUtils.fetchObjectsCount(conn, qap.getQuery(), qap.getParams()));
    }

    @Override
    @Transactional
    public List<FVUserRoles> pageQueryUserRole(Map<String, Object> pageQureyMap) {
      String querySql = "select u.USER_CODE,u.ROLE_CODE,u.ROLE_NAME,u.IS_VALID,u.ROLE_DESC," +
        " u.ROLE_TYPE,u.UNIT_CODE,u.OBTAIN_TYPE,u.INHERITED_FROM" +
        " from F_V_USERROLES u " +
        "where 1=1 [:roleCode | and u.ROLE_CODE = :roleCode] " +
        "[:userCode | and u.USER_CODE = :userCode]" +
        "[:obtainType | and u.OBTAIN_TYPE = :obtainType] ";
      PageDesc pageDesc = QueryParameterPrepare.fetchPageDescParams(pageQureyMap);
      QueryAndNamedParams qap = QueryUtils.translateQuery(querySql, pageQureyMap);
      return jdbcTemplate.execute(
           (ConnectionCallback<List<FVUserRoles>>) conn -> OrmDaoUtils
          .queryObjectsByNamedParamsSql(conn, qap.getQuery(), qap.getParams(), FVUserRoles.class,
            pageDesc.getRowStart(), pageDesc.getPageSize()));
    }


    @Transactional
    public void deleteByRoleCodeAndUserCode(String roleCode,String userCode) {
        super.deleteObjectsByProperties(
                CollectionsOpt.createHashMap("userCode",userCode,"roleCode",roleCode));
    }

    @Transactional
    public UserRole getValidUserRole(String userCode, String rolecode) {
        String sql = "select u.USER_CODE, u.ROLE_CODE, u.OBTAIN_DATE, u.CHANGE_DESC, u.CREATE_DATE, u.CREATOR, " +
                "u.UPDATOR, u.UPDATE_DATE from F_USERROLE u " +
                "where u.id.userCode=:userCode and u.id.roleCode = :roleCode " +
                "ORDER BY obtainDate";

        String hql = "FROM UserRole ur where ur.id.userCode=? and ur.id.roleCode = ? " +
             "ORDER BY obtainDate";

        List<UserRole> urlt = listObjectsBySql(sql, CollectionsOpt.createHashMap(
                "userCode", userCode, "roleCode", rolecode));
        if (CollectionUtils.isEmpty(urlt)) {
            return null;
        }
        return urlt.get(0);
    }

    @Override
    public void updateUserRole(UserRole userRole){
        super.updateObject(userRole);
    }

    /**
     * 合并
     *
     * @param dbUserRole 用户角色
     */
    @Override
    public void mergeUserRole(UserRole dbUserRole) {
        super.mergeObject(dbUserRole);
    }

}
