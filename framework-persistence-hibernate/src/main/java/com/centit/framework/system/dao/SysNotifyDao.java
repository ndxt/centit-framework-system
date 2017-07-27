package com.centit.framework.system.dao;

import com.centit.framework.hibernate.dao.BaseDao;
import com.centit.framework.system.po.SysNotify;

public interface SysNotifyDao extends BaseDao<SysNotify, Long> {
	
	//设置主键 o.setNotifyId(DatabaseOptUtils.getNextLongSequence(this, "S_MSGCODE"));
    SysNotify mergeObject(SysNotify o);
}
