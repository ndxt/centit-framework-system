package com.centit.framework.system.service.impl;

import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.dao.InnerMsgRecipientDao;
import com.centit.framework.system.po.InnerMsg;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.framework.system.service.InnerMsgManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("innerMessageManager")
@Transactional
public class InnerMsgManagerImpl implements InnerMsgManager, MessageSender{

    @Resource
    @NotNull
    protected InnerMsgDao innerMsgDao;

    @Resource(name="innerMsgRecipientDao")
    @NotNull
    private InnerMsgRecipientDao recipientDao;

    /*
     * 更新消息
     *
     */
    @Override
    public void updateInnerMsg(InnerMsg msg) {
       innerMsgDao.updateInnerMsg(msg);
    }
    /**
     * 发送消息
     *
     */
    @Override
    public String sendMessage(String sender, String receiver,
            String msgSubject, String msgContent, String optId,
            String optMethod, String optTag) {
        InnerMsg msg = new InnerMsg(sender,msgSubject,msgContent);
        msg.setSendDate(new Date());
        msg.setMsgType("P");
        msg.setMailType("O");
        msg.setMsgState("U");
        msg.setOptId(optId);
        msg.setOptMethod(optMethod);
        msg.setOptTag(optTag);
        InnerMsgRecipient recipient=new InnerMsgRecipient();
        recipient.setMInnerMsg(msg);
        recipient.setReplyMsgCode(0);
        recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        recipientDao.saveNewObject(recipient);
            return "OK";
    }

    @Override
    public String sendMessage(String sender, String receiver,
        String msgSubject, String msgContent) {

        return sendMessage( sender,  receiver,
                 msgSubject,  msgContent,  "msg",
                 "sender", "");
    }

    @Override
    public void deleteMsgById(String msgCode) {
       InnerMsg msg= innerMsgDao.getObjectById(msgCode);
       msg.setMsgState("D");
       innerMsgDao.updateInnerMsg(msg);
    }

    @Override
    public List<InnerMsg> listObjects(Map<String, Object> filterMap) {
        return innerMsgDao.listObjects(filterMap);
    }

    @Override
    public List<InnerMsg> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgDao.pageQuery(QueryParameterPrepare.prepPageParams(filterMap,pageDesc,innerMsgDao.pageCount(filterMap)));
    }

    @Override
    public InnerMsg getObjectById(String msgCode) {
        return innerMsgDao.getObjectById(msgCode);
    }

}
