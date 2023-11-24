package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.RoleInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("roleInfoDao")
public class RoleInfoDao extends BaseDaoImpl<RoleInfo, String>{

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("roleCode", CodeBook.LIKE_HQL_ID);
        filterField.put("publicUnitRole", "(ROLE_TYPE='P' or (ROLE_TYPE='D' and UNIT_CODE = :publicUnitRole))");
        filterField.put("NP_ALL", "(ROLE_TYPE='F' or ROLE_TYPE='G' or ROLE_TYPE='P')");
        filterField.put("NP_GLOBAL", "(ROLE_TYPE='G' or ROLE_TYPE='P')");
        filterField.put("NP_OWNER", "(ROLE_TYPE='D' or ROLE_TYPE='S')");

        filterField.put("roleName", CodeBook.LIKE_HQL_ID);
        filterField.put("ROLEDESC", CodeBook.LIKE_HQL_ID);
        filterField.put("isValid", CodeBook.EQUAL_HQL_ID);
        filterField.put("roleType", CodeBook.EQUAL_HQL_ID);
        filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("ownerCode", "UNIT_CODE = :ownerCode");

        filterField.put("roleNameEq", "ROLE_NAME = :roleNameEq");
        filterField.put("(date)createDateBeg", "CREATE_DATE>= :createDateBeg");
        filterField.put("(nextday)createDateEnd", "CREATE_DATE< :createDateEnd");

        filterField.put("unitRole", "(ROLE_TYPE='D' and UNIT_CODE = :unitRole)");
        filterField.put("subSystemROLE", "(ROLE_TYPE='S' and OS_ID = :subSystemROLE)");
        filterField.put("topUnit", "(ROLE_TYPE='P' or UNIT_CODE = :topUnit)"); //(ROLE_TYPE='D' ， ROLE_TYPE='G' or
        filterField.put("osRole", "(ROLE_TYPE <> 'S' or OS_ID = :subSystemROLE)"); // 这个一定要和 topUnit 联合使用
        return filterField;
    }


    public List<RoleInfo> listObjectsAll() {
        return super.listObjects();
    }

    @Transactional
    public void deleteObjectById(String roleCode) {
        super.deleteObjectById(roleCode);
    }

    public RoleInfo getObjectById(String roleCode) {
        return super.getObjectById(roleCode);
    }

    public RoleInfo getRoleByCodeOrName(String roleCodeOrName) {
        List<RoleInfo> roles = this.listObjectsByFilter(" where IS_VALID ='T' and ( ROLE_CODE= ? or " +
                    "((ROLE_TYPE='G' or ROLE_TYPE='P') and ROLE_NAME =?))", new Object[]{roleCodeOrName,roleCodeOrName});
        if(roles!=null && roles.size()>0)
            return roles.get(0);
        return null;
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

    public RoleInfo getObjectByProperty(String propertyName, Object propertyValue) {
        return super.getObjectByProperties(CollectionsOpt.createHashMap(propertyName, propertyValue));
    }

    @Transactional
    public void updateRole(RoleInfo roleInfo){
        super.updateObject(roleInfo);
    }

    @Transactional
    public List<RoleInfo> listAllRoleByUnit(String topUnit) {
        return super.listObjectsByFilter(
            " where ROLE_TYPE = 'G' or (ROLE_TYPE='D' and UNIT_CODE = ?) ",
            new Object[]{topUnit});
    }


    /**
     * 根据optCode查询角色信息
     * @param optCode String
     * @return List RoleInfo
     */
    public List<RoleInfo> listRoleInfoByOptCode(String optCode) {
        String sql = " SELECT B.ROLE_CODE, B.ROLE_NAME, B.ROLE_TYPE, B.UNIT_CODE, B.ROLE_DESC, B.UPDATE_DATE, B.CREATE_DATE, B.CREATOR, B.UPDATOR " +
            " FROM F_ROLEPOWER A JOIN F_ROLEINFO B ON A.ROLE_CODE = B.ROLE_CODE " +
            "WHERE A.OPT_CODE = ?  ";
       return getJdbcTemplate().execute(
            (ConnectionCallback<List<RoleInfo>>) conn ->OrmDaoUtils.queryObjectsByParamsSql(conn, sql,new Object[]{optCode}, RoleInfo.class));
    }


}
