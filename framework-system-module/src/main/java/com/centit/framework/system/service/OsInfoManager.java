package com.centit.framework.system.service;


import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.system.po.OsInfo;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface OsInfoManager extends BaseEntityManager<OsInfo, String> {

    List<OsInfo> listObjects(Map<String, Object> map);

    List<OsInfo> listObjects(Map<String, Object> map, PageDesc pageDesc);

    JSONArray listOsInfoAsJson(Map<String, Object> filterMap, PageDesc pageDesc);

    boolean refreshSingle(OsInfo osInfo);

    boolean refreshAll();
}
