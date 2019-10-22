package com.centit.framework.system.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.system.po.QueryFilterCondition;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * QueryFilterCondition  Service.
 * create by scaffold 2016-03-01
 * @author codefan@sina.com
 * 系统内置查询方式null
*/

public interface QueryFilterConditionManager
{

    List<QueryFilterCondition> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    QueryFilterCondition getObjectById(Long filterNo);

    void mergeObject(QueryFilterCondition userQueryFilter);

    void deleteObjectById(Long filterNo);

    Long saveNewObject(QueryFilterCondition userQueryFilter);

    JSONArray listQueryFilterConditionsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);
}
