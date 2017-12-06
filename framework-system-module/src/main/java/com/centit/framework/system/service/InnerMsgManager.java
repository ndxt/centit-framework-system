package com.centit.framework.system.service;

import com.centit.framework.system.po.InnerMsg;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface InnerMsgManager{
    List<InnerMsg> listObjects(Map<String, Object> filterMap);

    List<InnerMsg> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    InnerMsg getObjectById(String msgCode);

    void updateInnerMsg(InnerMsg msg);
    void deleteMsgById(String msgCode);
}
