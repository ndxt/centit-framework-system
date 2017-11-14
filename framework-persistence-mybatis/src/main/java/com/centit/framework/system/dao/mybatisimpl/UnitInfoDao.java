package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.UnitInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitInfoDao
  extends com.centit.framework.system.dao.UnitInfoDao {


    @Override
    UnitInfo getPeerUnitByName(@Param("unitName") String unitName,
                               @Param("parentUnit") String parentUnit,
                               @Param("unitCode") String unitCode);

    @Override
    Integer isExistsUnitByParentAndOrder(@Param("parentUnit") String parentUnit,@Param("unitOrder") long unitOrder);
}
