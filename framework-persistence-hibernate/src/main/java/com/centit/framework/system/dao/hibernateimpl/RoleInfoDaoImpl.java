package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.RoleInfoDao;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.VOptTree;
import com.centit.support.database.utils.QueryUtils;
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

    @SuppressWarnings("unchecked")
    @Transactional
    public List<VOptTree> getVOptTreeList() {
        return (List<VOptTree>)DatabaseOptUtils.findObjectsByHql
                (this,"FROM VOptTree");
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
            filterField.put("NP_unitCode", "unitCode is null");
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
                + "from OptMethod def, RolePower pow where def.optCode = pow.id.optCode and pow.id.roleCode = ?";
        return (List<Object>)DatabaseOptUtils.findObjectsByHql
                (this,hql,  new Object[]{rolecode});
    }


    public int countRoleUserSum(String roleCode){
        Long l = DatabaseOptUtils.getSingleIntByHql(this,
                "select count(1) as roleUserSum from UserRole where id.roleCode=?",
                roleCode
        );
        return l.intValue();
    }
}
