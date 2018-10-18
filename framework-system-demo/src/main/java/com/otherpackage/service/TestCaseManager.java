package com.otherpackage.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

public interface TestCaseManager {
    JSONArray listObjectsAsJson(CentitUserDetails ud,
                                Map<String, Object> filterMap, PageDesc pageDesc);
}
