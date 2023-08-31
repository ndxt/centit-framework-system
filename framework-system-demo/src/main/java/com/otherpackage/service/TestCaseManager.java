package com.otherpackage.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.database.utils.PageDesc;
import com.otherpackage.po.TestCase;

import java.util.Map;

public interface TestCaseManager {
    JSONArray listObjectsAsJson(CentitUserDetails ud,
                                Map<String, Object> filterMap, PageDesc pageDesc);

    boolean saveNewCase(CentitUserDetails ud, TestCase testCase);
}
