package com.centit.framework.system.dao;

import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;

import java.util.List;

/**
 * 个人设置Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface UserSettingDao {

    /**
    * 根据Id查询
    * @param userSettingId 个人设置Id
    * @return UserSetting
    */
    UserSetting getObjectById(UserSettingId userSettingId);

    /**
    * 根据Id删除
    * @param userSettingId 个人设置Id
    */
    void deleteObjectById(UserSettingId userSettingId);

    /**
    * 根据用户代码查询
    * @param userCode 用户代码
    * @return List<UserSetting>
    */
    List<UserSetting> getUserSettingsByCode(String userCode);

    /**
    * 根据用户代码和项目模块查询
    * @param userCode 用户代码
    * @param optId 项目模块
    * @return List<UserSetting>
    */
    List<UserSetting> getUserSettings(String userCode, String optId);

    /**
     * 查询全部个人设置
     * @return 个人设置列表
     */
    List<UserSetting> getAllSettings();

    /**
    * 新增
    * @param userSetting 个人设置对象
    */
    void saveNewUserSetting(UserSetting userSetting);

    /**
    * 更新个人设置
    * @param userSetting 个人设置对象
    */
    void updateObject(UserSetting userSetting);
}
