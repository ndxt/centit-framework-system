package com.otherpackage.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.otherpackage.po.TestCase;

import java.util.HashMap;
import java.util.Map;

public class TestCaseDao extends BaseDaoImpl<TestCase, String> {

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("caseId", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }
}
