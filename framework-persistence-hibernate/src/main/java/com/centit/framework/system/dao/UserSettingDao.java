package com.centit.framework.system.dao;

import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;

import java.util.List;

public interface UserSettingDao {

     UserSetting getObjectById(UserSettingId userSettingId);

    /**
     * update or insert
     * @param userSetting userSetting
     */
    void mergeObject(UserSetting userSetting);

    // return listObjectsAll("From UserSetting where cid.userCode=?",userCode);
     List<UserSetting> getUserSettingsByCode(String userCode);
    
    // listObjectsAll("From UserSetting where cid.userCode=? and optId= ?",
        //new Object[]{userCode,optID});
    //参数String userCode,String optID
     List<UserSetting> getUserSettings(String userCode,  String optId);
    
    //UserSetting us = new UserSetting(userCode,  paramCode, paramValue,
         //paramClass,  paramName);
     void saveUserSetting(UserSetting userSetting);
}
