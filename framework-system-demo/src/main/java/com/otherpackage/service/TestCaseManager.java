package com.otherpackage.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.database.utils.PageDesc;
import com.otherpackage.po.TestCase;

import java.util.Map;

public interface TestCaseManager {
    JSONArray listObjectsAsJson(CentitUserDetails ud,
                                Map<String, Object> filterMap, PageDesc pageDesc);

    boolean saveNewCase(CentitUserDetails ud, TestCase testCase);
}
