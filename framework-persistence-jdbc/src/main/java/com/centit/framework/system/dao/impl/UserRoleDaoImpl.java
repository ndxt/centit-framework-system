package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userRoleDao")
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole, UserRoleId> implements UserRoleDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("roleCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("userCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("roleName", CodeBook.LIKE_HQL_ID);
            filterField.put("NP_unitRoleType", "roleCode in (select ro.ROLE_CODE from f_roleinfo ro " +
                    "where ro.UNIT_CODE is not null)");
            filterField.put("NP_userRoleType", "roleCode not in (select ro.ROLE_CODE from f_roleinfo ro " +
                    "where ro.UNIT_CODE is not null)");
            filterField.put("userCode_isValid", "userCode in (select us.USER_CODE from f_userinfo us " +
                    "where us.IS_VALID = :userCode_isValid)");
            //filterField.put(CodeBook.ORDER_BY_HQL_ID, " userCode ");
            filterField.put("(like)userName", "userCode in (select us.USER_CODE from f_userinfo us " +
                    "where us.USER_NAME like :userName)");
        }
        return filterField;
    }

    @Override
    public void deleteObjectById(UserRoleId id) {
        super.deleteObjectById(id);
    }

    @Override
    public UserRole getObjectById(UserRoleId id) {
        return super.getObjectById(id);
    }

    @Transactional
    public void deleteByRoleId(String roid) {
        super.deleteObjectsByProperties(QueryUtils.createSqlParamsMap("roleCode",roid));
    }
    @Transactional
    public void deleteByUserId(String usid) {
        super.deleteObjectsByProperties(QueryUtils.createSqlParamsMap("userCode",usid));
    }
    @Transactional
    public void deleteByRoleCodeAndUserCode(String roleCode,String userCode) {
        super.deleteObjectsByProperties(
                QueryUtils.createSqlParamsMap("userCode",userCode,"roleCode",roleCode));   }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> getSysRolesByUserId(String userCode) {
        return getJdbcTemplate().execute(
                (ConnectionCallback<List<FVUserRoles>>) conn ->
                        OrmDaoUtils.listObjectsByProperties(conn,
                                QueryUtils.createSqlParamsMap("userCode",userCode) ,
                                FVUserRoles.class));
    }
    @Transactional
    public List<UserRole> getUserRolesByUserId(String usid, String rolePrefix) {
        String sql = "select u.USER_CODE, u.ROLE_CODE, u.OBTAIN_DATE, u.CHANGE_DESC, u.CREATE_DATE, u.CREATOR, " +
                "u.UPDATOR, u.UPDATE_DATE from F_USERROLE u " +
                "where USER_CODE = :userCode and ROLE_CODE like :rolePrefix" +
                "and OBTAIN_DATE <= sysdate and (SECEDE_DATE is null or SECEDE_DATE >sysdate)" +
                "ORDER BY OBTAIN_DATE,SECEDEDATE";

        return listObjectsBySql(sql, QueryUtils.createSqlParamsMap("userCode", usid, "rolePrefix", rolePrefix + "%"));
    }

    @Transactional
    public List<UserRole> getAllUserRolesByUserId(String usid, String rolePrefix) {
        String sql = "select u.USER_CODE, u.ROLE_CODE, u.OBTAIN_DATE, u.CHANGE_DESC, u.CREATE_DATE, u.CREATOR, " +
                "u.UPDATOR, u.UPDATE_DATE from F_USERROLE u " +
                "where u.id.userCode=:userCode and u.id.roleCode like :rolePrefix "
                + "ORDER BY obtainDate";

        return listObjectsBySql(sql, QueryUtils.createSqlParamsMap("userCode", usid, "rolePrefix", rolePrefix + "%"));
    }

    @Transactional
    public UserRole getValidUserRole(String userCode, String rolecode) {
        String sql = "select u.USER_CODE, u.ROLE_CODE, u.OBTAIN_DATE, u.CHANGE_DESC, u.CREATE_DATE, u.CREATOR, " +
                "u.UPDATOR, u.UPDATE_DATE from F_USERROLE u " +
                "where u.id.userCode=:userCode and u.id.roleCode = :roleCode " +
                "ORDER BY obtainDate";

        String hql = "FROM UserRole ur where ur.id.userCode=? and ur.id.roleCode = ? " +
             "ORDER BY obtainDate";

        List<UserRole> urlt = listObjectsBySql(sql, QueryUtils.createSqlParamsMap(
                "userCode", userCode, "roleCode", rolecode));
        if (CollectionUtils.isEmpty(urlt)) {
            return null;
        }
        return urlt.get(0);
    }
}
