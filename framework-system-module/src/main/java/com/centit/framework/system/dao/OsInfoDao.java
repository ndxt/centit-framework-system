package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.po.OsInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OsInfoDao extends BaseDaoImpl<OsInfo, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("osId", CodeBook.LIKE_HQL_ID);
        filterField.put("osName", CodeBook.LIKE_HQL_ID);
        filterField.put("hasInterface", CodeBook.LIKE_HQL_ID);
        filterField.put("interfaceUrl", CodeBook.LIKE_HQL_ID);
        filterField.put("created", CodeBook.LIKE_HQL_ID);
        filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);
        filterField.put("createTime", CodeBook.LIKE_HQL_ID);
        return filterField;
    }
}
