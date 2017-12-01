package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptFlowNoPool;
import com.centit.framework.system.po.OptFlowNoPoolId;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OptFlowNoPoolDao {

    /**
     * 根据Id查询
     * @param cid
     * @return
     */
    OptFlowNoPool getObjectById(OptFlowNoPoolId cid);

    /**
     * 删除
     * @param optFlowNoPool
     */
    void deleteObject(OptFlowNoPool optFlowNoPool);

    /**
     * 根据Id删除
     * @param cid
     */
    void deleteObjectById(OptFlowNoPoolId cid);


    /**
     * 新增
     * @param optFlowNoPool
     */
    void saveNewOptFlowNoPool(OptFlowNoPool optFlowNoPool);

    int  pageCount(Map<String, Object> filterDescMap);

    List<OptFlowNoPool>  pageQuery(Map<String, Object> pageQureyMap);


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
    long fetchFirstLsh(String ownerCode, String codeCode, Date codeBaseDate);
}
