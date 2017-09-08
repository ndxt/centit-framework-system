package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
            filterField.put("NP_unitRoleType", "id.roleCode in (select roleCode from RoleInfo where unitCode is not null)");
            filterField.put("NP_userRoleType", "id.roleCode not in (select roleCode from RoleInfo where unitCode is not null)");
            filterField.put("userCode_isValid", "id.userCode in (select userCode from UserInfo where isValid = :userCode_isValid)");
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " id.userCode ");
            filterField.put("userName", "id.userCode in (select userCode from UserInfo where userName like :userName)");
        }
        return filterField;
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
        String hql = "FROM UserRole ur where ur.id.userCode = ? and ur.id.roleCode like ?"
                + "and ur.id.obtainDate <= ? and (ur.secedeDate is null or ur.secedeDate > ?) "
                + "ORDER BY obtainDate";

        return listObjects(hql, new Object[]{usid, rolePrefix + "%",new Date(),new Date()});
    }

    @Transactional
    public List<UserRole> getAllUserRolesByUserId(String usid, String rolePrefix) {
        String hql = "FROM UserRole ur where ur.id.userCode=? and ur.id.roleCode like ? "
                + "ORDER BY obtainDate";

        return listObjects(hql, new Object[]{usid, rolePrefix + "%"});
    }
    @Transactional
    public UserRole getValidUserRole(String userCode, String rolecode) {
        String hql = "FROM UserRole ur where ur.id.userCode=? and ur.id.roleCode = ? " +
             "ORDER BY obtainDate";

        List<UserRole> urlt = listObjects(hql, new Object[]{userCode, rolecode});
        if (CollectionUtils.isEmpty(urlt)) {
            return null;
        }
        return urlt.get(0);
    }
}
