package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.RolePower;
import com.centit.framework.system.po.RolePowerId;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-29
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */

@Repository("rolePowerDao")
public class RolePowerDao extends BaseDaoImpl<RolePower, RolePowerId> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("optCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("roleCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("topUnit", "ROLE_CODE in (select role_code from f_roleinfo us " +
            "where unit_code = :topUnit)");
        return filterField;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<RolePower> listObjectsAll() {
        return super.listObjects();
    }

    @Transactional
    public void deleteRolePowersByRoleCode(String roleCode) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("roleCode",roleCode));
    }

    @Transactional
    public void deleteRolePowersByOptCode(String optCode) {
        super.deleteObjectsByProperties(CollectionsOpt.createHashMap("optCode",optCode));
    }


    @Transactional
    public List<RolePower> listRolePowersByRoleCode(String rolecode) {
        return listObjectsByProperty("roleCode", rolecode);
    }

    @Transactional
    public void mergeBatchObject(List<RolePower> rolePowers) {
        for (int i = 0; i < rolePowers.size(); i++) {
            super.mergeObject(rolePowers.get(i));
        }
    }

    @Transactional
    public void updateRolePower(RolePower rolePower){
        super.updateObject(rolePower);
    }

    @Transactional
    public void saveNewRolePower(RolePower rolePower){
      super.saveNewObject(rolePower);
    }

    @Transactional
    public void deleteObjectById(RolePowerId id){
        super.deleteObjectById(id);
    }


    @Transactional
    public List<RolePower> listAllRolePowerByUnit(String topUnit){
        String sql = "select distinct a.* " +
            "from F_ROLEPOWER a join F_ROLEINFO b on(a.ROLE_CODE=b.ROLE_CODE) " +
            "where (ROLE_TYPE = 'G' or (ROLE_TYPE='D' and UNIT_CODE = ?)";

        return getJdbcTemplate().execute(
            (ConnectionCallback<List<RolePower>>) conn ->
                OrmDaoUtils.queryObjectsByParamsSql(conn, sql ,
                    new Object[]{topUnit}, RolePower.class));
    }
}
