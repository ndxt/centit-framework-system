package com.centit.framework.system.dao.mybatisimpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OptLogDao
  extends com.centit.framework.system.dao.OptLogDao {

    //"delete from OptLog o where 1=1 ";  "and o.optTime > ?" "and o.optTime < ?";
    //参数 String beginDate, String endDate
    @Override
    void delete(@Param("beginDate") Date begin, @Param("endDate") Date end);
}
