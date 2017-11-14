package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.UserQueryFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * UserQueryFilterDao  Repository.
 * create by scaffold 2016-02-29
 * @author codefan@sina.com
 * 用户自定义过滤条件表null
*/

@Repository("userQueryFilterDao")
public interface UserQueryFilterDao
  extends com.centit.framework.system.dao.UserQueryFilterDao {

    @Override
    List<UserQueryFilter> listUserQueryFilterByModle(@Param("userCode") String userCode,
                                                     @Param("modelCode") String modelCode);
    //super.listObjectsAll("From UserQueryFilter where userCode = ? and modleCode = ? "
        //+ "and isDefault = 'T' order by isDefault desc , createDate desc",
    //参数 String userCode,String modelCode
    @Override
    List<UserQueryFilter> listUserDefaultFilterByModle(@Param("userCode") String userCode,
                                                              @Param("modelCode") String modelCode);
}
