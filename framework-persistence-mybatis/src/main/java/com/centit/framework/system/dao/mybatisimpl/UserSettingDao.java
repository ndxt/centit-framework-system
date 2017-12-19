package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.UserSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userSettingDao")
public interface UserSettingDao
  extends com.centit.framework.system.dao.UserSettingDao {
    // return listObjectsAll("From UserSetting where cid.userCode=?",userCode);
    @Override
    List<UserSetting> getUserSettingsByCode(@Param("userCode") String userCode);

    //参数String userCode,String optID
    @Override
    List<UserSetting> getUserSettings(@Param("userCode") String userCode, @Param("optId") String optId);

    @Override
    String getValue(@Param("userCode") String userCode, @Param("key") String key);

}
