package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.DataDictionaryDao;
import com.centit.framework.system.dao.UserSettingDao;
import com.centit.framework.system.po.DataDictionary;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询用户个人设置
     * @param searchColumn
     * @param pageDesc
     * @return
     */
    @Override
    public JSONArray listObjects(Map<String, Object> searchColumn, PageDesc pageDesc) {
        searchColumn.put("catalogCode", "userSettingKey");
        List<DataDictionary> dataDictionaries = dataDictionaryDao.pageQuery(
            QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(searchColumn, pageDesc,
                    dataDictionaryDao.pageCount(searchColumn) ),DataDictionary.class));

        JSONArray userSettings = new JSONArray(10);
        for(DataDictionary d : dataDictionaries){
            JSONObject userSetting = new JSONObject();
            //参数编码
            userSetting.put("paramCode", d.getDataCode());
            //个人设置的值
            String value = userSettingDao.getValue(String.valueOf(searchColumn.get("userCode")), d.getDataCode());
            if("".equals(value) || value == null){
                userSetting.put("defaultValue", true);
                userSetting.put("paramValue", userSettingDao.getValue("default", d.getDataCode()));
            }else{
                userSetting.put("paramValue", value);
            }
            //用户代码
            userSetting.put("userCode", StringBaseOpt.castObjectToString(searchColumn.get("userCode")));
            //业务代码
            userSetting.put("optId", d.getExtraCode2());
            //参数名称
            userSetting.put("paramName", d.getDataValue());
            //页面展示方式
            userSetting.put("showType", d.getDataTag());
            //候选值
            userSetting.put("typeValue", d.getExtraCode());
            //描述
            userSetting.put("paramDesc", d.getDataDesc());
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
            userSetting.setDefaultValue(true);
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
