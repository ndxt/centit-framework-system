package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.QueryFilterCondition;


/**
 * QueryFilterConditionDao  Repository.
 * create by scaffold 2016-03-01 
 * @author codefan@sina.com
 * 系统内置查询方式null   
*/

public interface QueryFilterConditionDao extends BaseDao<QueryFilterCondition,Long> {

	//DatabaseOptUtils.getNextLongSequence(this, "S_FILTER_NO");
    Long getNextKey();
    
}
