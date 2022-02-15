package com.centit.framework.system.service;

import com.centit.framework.system.po.UserSyncDirectory;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author tian_y
 */
public interface UserSyncDirectoryManager {

    List<UserSyncDirectory> listObjects();

    List<UserSyncDirectory> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    UserSyncDirectory getObjectById(String Id);

    void saveUserSyncDirectory(UserSyncDirectory userSyncDirectory);

    void updateUserSyncDirectory(UserSyncDirectory userSyncDirectory);

    void deleteUserSyncDirectoryById(String id);
}
