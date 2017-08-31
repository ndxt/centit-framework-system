package com.centit.framework.system.dao;

import com.centit.framework.system.po.QueryFilterCondition;

import java.util.List;
import java.util.Map;


/**
 * QueryFilterConditionDao  Repository.
 * create by scaffold 2016-03-01 
 * @author codefan@sina.com
 * 系统内置查询方式null   
*/

public interface QueryFilterConditionDao {

	//DatabaseOptUtils.getNextLongSequence(this, "S_FILTER_NO");
    Long getNextKey();
    
    
    int  pageCount(Map<String, Object> filterDescMap);
    List<QueryFilterCondition>  pageQuery(Map<String, Object> pageQureyMap);
    
	
	QueryFilterCondition getObjectById(Long filterNo);

	void mergeObject(QueryFilterCondition userQueryFilter);
	
	void deleteObjectById(Long filterNo);

	void saveNewObject(QueryFilterCondition userQueryFilter);
}
