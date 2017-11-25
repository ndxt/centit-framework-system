package com.centit.framework.system.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.system.po.UnitRole;
import com.centit.support.database.utils.PageDesc;

/**
 * @author codefan
 */
public interface SysUnitRoleManager {

    JSONArray listUnitRoles(String unitCode,PageDesc pageDesc);

    JSONArray listRoleUnits(String roleCode,PageDesc pageDesc);

    UnitRole getUnitRoleById(String unitCode, String roleCode);

    void saveNewUnitRole(UnitRole dbUnitRole);

    void deleteUnitRole(String unitCode, String roleCode);

    void updateUnitRole(UnitRole unitRole);

    void mergeUnitRole(UnitRole unitRole);

}
