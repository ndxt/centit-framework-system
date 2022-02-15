package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.UserSyncDirectoryDao;
import com.centit.framework.system.po.UserSyncDirectory;
import com.centit.framework.system.service.UserSyncDirectoryManager;
import com.centit.support.database.utils.PageDesc;
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

    @Override
    public List<UserSyncDirectory> listObjects() {
        return userSyncDirectoryDao.listObjectsAll();
    }

    @Override
    public List<UserSyncDirectory> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return userSyncDirectoryDao.listObjects(filterMap, pageDesc);
    }

    @Override
    public UserSyncDirectory getObjectById(String id) {
        return userSyncDirectoryDao.getObjectById(id);
    }

    @Override
    public void saveUserSyncDirectory(UserSyncDirectory userSyncDirectory) {
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
