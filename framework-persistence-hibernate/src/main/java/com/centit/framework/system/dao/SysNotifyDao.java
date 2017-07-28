package com.centit.framework.system.dao;

import com.centit.framework.system.po.SysNotify;

public interface SysNotifyDao {
	
	//设置主键 o.setNotifyId(DatabaseOptUtils.getNextLongSequence(this, "S_MSGCODE"));
    SysNotify mergeObject(SysNotify o);
}
