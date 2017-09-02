package com.centit.framework.system.dao.impl;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.po.InnerMsg;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("innerMsgDao")
public class InnerMsgDaoImpl extends BaseDaoImpl<InnerMsg, String> implements InnerMsgDao {

    @Override
    public InnerMsg getObjectById(String msgCode) {
        return super.getObjectById(msgCode);
    }

    @Override
    public void saveObject(InnerMsg innerMsg) {
        super.saveNewObject(innerMsg);
    }

    @Override
    public List<InnerMsg> listObjects(Map<String, Object> filterMap) {
        return super.listObjectsByProperties(filterMap);
    }

    /**
     * 每个dao都需要重载这个函数已获得自定义的查询条件，否则listObjects、pageQuery就等价与listObjectsByProperties
     *
     * @return FilterQuery
     */
    @Override
    public String getDaoEmbeddedFilter() {
        return "[:msgType | and MSG_TYPE = :msgType ]" +
                "[:(like)msgTitle | and MSG_TITLE like :msgTitle ]" +
                "[:(like)msgContent | and MSG_CONTENT like :msgContent ]" +
                "[:msgStateNot | and MSG_STATE != msgStateNot ]" +
                "[:sender | and SENDER = :sender ] " +
                "[:receive | and MSG_CODE in ( select re.MSG_CODE from M_INNERMSG_RECIPIENT re Where re.RECEIVE = :receive )] ";
    }
}