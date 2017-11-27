package com.centit.framework.system.dao.hibernateimpl;

import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.system.dao.UnitRoleDao;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("unitRoleDao")
public class UnitRoleDaoImpl extends BaseDaoImpl<UnitRole, UnitRoleId> implements UnitRoleDao {

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();

            filterField.put("roleCode", "id.roleCode = :roleCode");

            filterField.put("unitCode", "id.unitCode = :unitCode");

        }
        return filterField;
    }

    @Override
    @Transactional
    public void updateUnitRole(UnitRole dbUnitRole){
        super.updateObject(dbUnitRole);
    }

    @Override
    @Transactional
    public void mergeUnitRole(UnitRole dbUnitRole) {
        super.mergeObject(dbUnitRole);
    }

  @Override
    @Transactional
    public void deleteUnitRole(UnitRole dbUnitRole){
        super.deleteObject(dbUnitRole);
    }

    @Override
    @Transactional
    public void deleteUnitRoleById(UnitRoleId id){
        super.deleteObjectById(id);
    }

    @Override
    @Transactional
    public UnitRole getUnitRoleById(UnitRoleId id){
        return super.getObjectById(id);
    }

    @Override
    @Transactional
    public List<UnitRole> listUnitRolesByUnitCode(String unitCode){
        return super.listObjects(QueryUtils.createSqlParamsMap("unitCode",unitCode));
    }

    @Override
    @Transactional
    public List<UnitRole> listUnitRolesByRoleCode(String roleCode){
        return super.listObjects(QueryUtils.createSqlParamsMap("roleCode",roleCode));
    }

}
