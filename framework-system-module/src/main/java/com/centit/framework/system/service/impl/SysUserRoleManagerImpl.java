package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.po.UserInfo;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.framework.system.service.SysUserRoleManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class SysUserRoleManagerImpl implements SysUserRoleManager {

    @Resource
    @NotNull
    protected UserRoleDao userRoleDao;

    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public void mergeObject(UserRole dbUserRole, UserRole userRole) {
        userRoleDao.deleteObject(dbUserRole);

        userRoleDao.mergeObject(userRole);
    }

    @Override
    public List<UserRole> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return userRoleDao.pageQuery(QueryParameterPrepare.prepPageParams(filterMap,pageDesc,userRoleDao.pageCount(filterMap)));
    }

    @Override
    public UserRole getObjectById(UserRoleId id) {
        return userRoleDao.getObjectById(id);
    }

    @Override
    public void mergeObject(UserRole dbUserRole) {
        userRoleDao.mergeObject(dbUserRole);
    }

    @Override
    public void deleteObjectById(UserRoleId id) {
        userRoleDao.deleteObjectById(id);
    }

    @Override
    @Transactional
    public List<UserInfo> listUsersByRole(String roleCode){
        Map<String, Object> map = new HashMap<>(2);
        map.put("queryByRole", roleCode);
        return userInfoDao.listObjects(map);
    }
}
