package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.system.dao.UnitRoleDao;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author codefan
 */
@Service("sysUnitRoleManager")
public class SysUnitRoleManagerImpl implements SysUnitRoleManager {
    @Autowired
    @NotNull
    protected UnitRoleDao unitRoleDao;

    @Override
    @Transactional
    public JSONArray listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        JSONArray unitRoles = unitRoleDao.listObjectsAsJson(filterMap,pageDesc);
        return DictionaryMapUtils.mapJsonArray(unitRoles,  UnitRole.class);
    }

    @Override
    @Transactional
    public JSONArray listUnitRoles(String unitCode, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("unitCode",unitCode);
        JSONArray unitRoles = unitRoleDao.listObjectsAsJson(filterMap, pageDesc);
        return DictionaryMapUtils.mapJsonArray(unitRoles,  UnitRole.class);
    }

    @Override
    @Transactional
    public JSONArray listRoleUnits(String roleCode,PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("roleCode",roleCode);
        JSONArray unitRoles = unitRoleDao.listObjectsAsJson(filterMap, pageDesc);
        return DictionaryMapUtils.mapJsonArray(unitRoles,  UnitRole.class);
    }

    @Override
    @Transactional
    public UnitRole getUnitRoleById(String unitCode, String roleCode) {
        return unitRoleDao.getUnitRoleById(new UnitRoleId(unitCode,roleCode));
    }

    @Override
    @Transactional
    public void saveNewUnitRole(UnitRole dbUnitRole) {
        unitRoleDao.saveNewObject(dbUnitRole);
    }

    @Override
    @Transactional
    public void deleteUnitRole(String unitCode, String roleCode) {
        unitRoleDao.deleteUnitRoleById(new UnitRoleId(unitCode,roleCode));
    }

    @Override
    @Transactional
    public void updateUnitRole(UnitRole unitRole) {
        unitRoleDao.updateUnitRole(unitRole);
    }

    @Override
    @Transactional
    public void mergeUnitRole(UnitRole unitRole) {
        unitRoleDao.mergeUnitRole(unitRole);
    }

    @Override
    @Transactional
    public JSONArray listRoleSubUnits(String roleCode, String unitPathPrefix, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("roleCode", roleCode);
        filterMap.put("unitPathPrefix", unitPathPrefix);
        JSONArray unitRoles = unitRoleDao.listObjectsAsJson(filterMap, pageDesc);
        return DictionaryMapUtils.mapJsonArray(unitRoles,  UnitRole.class);
    }

    @Override
    @Transactional
    public JSONArray listCurrentUnitRoles(String unitCode, PageDesc pageDesc){
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("unitCode", unitCode);
        filterMap.put("currentUnitCode", unitCode);
        JSONArray unitRoles = unitRoleDao.listObjectsAsJson(filterMap, pageDesc);
        return DictionaryMapUtils.mapJsonArray(unitRoles,  UnitRole.class);
    }
}
