package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.RoleInfoDao;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.VOptTree;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("roleInfoDao")
public class RoleInfoDaoImpl extends BaseDaoImpl<RoleInfo, String> implements RoleInfoDao {


    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("roleCode", CodeBook.LIKE_HQL_ID);
            filterField.put("publicUnitRole", "(ROLE_TYPE='P' or (ROLE_TYPE='D' and UNIT_CODE = :publicUnitRole))");
            filterField.put("UNITROLE", "(ROLE_TYPE='P' or (ROLE_TYPE='D' and UNIT_CODE = :UNITROLE))");
            filterField.put("NP_GLOBAL", "(ROLE_TYPE='G' or ROLE_TYPE='P')");
            filterField.put("roleName", CodeBook.LIKE_HQL_ID);
            filterField.put("ROLEDESC", CodeBook.LIKE_HQL_ID);
            filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
            filterField.put("roleType", CodeBook.EQUAL_HQL_ID);
            filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
            filterField.put("NP_ALL", "(ROLE_TYPE='F' or ROLE_TYPE='G' or ROLE_TYPE='P')");
            filterField.put("roleNameEq", "ROLE_NAME = :roleNameEq");
            filterField.put("(date)createDateBeg", "CREATE_DATE>= :createDateBeg");
            filterField.put("(nextday)createDateEnd", "CREATE_DATE< :createDateEnd");
        }
        return filterField;
    }

    @Override
    public String getNextKey() {
        return StringBaseOpt.objectToString(
            DatabaseOptUtils.getSequenceNextValue(
                this, "S_ROLECODE"));
    }

    @Override
    public List<RoleInfo> listObjectsAll() {
        return super.listObjects();
    }

    @Override
    public void deleteObjectById(String roleCode) {
        super.deleteObjectById(roleCode);
    }

    @Override
    public RoleInfo getObjectById(String roleCode) {
        return super.getObjectById(roleCode);
    }

    @Override
    public RoleInfo getRoleByCodeOrName(String roleCodeOrName) {
        List<RoleInfo> roles = this.listObjectsByFilter(" where IS_VALID ='T' and ( ROLE_CODE= ? or " +
                    "((ROLE_TYPE='G' or ROLE_TYPE='P') and ROLE_NAME =?))", new Object[]{roleCodeOrName,roleCodeOrName});
        if(roles!=null && roles.size()>0)
            return roles.get(0);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<VOptTree> getVOptTreeList() {

        return getJdbcTemplate().execute(
                (ConnectionCallback<List<VOptTree>>) conn ->
                        OrmDaoUtils.listAllObjects(conn,  VOptTree.class));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object> listRoleOptMethods(String rolecode) {
        String hql = "select def.OPT_NAME as def_optname, def.OPT_CODE as def_optcode " +
                "from F_OPTDEF def, F_ROLEPOWER pow  " +
                "where def.OPT_CODE = pow.OPT_CODE and pow.ROLE_CODE = ? ";
        return DatabaseOptUtils.listObjectsBySqlAsJson(
                this,hql,  new Object[]{rolecode});
    }


    /**
     * 对角色信息进行模糊搜索，适用于带搜索条件的下拉框。
     *
     * @param key      搜索条件
     * @param field    需要搜索的字段，如为空，默认，roleCode,roleName
     * @return List
     */
    @Transactional
    public List<RoleInfo> search(String key, String[] field) {
        HashMap<String,Object> filter = new HashMap<>(field.length*2);
        for(String f :field){
            filter.put(f,key);
        }
        return listObjectsByProperties(filter);
    }

    @Override
    public RoleInfo getObjectByProperty(String propertyName, Object propertyValue) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap(propertyName, propertyValue));
    }

    @Override
    public void updateRole(RoleInfo roleInfo){
        super.updateObject(roleInfo);
    }

    @Override
    public void saveNewObject(RoleInfo roleInfo) {
        super.saveNewObject(roleInfo);
    }
}
