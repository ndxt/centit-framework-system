package com.centit.framework.system.dao;

import com.centit.framework.system.po.UserQueryFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;



/**
 * UserQueryFilterDao  Repository.
 * create by scaffold 2016-02-29 
 * @author codefan@sina.com
 * 用户自定义过滤条件表null   
*/

@Repository
public interface UserQueryFilterDao{
	
	void deleteObject(UserQueryFilter userQueryFilter);
	
	void mergeObject(UserQueryFilter userQueryFilter);
	
	Long saveNewObject(UserQueryFilter userQueryFilter);
	
	
    int  pageCount(Map<String, Object> filterDescMap);
    List<UserQueryFilter>  pageQuery(Map<String, Object> pageQureyMap);
	
	UserQueryFilter getObjectById(Long filterNo);
	//"From UserQueryFilter where userCode = ? and modleCode = ? "
			//+ "order by isDefault desc , createDate desc"
	// 参数 String userCode,String modelCode
	List<UserQueryFilter> listUserQueryFilterByModle(@Param("userCode") String userCode,
													 @Param("modelCode") String modelCode);
	
	//super.listObjectsAll("From UserQueryFilter where userCode = ? and modleCode = ? "
		//+ "and isDefault = 'T' order by isDefault desc , createDate desc",
	//参数 String userCode,String modelCode
	List<UserQueryFilter> listUserDefaultFilterByModle(@Param("userCode") String userCode,
															  @Param("modelCode") String modelCode);
	
	//= super.listObjectsAll("From UserQueryFilter where userCode = ? and modleCode = ? "
		//+ "and isDefault = 'T' order by isDefault desc , createDate desc",
		//new Object[]{userCode,modelCode});
	//public UserQueryFilter getUserDefaultFilterByModle(String userCode,String modelCode);
	
	// DatabaseOptUtils.getNextLongSequence(this, "S_FILTER_NO");
    Long getNextKey();
}
