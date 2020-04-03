package com.centit.framework.system.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.SysNotify;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository("sysNotifyDao")
public class SysNotifyDao extends BaseDaoImpl<SysNotify, Long> {

    @Override
    public Map<String, String> getFilterField() {
        return null;
    }

    @Transactional
    public void mergeSysNotify(SysNotify o) {
        if (null == o.getNotifyId()) {
            o.setNotifyId(DatabaseOptUtils.getSequenceNextValue(this, "S_MSGCODE"));
        }
        /*return*/ super.mergeObject(o);
    }
}
