package com.centit.framework.system.service;

import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface UserSettingManager {

    List<UserSetting> getUserSettings(String userCode);

    List<UserSetting> getUserSettings(String userCode,String optID);

    UserSetting getUserSetting(String userCode,String paramCode);

    void saveNewUserSetting(UserSetting userSetting);

    void updateUserSetting(UserSetting userSetting);

    void saveUserSetting(String userCode,String paramCode,String paramName,String paramValue,String optId);

    List<UserSetting> listObjects(Map<String,Object>searchColumn,PageDesc pageDesc);

    /**
     * 查询全部默认用户设置
     * @param pageDesc 分页信息
     * @param map 过滤条件
     * @return 个人设置列表
     */
    List<UserSetting> listDefaultSettings(Map<String, Object> map, PageDesc pageDesc);

    List<UserSetting> listObjects(Map<String,Object>searchColumn);

    UserSetting getObjectById(UserSettingId userSettingid);

    void deleteObject(UserSetting userSetting);

    List<UserSetting> getAllSettings();
}
