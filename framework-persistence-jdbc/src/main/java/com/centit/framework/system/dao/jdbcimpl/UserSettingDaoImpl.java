package com.centit.framework.system.dao.jdbcimpl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.UserSettingDao;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userSettingDao")
public class UserSettingDaoImpl extends BaseDaoImpl<UserSetting, UserSettingId> implements UserSettingDao {

    public static final Logger logger = LoggerFactory.getLogger(UserSettingDaoImpl.class);

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

    @Override
    @Transactional
    public UserSetting getObjectById(UserSettingId userSettingId) {
        return super.getObjectById(userSettingId);
    }

    @Override
    @Transactional
    public void deleteObjectById(UserSettingId userSettingId) {
        super.deleteObjectById(userSettingId);
    }

    @Transactional
    public List<UserSetting> getUserSettingsByCode(String userCode) {
        return listObjectsByProperty("userCode",userCode);
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
}
