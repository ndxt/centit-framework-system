package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作日志Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface OptLogDao  {
    /**
     * 生成一个新的序列
     * @return Long
     */
    Long createNewLogId();

    /**
     * 根据Id查询
     * @param logId 日志Id
     * @return OptLog
     */
    OptLog getObjectById(Long logId);

    /**
     * 新增
     * @param o 日志对象
     */
    void saveNewObject(OptLog o);

    /**
     * 根据Id删除
     * @param logId 日志Id
     */
    void deleteObjectById(Long logId);

    /**
     * 这个sql语句效率太低，应该从f_optinfo表中获取
     * @return 所有的业务列表
     */
    //final String hql = "select DISTINCT f.optId from OptLog f";
    List<String> listOptIds();

    /**
     *
     * @param o 日志对象
     */
    //设置主键 DatabaseOptUtils.getNextLongSequence(this, "S_SYS_LOG"));
    void mergeOptLog(OptLog o);

    /**
     * 根据开始
     * @param begin 起始时间
     * @param end 结束时间
     */
      //"delete from OptLog o where 1=1 ";  "and o.optTime > ?" "and o.optTime < ?";
      //参数 String beginDate, String endDate
    void delete(Date begin, Date end);


    /**
     * 查询条数
     * @param filterDescMap 过滤条件
     * @return int
     */
    int pageCount(Map<String, Object> filterDescMap);

    /**
     * 分页查询
     * @param pageQueryMap 过滤条件
     * @return List&lt;OptLog&gt;
     */
    List<OptLog>  pageQuery(Map<String, Object> pageQueryMap);
    /* 用于测试
     * 分页查询
     * @param pageQueryMap 过滤条件
     * @return List&lt;OptLog&gt;
     */
    //List<OptLog>  pageQueryByPDSql(Map<String, Object> pageQueryMap);

}
