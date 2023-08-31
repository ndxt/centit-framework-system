package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserSyncDirectoryDao;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.system.service.UserSyncDirectoryManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author tian_y
 */
@Service("userSyncDirectoryManager")
public class UserSyncDirectoryManagerImpl implements UserSyncDirectoryManager {

    @Autowired
    private UserSyncDirectoryDao userSyncDirectoryDao;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<UserSyncDirectory> listObjects() {
        return userSyncDirectoryDao.listObjectsAll();
    }

    @Override
    public List<UserSyncDirectory> listLdapDirectory() {
        return userSyncDirectoryDao.listObjectsByProperties(CollectionsOpt.createHashMap("type", "LDAP"));
    }

    @Override
    public List<UserSyncDirectory> listObjects(Map<String, Object> filterMap, PageDesc pageDesc, String userCode) {
        UserInfo userInfo = userInfoDao.getUserByCode(userCode);
        UnitInfo unitInfo = unitInfoDao.getObjectById(userInfo.getPrimaryUnit());
        String topUnit = "";
        if (null != unitInfo && StringUtils.isNotBlank(unitInfo.getTopUnit())) {
            topUnit = unitInfo.getTopUnit();
        }
        if (null != unitInfo && StringUtils.isBlank(userInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
            String[] unitCodeArray = unitInfo.getUnitPath().split("/");
            if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                topUnit = unitCodeArray[1];
            }
        }
        filterMap.put("topUnit", topUnit);
        return userSyncDirectoryDao.listObjectsByProperties(filterMap, pageDesc);
    }

    @Override
    public UserSyncDirectory getObjectById(String id) {
        return userSyncDirectoryDao.getObjectById(id);
    }

    @Override
    public void saveUserSyncDirectory(UserSyncDirectory userSyncDirectory, String userCode) {
        UserInfo userInfo = userInfoDao.getUserByCode(userCode);
        UnitInfo unitInfo = unitInfoDao.getObjectById(userInfo.getPrimaryUnit());
        if (null != unitInfo && StringUtils.isNotBlank(unitInfo.getTopUnit())) {
            userSyncDirectory.setTopUnit(unitInfo.getTopUnit());
        }
        if (null != unitInfo && StringUtils.isBlank(userInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
            String[] unitCodeArray = unitInfo.getUnitPath().split("/");
            if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                userSyncDirectory.setTopUnit(unitCodeArray[1]);
            }
        }
        userSyncDirectoryDao.saveNewObject(userSyncDirectory);
    }

    @Override
    public void updateUserSyncDirectory(UserSyncDirectory userSyncDirectory) {
        userSyncDirectoryDao.updateObject(userSyncDirectory);
    }

    @Override
    public void deleteUserSyncDirectoryById(String id) {
        userSyncDirectoryDao.deleteObjectById(id);
    }
}
