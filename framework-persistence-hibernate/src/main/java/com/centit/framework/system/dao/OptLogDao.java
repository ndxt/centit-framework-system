package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.OptLog;

import java.util.Date;
import java.util.List;

public interface OptLogDao extends BaseDao<OptLog, Long> {
	//生成一个新的id序列	 S_SYS_LOG
	Long createNewLogId();
	

	//final String hql = "select DISTINCT f.optId from OptLog f";
    List<String> listOptIds();


    //"delete from OptLog o where 1=1 ";  "and o.optTime > ?" "and o.optTime < ?";
    //参数 String beginDate, String endDate
    void delete(Date begin, Date end);

}
