package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.FVUserOptMoudleList;
import com.centit.framework.system.po.OptInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("optInfoDao")
public interface OptInfoDao
    extends com.centit.framework.system.dao.OptInfoDao {

    @Override
    List<FVUserOptMoudleList> getMenuFuncByUserID(@Param("userCode") String userCode, @Param("optType") String optType);

    @Override
    List<String> listUserDataPowerByOptMethod(@Param("userCode") String userCode,
                                              @Param("optId") String optid,
                                              @Param("optMethod") String optMethod);//zou_wy

    @Override
    List<OptInfo> listObjectByParentOptid(@Param("optId") String optId);

    /**
     * 根据菜单类型获取菜单
     * @param types 类型数组
     * @return 菜单列表
     */
    @Override
    List<OptInfo> listMenuByTypes(@Param("types") String... types);

}
