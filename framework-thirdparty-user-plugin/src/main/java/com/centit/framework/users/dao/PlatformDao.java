package com.centit.framework.users.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.users.po.Platform;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zfg
 */
@Repository("platformDao")
public class PlatformDao extends BaseDaoImpl<Platform, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("platId", CodeBook.EQUAL_HQL_ID);
        filterField.put("platSourceCode", CodeBook.EQUAL_HQL_ID);
        filterField.put("platType", CodeBook.EQUAL_HQL_ID);
        filterField.put("corpId", CodeBook.LIKE_HQL_ID);
        filterField.put("appKey", CodeBook.EQUAL_HQL_ID);
        filterField.put("appSecret", CodeBook.EQUAL_HQL_ID);

        return filterField;
    }

    /**
     *
     * @param platSourceCode 平台来源代码
     * @param platId 平台ID
     * @return
     */
    public boolean isUniquePlat(String platSourceCode, String platId) {
        String sql = "select count(*) as hasSameNamePlat from F_PLATFORM u " +
            "where u.PLAT_SOURCE_CODE = :platSourceCode and" +
            " u.PLAT_ID <> :platId";
        Object hasSameNamePlat = DatabaseOptUtils.getScalarObjectQuery(this, sql, CollectionsOpt.createHashMap(
            "platSourceCode", platSourceCode, "platId", platId));
        return NumberBaseOpt.castObjectToInteger(hasSameNamePlat, 0) > 0;
    }
}
