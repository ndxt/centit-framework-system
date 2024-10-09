package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.support.algorithm.CollectionsOpt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OsInfoDao extends BaseDaoImpl<OsInfo, String> {

    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("osId", CodeBook.LIKE_HQL_ID);
        filterField.put("osName", CodeBook.LIKE_HQL_ID);
        filterField.put("osType", CodeBook.LIKE_HQL_ID);
        filterField.put("topUnit", CodeBook.LIKE_HQL_ID);
        filterField.put("hasInterface", CodeBook.LIKE_HQL_ID);
        filterField.put("interfaceUrl", CodeBook.LIKE_HQL_ID);
        filterField.put("created", CodeBook.LIKE_HQL_ID);
        filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);
        filterField.put("createTime", CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    @Transactional
    public List<OsInfo> listOsInfoByUnit(String topUnit){
        return super.listObjectsByFilter(
            " where IS_DELETE = 'F' AND TOP_UNIT = ?",
            new Object[]{topUnit});
    }

    @Transactional
    public OsInfo getOsInfoByRelOpt(String relOptId){
        if(StringUtils.isBlank(relOptId)) return null;
        return super.getObjectByProperties(CollectionsOpt.createHashMap("relOptId", relOptId));
    }

    public int countOsInfoByTopUnit(String topUnit) {
        if(StringUtils.isBlank(topUnit)) return 0;
        return this.countObjectByProperties(CollectionsOpt.createHashMap("topUnit", topUnit));
    }
}
