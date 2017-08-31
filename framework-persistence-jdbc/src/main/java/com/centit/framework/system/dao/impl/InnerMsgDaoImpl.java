package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.po.InnerMsg;
import org.springframework.stereotype.Repository;

@Repository("innerMsgDao")
public class InnerMsgDaoImpl extends BaseDaoImpl<InnerMsg, String> implements InnerMsgDao {

    @Override
    public void saveObject(InnerMsg innerMsg) {
        super.saveNewObject(innerMsg);
    }
}
