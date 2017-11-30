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
     *
     * @return
     */
    //final String hql = "select DISTINCT f.optId from OptLog f";
      List<String> listOptIds();

    /**
     *
     * @param o
     */
    //设置主键 DatabaseOptUtils.getNextLongSequence(this, "S_SYS_LOG"));
      void mergeObject(OptLog o);

    /**
     * 根据开始
     * @param begin
     * @param end
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
     * @return List<OptLog>
     */
      List<OptLog>  pageQuery(Map<String, Object> pageQueryMap);

}
