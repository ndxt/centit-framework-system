package com.centit.framework.system.dao;

import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;

import java.util.List;
import java.util.Map;

public interface UserRoleDao {

    void saveNewObject(UserRole dbUserRole);

     List<UserRole> listObjects();

     void mergeObject(UserRole dbUserRole);

     void deleteObject(UserRole dbUserRole);

     void deleteObjectById(UserRoleId id);

     UserRole getObjectById(UserRoleId id);


     int  pageCount(Map<String, Object> filterDescMap);
     List<UserRole>  pageQuery(Map<String, Object> pageQureyMap);

     //DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.roleCode = ?", roid);
     void deleteByRoleId(String roleCode);

     //DatabaseOptUtils.doExecuteHql(this, "DELETE FROM UserRole WHERE id.userCode = ?", usid);
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
       * @param pageQureyMap 条件Map
       * @return 返回列表
       */
      List<FVUserRoles>  pageQueryUserRole(Map<String, Object> pageQureyMap);


}
