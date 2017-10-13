package com.centit.framework.system.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.system.po.UserQueryFilter;

import java.util.List;
import java.util.Map;

/**
 * UserQueryFilter  Service.
 * create by scaffold 2016-02-29 
 * @author codefan@sina.com
 * 用户自定义过滤条件表null   
*/

public interface UserQueryFilterManager
{

    void mergeObject(UserQueryFilter userQueryFilter);

    void saveNewObject(UserQueryFilter userQueryFilter);

    List<UserQueryFilter>listObjects(Map<String, Object> filterMap, PageDesc pageDesc);
        JSONArray listUserQueryFiltersAsJson(
     String[] fields,
     Map<String, Object> filterMap, PageDesc pageDesc);

    List<UserQueryFilter> listUserQueryFilterByModle(String userCode,String modelCode);

    Long getNextFilterKey();

    Long saveUserDefaultFilter(UserQueryFilter userQueryFilter);

    UserQueryFilter getUserDefaultFilter(String userCode,String modelCode);

    UserQueryFilter getUserQueryFilter(Long filterNo);

    boolean deleteUserQueryFilter(Long filterNo);

}
