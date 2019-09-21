package com.centit.framework.system.dao;


import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.po.UnitRoleId;

import java.util.List;
import java.util.Map;

/**
 * 机构角色Dao
 * @author codefan
 * updated by zou_wy@centit.com
 */
public interface UnitRoleDao {

    /**
    * 新增
    * @param dbUnitRole 机构角色对象
    */
    void saveNewObject(UnitRole dbUnitRole);

    /**
    * 更新
    * @param dbUnitRole 机构角色对象
    */
    void updateUnitRole(UnitRole dbUnitRole);

    /**
    *
    * @param dbUnitRole 机构角色对象
    */
    void mergeUnitRole(UnitRole dbUnitRole);

    /**
    * 删除
    * @param unitRole 机构角色对象
    */
    void deleteUnitRole(UnitRole unitRole);

    /**
    * 根据Id删除
    * @param id 机构角色Id
    */
    void deleteUnitRoleById(UnitRoleId id);

    /**
    * 根据Id查询
    * @param id 机构角色Id
    * @return UnitRole
    */
    UnitRole getUnitRoleById(UnitRoleId id);

    /**
    * 查询数量 用于分页
    * @param filterDescMap 过滤条件
    * @return int
    */
    int pageCount(Map<String, Object> filterDescMap);

    /**
    * 分页查询
    * @param pageQueryMap 分页查询条件
    * @return List &lt;UnitRole&gt;
    */
    List<UnitRole> pageQuery(Map<String, Object> pageQueryMap);

    /**
    * 根据机构代码查询
    * @param unitCode 机构代码
    * @return List &lt;UnitRole&gt;
    */
    List<UnitRole> listUnitRolesByUnitCode(String unitCode);

    /**
    * 根据角色代码查询
    * @param roleCode 角色代码
    * @return List &lt;UnitRole&gt;
    */
    List<UnitRole> listUnitRolesByRoleCode(String roleCode);
}
