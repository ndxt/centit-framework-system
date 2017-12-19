package com.centit.framework.system.service.impl;

import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.dao.UserSettingDao;
import com.centit.framework.system.po.DataCatalog;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserSettingManagerImpl implements UserSettingManager {

    public static final Logger logger = LoggerFactory.getLogger(UserSettingManager.class);

    @Resource
    private UserSettingDao userSettingDao;

    @Resource
    private DataDictionaryDao dataDictionaryDao;

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
//    @CacheEvict(value ={"UserInfo","UserSetting"},allEntries = true)
    @Transactional
    public void saveNewUserSetting(UserSetting userSetting){
//        Map<String, Object> map = new HashMap<>();
//        map.put("catalogCode", "userSettingKey");
//        map.put("dataCode", userSetting.getParamCode());
//        DataDictionary dictionary = dataDictionaryDao.listObjects(map).get(0);
//        userSetting.setOptId(dictionary.getExtraCode());
        userSetting.setCreateDate(new Date());
        userSettingDao.saveNewUserSetting(userSetting);
    }
    @Override
//    @CacheEvict(value ={"UserInfo","UserSetting"},allEntries = true)
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
        List<UserSetting> userSettings = new ArrayList<>();

        searchColumn.put("catalogCode", "userSettingKey");
        List<DataDictionary> dataDictionaries = dataDictionaryDao.pageQuery(
            QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(searchColumn, pageDesc,
                    dataDictionaryDao.pageCount(searchColumn) ),DataDictionary.class));

        for(DataDictionary d : dataDictionaries){
            UserSetting userSetting = new UserSetting();
            userSetting.setParamCode(d.getDataCode());
            String value = userSettingDao.getValue(String.valueOf(searchColumn.get("userCode")), d.getDataCode());
            if("null".equals(value)){
                userSetting.setDefaultValue(true);
                userSetting.setUserCode("default");
                userSetting.setParamValue(userSettingDao.getValue("default", d.getDataCode()));
            }else{
                userSetting.setUserCode(String.valueOf(searchColumn.get("userCode")));
                userSetting.setParamValue(userSettingDao.getValue(String.valueOf(searchColumn.get("userCode")), d.getDataCode()));
            }
            userSetting.setOptId(d.getExtraCode());
            userSetting.setParamName(d.getDataDesc());
            userSettings.add(userSetting);
        }
        return userSettings;
    }

    @Override
    @Transactional
    public List<UserSetting> listDefaultSettings(Map<String, Object> map, PageDesc pageDesc){
        List<UserSetting> userSettings = new ArrayList<>();

        map.put("catalogCode", "userSettingKey");
        List<DataDictionary> dataDictionaries = dataDictionaryDao.pageQuery(
            QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(map, pageDesc,
                    dataDictionaryDao.pageCount(map) ),DataDictionary.class));

        for(DataDictionary d : dataDictionaries){
            UserSetting userSetting = new UserSetting(new UserSettingId("default", d.getDataCode()));
            userSetting.setParamValue(userSettingDao.getValue("default", d.getDataCode()));
            userSetting.setOptId(d.getExtraCode());
            userSetting.setParamName(d.getDataDesc());
            userSettings.add(userSetting);
        }
        return userSettings;
    }

    @Override
    public List<UserSetting> listObjects(Map<String, Object> searchColumn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserSetting getObjectById(UserSettingId userSettingid) {
        return userSettingDao.getObjectById(userSettingid);
    }

    @Override
    public void deleteObject(UserSetting userSetting) {
        userSettingDao.deleteObject(userSetting);

    }

}
