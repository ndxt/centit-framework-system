package com.centit.framework.system.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.system.po.UnitRole;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

/**
 * @author codefan
 */
public interface SysUnitRoleManager {

    JSONArray listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    JSONArray listUnitRoles(String unitCode,PageDesc pageDesc);

    /**
     * 根据机构 该机构关联角色
     * @param unitCode 机构代码
     * @param pageDesc 分页
     */
    JSONArray listCurrentUnitRoles(String unitCode, PageDesc pageDesc);

    JSONArray listRoleUnits(String roleCode,PageDesc pageDesc);

    /**
     * 根据角色 查询当前部门 机构
     * @param roleCode 角色代码
     * @param unitPathPrefix 机构路径前缀
     * @param pageDesc 分页
     */
    JSONArray listRoleSubUnits(String roleCode, String unitPathPrefix, PageDesc pageDesc);

    UnitRole getUnitRoleById(String unitCode, String roleCode);

    void saveNewUnitRole(UnitRole dbUnitRole);

    void deleteUnitRole(String unitCode, String roleCode);

    void updateUnitRole(UnitRole unitRole);

    void mergeUnitRole(UnitRole unitRole);

}
