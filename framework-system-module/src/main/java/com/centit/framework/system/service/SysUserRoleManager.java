package com.centit.framework.system.service;

import java.util.List;
import java.util.Map;

import com.centit.support.database.utils.PageDesc;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;


/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public interface SysUserRoleManager{

    List<UserRole> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    UserRole getObjectById(UserRoleId id);

    void mergeObject(UserRole dbUserRole);

    void deleteObjectById(UserRoleId id);

    void mergeObject(UserRole dbUserRole, UserRole userRole);
}
