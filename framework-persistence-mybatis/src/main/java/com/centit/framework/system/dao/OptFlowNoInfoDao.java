package com.centit.framework.system.dao;

import com.centit.framework.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.centit.framework.system.po.OptFlowNoInfo;
import com.centit.framework.system.po.OptFlowNoInfoId;

@Repository
public interface OptFlowNoInfoDao extends BaseDao {
	
	OptFlowNoInfo getObjectById(OptFlowNoInfoId cid);
	
	void deleteObjectById(OptFlowNoInfoId cid);
	
	void saveObject(OptFlowNoInfo optMethod);
}
