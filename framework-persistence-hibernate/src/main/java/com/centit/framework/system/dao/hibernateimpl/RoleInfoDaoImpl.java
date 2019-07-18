package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.RoleInfoDao;
import com.centit.framework.system.po.RoleInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("roleInfoDao")
public class RoleInfoDaoImpl extends BaseDaoImpl<RoleInfo, String> implements RoleInfoDao {

    @Override
    public String getNextKey() {
        return DatabaseOptUtils.getNextValueOfSequence(
          this, "S_ROLECODE");
    }

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("ROLECODE", CodeBook.EQUAL_HQL_ID);
            filterField.put("publicUnitRole", "(roleType='P' or (roleType='D' and unitCode = :publicUnitRole))");
            filterField.put("UNITROLE", "(roleType='P' or (roleType='D' and unitCode = :UNITROLE))");
            filterField.put("NP_GLOBAL", "(roleType='G' or roleType='P')");
            filterField.put("ROLENAME", CodeBook.LIKE_HQL_ID);
            filterField.put("ROLEDESC", CodeBook.LIKE_HQL_ID);
            filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
            filterField.put("roleType", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_ALL", "(roleType='F' or roleType='G' or roleType='P')");
            filterField.put("roleNameEq", "roleName = :roleNameEq");

            filterField.put("(date)createDateBeg", "createDate>= :createDateBeg");

            filterField.put("(nextday)createDateEnd", "createDate< :createDateEnd");
        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object> listRoleOptMethods(String rolecode) {
        String hql = "select new map(def.optName as def_optname, def.optCode as def_optcode) "
                + "from OptMethod def, RolePower pow where def.optCode = pow.id.optCode and pow.id.roleCode = ?0";
        return (List<Object>)DatabaseOptUtils.findObjectsByHql
                (this,hql,  new Object[]{rolecode});
    }

    @Override
    public void updateRole(RoleInfo roleInfo){
        super.updateObject(roleInfo);
    }

    @Override
    public RoleInfo getRoleByCodeOrName(String roleCodeOrName) {
        List<RoleInfo> roles = this.listObjects(" From RoleInfo where isValid ='T' and ( roleCode= ?0 or " +
            "(( roleType='G' or roleType='P') and roleName = ?1))", new Object[]{roleCodeOrName,roleCodeOrName});
        if(roles!=null && roles.size()>0)
            return roles.get(0);
        return null;
    }

}
