package com.centit.framework.system.dao;

import java.util.List;
import java.util.Map;

import com.centit.framework.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.centit.framework.system.po.QueryFilterCondition;



/**
 * QueryFilterConditionDao  Repository.
 * create by scaffold 2016-03-01 
 * @author codefan@sina.com
 * 系统内置查询方式null   
*/

@Repository
public interface QueryFilterConditionDao extends BaseDao {

	//DatabaseOptUtils.getNextLongSequence(this, "S_FILTER_NO");
    Long getNextKey();
    
    
    int  pageCount(Map<String, Object> filterDescMap);
    List<QueryFilterCondition>  pageQuery(Map<String, Object> pageQureyMap);
    
	
	QueryFilterCondition getObjectById(Long filterNo);
	
	void mergeObject(QueryFilterCondition userQueryFilter);
	
	void deleteObjectById(Long filterNo);
			
	Long saveNewObject(QueryFilterCondition userQueryFilter);
}
