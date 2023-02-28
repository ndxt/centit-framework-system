package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.framework.system.service.SysUserRoleManager;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JSONOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    @NotNull
    protected UserRoleDao userRoleDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public void mergeObject(UserRole dbUserRole, UserRole userRole) {
        userRoleDao.deleteObject(dbUserRole);

        userRoleDao.mergeUserRole(userRole);
    }

    @Override
    public JSONArray listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        List<UserRole> userRolesList = userRoleDao.listObjectsByProperties(filterMap, pageDesc);
        JSONArray userRoles = JSONOpt.arrayToJSONArray(userRolesList);
        for (Object a : userRoles) {
            JSONObject aa = (JSONObject) a;
            String userCode = aa.getString("userCode");
            IUserInfo userinfo = CodeRepositoryUtil.getUserInfoByCode(
                StringBaseOpt.objectToString(filterMap.get("topUnit")), userCode);
            if(userinfo!=null) {
                String loginName = userinfo.getLoginName();
                aa.put("loginName", loginName);
            }
        }
        return DictionaryMapUtils.mapJsonArray(userRoles, UserRole.class);
    }

    @Override
    public UserRole getObjectById(UserRoleId id) {
        return userRoleDao.getObjectById(id);
    }

    @Override
    public void mergeObject(UserRole dbUserRole) {
        userRoleDao.mergeUserRole(dbUserRole);
    }

    @Override
    public void deleteObjectById(UserRoleId id) {
        userRoleDao.deleteObjectById(id);
    }

    @Override
    @Transactional
    public List<UserInfo> listUsersByRole(String roleCode) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("queryByRole", roleCode);
        return userInfoDao.listObjects(map);
    }

    @Override
    @Transactional
    public List<FVUserRoles> listUserRolesByUserCode(String userCode) {
        return userRoleDao.listUserRolesByUserCode(userCode);
    }

    @Override
    @Transactional
    public List<FVUserRoles> listRoleUsersByRoleCode(String roleCode) {
        return userRoleDao.listRoleUsersByRoleCode(roleCode);
    }

    @Override
    @Transactional
    public JSONArray pageQueryUserRole(Map<String, Object> filterMap, PageDesc pageDesc) {
        return userRoleDao.pageQueryUserRole(filterMap, pageDesc);
    }

    /**
     * 查询全部
     *
     * @param userCode 用户编码
     * @return List &lt; UserRole &gt;
     */
    @Override
    @Transactional
    public List<UserRole> listUserRoles(String userCode) {
        return userRoleDao.listUserRoles(userCode);
    }

    /**
     * 查询全部
     *
     * @param roleCode 角色编码
     * @return List &lt; UserRole &gt;
     */
    @Override
    @Transactional
    public List<UserRole> listRoleUsers(String roleCode) {
        return userRoleDao.listRoleUsers(roleCode);
    }
}
