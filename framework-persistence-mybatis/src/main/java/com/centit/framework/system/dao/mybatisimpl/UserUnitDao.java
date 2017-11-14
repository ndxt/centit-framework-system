package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.UserUnit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userUnitDao")
public interface UserUnitDao
    extends com.centit.framework.system.dao.UserUnitDao {
      //"FROM UserUnit where userCode=? and unitCode=?",new Object[]{userCode,unitCode});
      //参数 String userCode,String unitCode
      @Override
      List<UserUnit> listObjectByUserUnit(@Param("userCode") String userCode, @Param("unitCode") String unitCode);
}
