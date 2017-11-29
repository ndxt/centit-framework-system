package com.centit.framework.system.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public interface SysUserRoleManager{

    JSONArray listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    UserRole getObjectById(UserRoleId id);

    void mergeObject(UserRole dbUserRole);

    void deleteObjectById(UserRoleId id);

    void mergeObject(UserRole dbUserRole, UserRole userRole);

    List<UserInfo> listUsersByRole(String roleCode);

    /**
     * List roleInfos = new ArrayList();
     //所有的用户 都要添加这个角色
     roleInfos.add(new RoleInfo("G-", "general ","G",
     "G","T", "general "));
     final String sSqlsen = "from FVUserRoles v where userCode = ?";
     * @param userCode usid
     * @return List FVUserRoles
     */
    List<FVUserRoles> listUserRolesByUserCode(String userCode);

    /**
     * List roleInfos = new ArrayList();
     //所有的用户 都要添加这个角色
     roleInfos.add(new RoleInfo("G-", "general ","G",
     "G","T", "general "));
     final String sSqlsen = "from FVUserRoles v where v.id.roleCode = ? "
     * @param roleCode 角色代码
     * @return 拥有该角色的用户
     */
    List<FVUserRoles> listRoleUsersByRoleCode(String roleCode);

    /**
     * 获取角色对应的人
     * @param filterMap 条件参数 只能是  roleCode  userCode obtainType
     * @param pageDesc 分页信息
     * @return 角色列表
     */
    JSONArray pageQueryUserRole( Map<String, Object> filterMap, PageDesc pageDesc);
}
