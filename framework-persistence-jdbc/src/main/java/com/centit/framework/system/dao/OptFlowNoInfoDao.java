package com.centit.framework.system.dao;

import com.centit.framework.system.po.OptFlowNoInfo;
import com.centit.framework.system.po.OptFlowNoInfoId;

public interface OptFlowNoInfoDao {
	
	OptFlowNoInfo getObjectById(OptFlowNoInfoId cid);
	
	void deleteObjectById(OptFlowNoInfoId cid);
	
	void saveObject(OptFlowNoInfo optFlowNoINfo);
}
