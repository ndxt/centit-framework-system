package com.centit.framework.system.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("unitRoleDao")
public class UnitRoleDao extends BaseDaoImpl<UnitRole, UnitRoleId> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("roleCode", "ROLE_CODE = :roleCode");
        filterField.put("unitCode", "UNIT_CODE = :unitCode");
        filterField.put("(StartWith)unitPathPrefix",
            "UNIT_CODE in (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPathPrefix)");
        filterField.put("currentUnitCode",
            "ROLE_CODE in (select ROLE_CODE from f_roleinfo where UNIT_CODE = :currentUnitCode)");
        filterField.put("unitValid", "unitCode in (select UNIT_CODE from f_unitinfo where IS_VALID = :unitValid)");
        filterField.put("roleValid", "roleCode in (select ROLE_CODE from f_roleinfo where IS_VALID = :roleValid)");
        return filterField;
    }

    @Transactional
    public void updateUnitRole(UnitRole dbUnitRole){
        super.updateObject(dbUnitRole);
    }

    @Transactional
    public void mergeUnitRole(UnitRole dbUnitRole) {
        super.mergeObject(dbUnitRole);
    }

    @Transactional
    public void deleteUnitRole(UnitRole dbUnitRole){
        super.deleteObject(dbUnitRole);
    }

    @Transactional
    public void deleteUnitRoleById(UnitRoleId id){
        super.deleteObjectById(id);
    }

    @Transactional
    public UnitRole getUnitRoleById(UnitRoleId id){
        return super.getObjectById(id);
    }

    @Transactional
    public List<UnitRole> listUnitRolesByUnitCode(String unitCode){
        return super.listObjects(CollectionsOpt.createHashMap("unitCode",unitCode));
    }

    @Transactional
    public List<UnitRole> listUnitRolesByRoleCode(String roleCode){
        return super.listObjects(CollectionsOpt.createHashMap("roleCode",roleCode));
    }

}
