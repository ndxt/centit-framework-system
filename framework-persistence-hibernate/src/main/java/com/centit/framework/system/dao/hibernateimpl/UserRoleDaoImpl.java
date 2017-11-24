package com.centit.framework.system.dao.hibernateimpl;

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

            filterField.put("NP_unitRoleType", "id.roleCode in (select roleCode from RoleInfo where unitCode is not null)");
            filterField.put("NP_userRoleType", "id.roleCode not in (select roleCode from RoleInfo where unitCode is not null)");

            filterField.put("userCode_isValid", "id.userCode in (select userCode from UserInfo where isValid = :userCode_isValid)");

            filterField.put(CodeBook.ORDER_BY_HQL_ID, " id.userCode ");

            filterField.put("(like)userName", "id.userCode in (select userCode from UserInfo where userName like :userName)");

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


    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listUserRolesByUserCode(String userCode) {
        final String sSqlsen = "from FVUserRoles v where v.id.userCode = ?";
        return (List<FVUserRoles>) DatabaseOptUtils.findObjectsByHql(
          this, sSqlsen, new Object[]{userCode});
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> listRoleUsersByRoleCode(String roleCode) {
        final String sSqlsen = "from FVUserRoles v where v.id.roleCode = ?";
        return (List<FVUserRoles>) DatabaseOptUtils.findObjectsByHql(
          this, sSqlsen, new Object[]{roleCode});
    }

}
