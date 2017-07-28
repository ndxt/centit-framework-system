package com.centit.framework.system.dao;

import com.centit.framework.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.centit.framework.system.po.SysNotify;

@Repository
public interface SysNotifyDao extends BaseDao {
	
	//设置主键 o.setNotifyId(DatabaseOptUtils.getNextLongSequence(this, "S_MSGCODE"));
    SysNotify mergeObject(SysNotify o);
}
