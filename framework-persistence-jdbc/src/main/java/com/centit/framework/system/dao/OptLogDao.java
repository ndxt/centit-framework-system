package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OptLogDao  {
	//生成一个新的id序列	 S_SYS_LOG
	Long createNewLogId();
	
	OptLog getObjectById(Long logId);

    void saveNewObject(OptLog o);
	
	void deleteObjectById(Long logId);

	//final String hql = "select DISTINCT f.optId from OptLog f";
    List<String> listOptIds();

    //设置主键 DatabaseOptUtils.getNextLongSequence(this, "S_SYS_LOG"));
    void mergeObject(OptLog o);

    //"delete from OptLog o where 1=1 ";  "and o.optTime > ?" "and o.optTime < ?";
    //参数 String beginDate, String endDate
    void delete(Date begin, Date end);
    
    
    //分页  //startRow  startRow
    int  pageCount(Map<String, Object> filterDescMap);
    
    List<OptLog>  pageQuery(Map<String, Object> pageQureyMap);

}
