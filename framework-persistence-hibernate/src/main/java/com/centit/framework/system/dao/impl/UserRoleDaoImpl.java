package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
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

            filterField.put("roleCode", "id.roleCode = :roleCode");

            filterField.put("userCode", "id.userCode = :userCode");
          
            filterField.put("roleName", CodeBook.LIKE_HQL_ID);
            
            filterField.put("NP_unitRoleType", "roleCode in (select roleCode from RoleInfo where unitCode is not null)");
            filterField.put("NP_userRoleType", "roleCode not in (select roleCode from RoleInfo where unitCode is not null)");

            filterField.put("USERCODE_ISVALID", "userCode in (select userCode from UserInfo where isValid = :USERCODE_ISVALID)");
            
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " id.userCode ");

        }
        return filterField;
    }
    
    @Transactional
    public void deleteByRoleId(String roid) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.roleCode = ?", roid);
    }
    @Transactional
    public void deleteByUserId(String usid) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.userCode = ?", usid);
    }
    @Transactional
    public void deleteByRoleCodeAndUserCode(String roleCode,String userCode) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.userCode = '"+userCode+"' and id.roleCode= '"+roleCode+"'");
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> getSysRolesByUserId(String userCode) {
 
        final String sSqlsen = "from FVUserRoles v where userCode = ?";
        List<FVUserRoles> ls = (List<FVUserRoles>) DatabaseOptUtils.findObjectsByHql(
                this, sSqlsen, new Object[]{userCode});
        return ls;
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
