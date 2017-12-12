package com.centit.framework.system.service.impl;

import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.UserSettingDao;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserSettingManagerImpl implements UserSettingManager {

    public static final Logger logger = LoggerFactory.getLogger(UserSettingManager.class);

    @Resource
    private UserSettingDao userSettingDao;

    @Override
    public List<UserSetting> getUserSettings(String userCode) {
        return userSettingDao.getUserSettingsByCode(userCode);
    }

    @Override
    public List<UserSetting> getUserSettings(String userCode,String optID) {
        return userSettingDao.getUserSettings(userCode, optID);
    }

    @Override
    public UserSetting getUserSetting(String userCode, String paramCode) {

        return userSettingDao.getObjectById(new UserSettingId(userCode,paramCode));
    }

    @Override
    @Transactional
    public void saveNewUserSetting(UserSetting userSetting){
        userSettingDao.saveNewUserSetting(userSetting);
    }
    @Override
    @Transactional
    public void updateUserSetting(UserSetting userSetting){
        userSettingDao.updateObject(userSetting);
    }
    @Override
    @Transactional
    public void saveUserSetting(String userCode,String paramCode,String paramName,String paramValue,String optId){
        UserSetting userSetting = new UserSetting( userCode,  paramCode, paramValue,
                                        optId,  paramName);
        userSettingDao.saveNewUserSetting(userSetting);
    }

    @Override
    public List<UserSetting> getAllSettings(){
        return userSettingDao.getAllSettings();
    }

    @Override
    public List<UserSetting> listObjects(Map<String, Object> searchColumn, PageDesc pageDesc) {
        return userSettingDao.pageQuery(
            QueryParameterPrepare.prepPageParams(
                searchColumn,pageDesc,userSettingDao.pageCount(searchColumn)));
    }

    @Override
    public List<UserSetting> listObjects(Map<String, Object> searchColumn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserSetting getObjectById(UserSettingId userSettingid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteObject(UserSetting userSetting) {
        // TODO Auto-generated method stub

    }



}
