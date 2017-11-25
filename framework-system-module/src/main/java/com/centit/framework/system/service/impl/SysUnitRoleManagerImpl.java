package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.UnitRoleDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codefan
 */
@Service("sysUnitManager")
public class SysUnitRoleManagerImpl implements SysUnitRoleManager {
    @Resource
    @NotNull
    protected UnitRoleDao unitRoleDao;

    @Override
    @Transactional
    public JSONArray listUnitRoles(String unitCode, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("unitCode",unitCode);
        List<UnitRole> unitRoles = unitRoleDao.pageQuery(
            QueryParameterPrepare.prepPageParams(
                filterMap,pageDesc,unitRoleDao.pageCount(filterMap)));
        return DictionaryMapUtils.objectsToJSONArray(unitRoles);
    }

    @Override
    @Transactional
    public JSONArray listRoleUnits(String roleCode,PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("roleCode",roleCode);
        List<UnitRole> unitRoles = unitRoleDao.pageQuery(
            QueryParameterPrepare.prepPageParams(
                filterMap,pageDesc,unitRoleDao.pageCount(filterMap)));
        return DictionaryMapUtils.objectsToJSONArray(unitRoles);
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
}
