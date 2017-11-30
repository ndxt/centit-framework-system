package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;

import java.util.List;
import java.util.Map;

/**
 * 用户角色Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface UserRoleDao {

    /**
    * 新增
    * @param dbUserRole 用户角色
    */
    void saveNewObject(UserRole dbUserRole);

    /**
    * 查询全部
    * @return List<UserRole>
    */
    List<UserRole> listObjects();

    /**
     * 查询全部
     * @param userCode 用户编码
     * @return List<UserRole>
     */
    List<UserRole> listUserRoles(String userCode);
    /**
     * 查询全部
     * @param roleCode 角色编码
     * @return List<UserRole>
     */
    List<UserRole> listRoleUsers(String roleCode);
    /**
    *
    * @param dbUserRole
    */
    void mergeObject(UserRole dbUserRole);

    /**
    * 删除
    * @param dbUserRole 用户角儿对象
    */
    void deleteObject(UserRole dbUserRole);

    /**
    * 根据Id删除
    * @param id 用户角色Id
    */
    void deleteObjectById(UserRoleId id);

    /**
    * 根据Id查询
    * @param id 用户角色Id
    * @return UserRole
    */
    UserRole getObjectById(UserRoleId id);

    /**
    * 查询数量 用于分页
    * @param filterDescMap 过滤条件
    * @return int
    */
    int pageCount(Map<String, Object> filterDescMap);

    /**
    * 分页查询
    * @param pageQueryMap 过滤条件
    * @return List<UserRole>
    */
    List<UserRole> pageQuery(Map<String, Object> pageQueryMap);

    /**
    * 根据角色代码删除
    * @param roleCode 角色代码
    */
    void deleteByRoleId(String roleCode);

    /**
    * 根据用户代码删除
    * @param userCode 用户代码
    */
    void deleteByUserId(String userCode);

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
    * 所有的用户角色关系，包括 继承来的额角色
    * @param filterDescMap 条件Map
    * @return 返回列表数量
    */
    int  pageCountUserRole(Map<String, Object> filterDescMap);

    /**
    * 所有的用户角色关系，包括 继承来的额角色
    * @param pageQueryMap 条件Map
    * @return 返回列表
    */
    List<FVUserRoles> pageQueryUserRole(Map<String, Object> pageQueryMap);
}
