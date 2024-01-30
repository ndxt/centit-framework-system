package com.centit.framework.users.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.users.po.AccessToken;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zfg
 */
@Repository("accessTokenDao")
public class AccessTokenDao extends BaseDaoImpl<AccessToken,String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("appId", CodeBook.EQUAL_HQL_ID);
        filterField.put("accessToken", CodeBook.EQUAL_HQL_ID);
        filterField.put("expireIn", CodeBook.EQUAL_HQL_ID);
        filterField.put("expireTime", CodeBook.EQUAL_HQL_ID);

        return filterField;
    }
}
