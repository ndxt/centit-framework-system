package com.centit.framework.system.dao.mybatisimpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao
    extends com.centit.framework.system.dao.UserInfoDao {
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
