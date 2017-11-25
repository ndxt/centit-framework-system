package com.centit.framework.system.dao;


import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;

import java.util.List;
import java.util.Map;

public interface UnitRoleDao {

    void saveNewObject(UnitRole dbUnitRole);

    void updateUnitRole(UnitRole dbUnitRole);

    void mergeUnitRole(UnitRole dbUnitRole);

    void deleteUnitRole(UnitRole dbUnitRole);

    void deleteUnitRoleById(UnitRoleId id);

    UnitRole getUnitRoleById(UnitRoleId id);

    int  pageCount(Map<String, Object> filterDescMap);

    List<UnitRole>  pageQuery(Map<String, Object> pageQureyMap);

    List<UnitRole> listUnitRolesByUnitCode(String unitCode);

    List<UnitRole> listUnitRolesByRoleCode(String roleCode);
}
