package com.centit.framework.system.service;

import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.RolePower;
import com.centit.framework.system.po.VOptTree;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SysRoleManager{
	
	List<RoleInfo> listObjects(Map<String, Object> filterMap);

	List<RoleInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);
	
	RoleInfo getObjectById(String roleCode);
	
    List<RolePower> getRolePowers(String rolecode); // 角色操作权限

    List<RolePower> getRolePowersByDefCode(String optCode);

    List<VOptTree> getVOptTreeList();// 获取菜单TREE

    void loadRoleSecurityMetadata();

    Serializable saveNewRoleInfo(RoleInfo o);

    void updateRoleInfo(RoleInfo o);

    List<RolePower> updateRolePower(RoleInfo o);
    
    void deleteRoleInfo(String roleCode);
    
    RoleInfo getRoleInfo(String roleCode);
    
    List<RolePower> listAllRolePowers();

    List<OptMethod> listAllOptMethods();

    int countRoleUserSum(String roleCode);

    RoleInfo getRoleByName(String roleName);

    boolean isRoleNameNotExist(String roleName, String roleCode);
}
