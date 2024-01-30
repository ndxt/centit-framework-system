package com.centit.framework.users.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.users.po.SocialDeptAuth;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SocialDeptAuthDao extends BaseDaoImpl<SocialDeptAuth, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("unitCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("deptId", CodeBook.EQUAL_HQL_ID);

        return filterField;
    }
}
