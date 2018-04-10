package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
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
            filterField.put("roleCode", "ROLE_CODE = :roleCode");
            filterField.put("unitCode", "UNIT_CODE = :unitCode");
            filterField.put("(StartWith)unitPathPrefix",
                "UNIT_CODE in (select UNIT_CODE from f_unitinfo where UNIT_PATH like :unitPathPrefix)");
            filterField.put("currentUnitCode",
                "ROLE_CODE in (select ROLE_CODE from f_roleinfo where UNIT_CODE = :currentUnitCode)");
            filterField.put("unitValid", "unitCode in (select UNIT_CODE from f_unitinfo where IS_VALID = :unitValid)");
            filterField.put("roleValid", "roleCode in (select ROLE_CODE from f_roleinfo where IS_VALID = :roleValid)");
        }
        return filterField;
    }

    @Override
    @Transactional
    public void updateUnitRole(UnitRole dbUnitRole){
        super.updateObject(dbUnitRole);
    }

    @Override
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
