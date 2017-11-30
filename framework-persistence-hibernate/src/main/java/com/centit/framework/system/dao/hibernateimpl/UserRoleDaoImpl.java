package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            filterField.put("roleUnitCode", "id.roleCode in (select roleCode from RoleInfo where (roleType = 'P' or (roleType = 'D' and unitCode = :unitCode))");
            filterField.put("unitCode", "id.userCode in (select uu.userCode from UserUnit uu where uu.unitCode = :unitCode)");
            filterField.put("userCode_isValid", "id.userCode in (select userCode from UserInfo where isValid = :userCode_isValid)");
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " id.userCode ");

            filterField.put("(like)userName", "id.userCode in (select userCode from UserInfo where userName like :userName)");

        }
        return filterField;
    }

    @Override
    @Transactional
    public List<UserRole> listUserRoles(String userCode) {
        return super.listObjects(QueryUtils.createSqlParamsMap("userCode",userCode));
    }

    @Override
    @Transactional
    public List<UserRole> listRoleUsers(String roleCode) {
        return super.listObjects(QueryUtils.createSqlParamsMap("roleCode",roleCode));
    }

    @Override
    @Transactional
    public void deleteByRoleId(String roid) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.roleCode = ?", roid);
    }

    @Override
    @Transactional
    public void deleteByUserId(String usid) {
        DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.userCode = ?", usid);
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

    @Override
    @Transactional
    public int pageCountUserRole(Map<String, Object> filterDescMap) {
        final String sSqlsen = "select count(*) as cnt from FVUserRoles v " +
          "where 1=1 [:roleCode | and v.id.roleCode = :roleCode] " +
          "[:userCode | and v.id.userCode = :userCode]" +
          "[:obtainType | and v.obtainType = :obtainType] ";
        QueryAndNamedParams qap = QueryUtils.translateQuery(sSqlsen, filterDescMap );
        return NumberBaseOpt.castObjectToInteger(
            DatabaseOptUtils.getSingleObjectByHql(this, qap.getQuery(), qap.getParams()));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FVUserRoles> pageQueryUserRole(Map<String, Object> pageQureyMap) {
        final String sSqlsen = "from FVUserRoles v " +
          "where 1=1 [:roleCode | and v.id.roleCode = :roleCode] " +
          "[:userCode | and v.id.userCode = :userCode]" +
          "[:obtainType | and v.obtainType = :obtainType] ";
        int startPos = 0;
        int maxSize = 0;
        if(pageQureyMap!=null){
          startPos = NumberBaseOpt.castObjectToInteger(pageQureyMap.get("startRow"));
          maxSize = NumberBaseOpt.castObjectToInteger(pageQureyMap.get("maxSize"));
        }

        QueryAndNamedParams qap = QueryUtils.translateQuery(sSqlsen, pageQureyMap );
        return ( List<FVUserRoles>) DatabaseOptUtils.findObjectsByHql (
          this, qap.getQuery(), qap.getParams(),startPos,maxSize);
    }

}
