package com.centit.framework.system.dao.mybatisimpl;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("optFlowNoPoolDao")
public interface OptFlowNoPoolDao
    extends com.centit.framework.system.dao.OptFlowNoPoolDao {
    /**
     *  "select min(CurNo) as MinNo from F_OptFlowNoPool" +
                " where OwnerCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeCode = " + QueryUtils.buildStringForQuery(ownerCode) +
                " and CodeDate = to_date(" + QueryUtils.buildStringForQuery(
                DatetimeOpt.convertDatetimeToString(codeBaseDate))
                + ",'YYYY-MM-DD HH:MI:SS')");
     * @param codeBaseDate 编码基准日期
     * @param codeCode 编码类别
     * @param ownerCode 归属人员
     * @return long
     */
     @Override
     long fetchFirstLsh(@Param("ownerCode") String ownerCode,
                        @Param("codeCode") String codeCode,
                        @Param("codeBaseDate") Date codeBaseDate);
}
