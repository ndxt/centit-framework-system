package com.centit.framework.system.service;

import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface SysUnitManager{

    List<UnitInfo> listObjects(Map<String, Object> filterMap);

    List<UnitInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    UnitInfo getObjectById(String unitCode);

    List<UserInfo> getUnitUsers(String unitCode);

    UnitInfo getUnitByName(String name);

    List<UnitInfo> listObjectsAsSort(Map<String, Object> searchColumn);

    /**
     * 更新机构及子机构的状态
     * @param unitCode 机构代码
     * @param isValid 状态码
     */
    void changeStatus(String unitCode, String isValid);

    /**
     * 删除机构
     * @param unitinfo 机构对象
     */
    void deleteUnitInfo(UnitInfo unitinfo);

    String saveNewUnitInfo(UnitInfo unitinfo);

    boolean isUniqueName(UnitInfo unitInfo);

    boolean isUniqueOrder(UnitInfo unitInfo);

    void updateUnitInfo(UnitInfo unitinfo);
    List<UnitInfo> listAllSubObjectsAsSort(String primaryUnit);

    void checkState(List<UnitInfo> objs);

    List<UnitInfo> listValidSubUnit(String unitCode);

    List<UnitInfo> listAllSubUnits(String unitCode);

}
