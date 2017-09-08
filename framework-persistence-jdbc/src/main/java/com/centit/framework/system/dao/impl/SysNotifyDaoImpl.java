package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.SysNotifyDao;
import com.centit.framework.system.po.SysNotify;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository("sysNotifyDao")
public class SysNotifyDaoImpl extends BaseDaoImpl<SysNotify, Long> implements SysNotifyDao {

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();

        }
        return filterField;
    }


    @Override
    @Transactional
    public void mergeObject(SysNotify o) {
        if (null == o.getNotifyId()) {
            o.setNotifyId(DatabaseOptUtils.getSequenceNextValue(this, "S_MSGCODE"));
        }
        /*return*/ super.mergeObject(o);
    }
}
