package com.centit.framework.system.dao;

import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingDao{
	
	 UserSetting getObjectById(UserSettingId userSettingId);
	
	/**
	 * update or insert
	 * @param userSetting userSetting
	 */
	 void mergeObject(UserSetting userSetting);

    // return listObjectsAll("From UserSetting where cid.userCode=?",userCode);
     List<UserSetting> getUserSettingsByCode(@Param("userCode") String userCode);
    
    // listObjectsAll("From UserSetting where cid.userCode=? and optId= ?",
    	//new Object[]{userCode,optID});
    //参数String userCode,String optID
     List<UserSetting> getUserSettings(@Param("userCode") String userCode, @Param("optId") String optId);
    
    //UserSetting us = new UserSetting(userCode,  paramCode, paramValue,
	 	//paramClass,  paramName);
     void saveUserSetting(UserSetting userSetting);
}
