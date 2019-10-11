package com.centit.framework.system.dao;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.support.algorithm.StringBaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userSettingDao")
public class UserSettingDao extends BaseDaoImpl<UserSetting, UserSettingId> {

    public static final Logger logger = LoggerFactory.getLogger(UserSettingDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put(CodeRepositoryUtil.USER_CODE,  CodeBook.LIKE_HQL_ID);
            filterField.put("paramCode",  CodeBook.LIKE_HQL_ID);
            filterField.put("paramValue", CodeBook.LIKE_HQL_ID);
            filterField.put("paramClass", CodeBook.LIKE_HQL_ID);
            filterField.put("paramName", CodeBook.LIKE_HQL_ID);
            filterField.put("createDate", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    @Transactional
    public UserSetting getObjectById(UserSettingId userSettingId) {
        return super.getObjectById(userSettingId);
    }

    @Transactional
    public void deleteObjectById(UserSettingId userSettingId) {
        super.deleteObjectById(userSettingId);
    }

    @Transactional
    public List<UserSetting> getUserSettingsByCode(String userCode) {
        return listObjectsByProperty("userCode",userCode);
    }

    public List<UserSetting> getAllSettings(){
        return super.listObjects();
    }

    @Transactional
    public List<UserSetting> getUserSettings(String userCode,String optID) {
        return listObjectsByFilter(" where USER_CODE =? and OPT_ID = ?",
                new Object[]{userCode,optID});
    }

    @Transactional
    public void saveNewUserSetting(UserSetting us){
        super.saveNewObject(us);
    }

    /**
     * 更新个人设置
     *
     * @param userSetting 个人设置对象
     */
    @Transactional
    public void updateUserSetting(UserSetting userSetting) {
        super.updateObject(userSetting);
    }

    public String getValue(String userCode, String key){
        String sql = "SELECT PARAM_VALUE FROM F_USERSETTING WHERE USER_CODE = ? AND PARAM_CODE = ?";
        return StringBaseOpt.castObjectToString(DatabaseOptUtils.getScalarObjectQuery(this, sql, new Object[]{userCode, key}));
    }

}
