package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.FVUserOptList;
import com.centit.framework.system.po.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userInfoDao")
public interface UserInfoDao
    extends com.centit.framework.system.dao.UserInfoDao {
      @Override
      List<FVUserOptList> getAllOptMethodByUser(@Param("userCode") String userCode);

      @Override
      UserInfo getUserByCode(@Param("userCode") String userCode);
      // return super.getObjectByProperty("loginName",loginName.toLowerCase());
      @Override
      UserInfo getUserByLoginName(@Param("loginName") String loginName);

      // return super.getObjectByProperty("regEmail", regEmail);
      @Override
      UserInfo getUserByRegEmail(@Param("regEmail") String regEmail);

      //return super.getObjectByProperty("regCellPhone", regCellPhone);
      @Override
      UserInfo getUserByRegCellPhone(@Param("regCellPhone") String regCellPhone);

      //return super.getObjectByProperty("userTag", userTag);
      @Override
      UserInfo getUserByTag(@Param("userTag") String userTag);

      //return super.getObjectByProperty("userWord", userWord);
      @Override
      UserInfo getUserByUserWord(@Param("userWord") String userWord);
      @Override
      UserInfo getUserByIdCardNo(@Param("idCardNo") String idCardNo);


      @Override
      int isLoginNameExist(@Param("userCode") String userCode, @Param("loginName") String loginName);
      @Override
      int isCellPhoneExist(@Param("userCode") String userCode, @Param("regCellPhone") String regCellPhone);
      @Override
      int isEmailExist(@Param("userCode") String userCode, @Param("regEmail") String regEmail);
      @Override
      int isAnyOneExist(@Param("userCode") String userCode, @Param("loginName") String loginName,
                           @Param("regCellPhone") String regCellPhone, @Param("regEmail") String regEmail);
}
