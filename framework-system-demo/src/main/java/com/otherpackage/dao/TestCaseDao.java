package com.otherpackage.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.otherpackage.po.TestCase;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository("testCaseDao")
public class TestCaseDao extends BaseDaoImpl<TestCase, String> {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("caseId", CodeBook.EQUAL_HQL_ID);
        return filterField;
    }
}
