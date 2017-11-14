package com.centit.framework.system.dao.mybatisimpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OptDataScopeDao
  extends com.centit.framework.system.dao.OptDataScopeDao {
       // listObjectsAll("FROM OptDataScope WHERE optId in ?", scopeCodes)
    @Override
    List<String> listDataFiltersByIds(@Param("scopeCodes") Collection<String> scopeCodes);//zou_wy
}
