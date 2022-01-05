package com.centit.framework.system.service;

import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.RolePower;
import com.centit.support.database.utils.PageDesc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SysRoleManager{

    List<RoleInfo> listObjects(Map<String, Object> filterMap);

    List<RoleInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    RoleInfo getObjectById(String roleCode);

    List<RolePower> getRolePowers(String rolecode); // 角色操作权限

    List<RolePower> getRolePowersByDefCode(String optCode);

    Serializable saveNewRoleInfo(RoleInfo o);

    void updateRoleInfo(RoleInfo o);

    /**
     * 更新角色权限
     * @param o 角色对象
     * @return 旧的操作权限
     */
    List<RolePower> updateRolePower(RoleInfo o);

    /**
     * 根据单位code更新角色权限
     * @param o
     * @param topUnit
     * @return
     */
    List<RolePower> updateRolePower(RoleInfo o,String topUnit);

    void deleteRoleInfo(String roleCode);

    RoleInfo getRoleInfo(String roleCode);

    List<RolePower> listAllRolePowers();

    List<OptMethod> listAllOptMethods();

    int countRoleUserSum(String roleCode);

  /**
   * 判断角色名称是否重复
   * @param roleName 角色名称
   * @param roleCode 角色代码 （新增时设为null）
   * @param unitCode 部门代码 （系统角色设为null）
   * @return 名称是否可用 （true 可用； false 不可用）
   */
    boolean judgeSysRoleNameCanBeUsed(String roleName, String roleCode, String unitCode);

    /**
     * 根据optCode查询角色信息
     * @param optCode
     * @return
     */
    List<RoleInfo> listRoleInfoByOptCode(String optCode);

    /**
     * 根据optCode更新F_ROLEPOWER中的信息
     * @param optCode 操作code
     * @param roleCode 角色code 当roleCode为空时，意味着要删除与optCode相关的所有数据
     */
    void updateRolePower(String optCode, String roleCode);
}
