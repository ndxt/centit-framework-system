package com.centit.framework.users.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.users.po.DingTalkSuite;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zfg
 */
@Repository("dingTalkSuiteDao")
public class DingTalkSuiteDao extends BaseDaoImpl<DingTalkSuite,String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("id", CodeBook.EQUAL_HQL_ID);
        filterField.put("suiteid", CodeBook.EQUAL_HQL_ID);
        filterField.put("name", CodeBook.LIKE_HQL_ID);
        filterField.put("token", CodeBook.EQUAL_HQL_ID);
        filterField.put("encodingAesKey", CodeBook.EQUAL_HQL_ID);
        filterField.put("suiteKey", CodeBook.EQUAL_HQL_ID);
        filterField.put("suiteSecret", CodeBook.EQUAL_HQL_ID);
        filterField.put("suitTicket", CodeBook.EQUAL_HQL_ID);
        filterField.put("suiteAccessToken", CodeBook.EQUAL_HQL_ID);

        return filterField;
    }
}
