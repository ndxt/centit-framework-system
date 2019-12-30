package com.centit.framework.system.service.impl;

import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.framework.system.dao.InnerMsgDao;
import com.centit.framework.system.dao.InnerMsgRecipientDao;
import com.centit.framework.system.po.InnerMsg;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.framework.system.service.InnerMessageManager;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

@Transactional
@Service("innerMessageManager")
public class InnerMessageManagerImpl implements InnerMessageManager, MessageSender {

    @Autowired
    @NotNull
    protected InnerMsgRecipientDao innerMsgRecipientDao;

    @Autowired
    private InnerMsgDao innerMsgDao;

    /*
     * 更新接受者信息
     *
     */
    @Override
    @Transactional
    public void updateRecipient(InnerMsgRecipient recipient) {
        innerMsgRecipientDao.updateInnerMsgRecipient(recipient);

    }

    /*
     * msg为消息主题，recipient为接收人
     * 添加消息接受者,群发(receipient.receive为数组，但是保存到数据库是挨个保存)
     *
     */
    @Override
    @Transactional
    public void sendInnerMsg(InnerMsgRecipient recipient, String sysUserCode) {
        String receive = recipient.getReceive();
        String receives[] = StringUtils.split(receive, ",");
        InnerMsg msg = recipient.getMInnerMsg();
        //拆分recieve
        if (!StringUtils.isNotBlank(msg.getSender())) {
            msg.setSender(sysUserCode);
            //msg.setSenderName(CodeRepositoryUtil.getUserInfoByCode(sysUserCode).getUserName());
            if (null == msg.getSendDate()) {
                msg.setSendDate(new Date());
            }
        }
        sendToMany(receives, msg, recipient);
    }

    public void sendToMany(String[] receives, InnerMsg msg, InnerMsgRecipient recipient) {
        if (null != receives && receives.length > 0) {
            String receiveName = "";
            int bo = 0;
            for (String userCode : receives) {
                if (bo > 0) {
                    receiveName += ",";
                }
                receiveName += CodeRepositoryUtil.getUserInfoByCode(userCode).getUserName();
                bo++;
            }
            msg.setReceiveName(receiveName);
            msg.setMsgCode(innerMsgDao.getNextKey());
            innerMsgDao.saveNewObject(msg);
            recipient.setMInnerMsg(msg);
            recipient.setMsgCode(msg.getMsgCode());
            //DataPushSocketServer.pushMessage(msg.getSender(), "你发送邮件："+ msg.getMsgTitle());
            for (String userCode : receives) {
                InnerMsgRecipient innerMsgRecipient  = new InnerMsgRecipient();
                innerMsgRecipient.setId(innerMsgRecipientDao.getNextKey());
                innerMsgRecipient.copyNotNullProperties(recipient);
                innerMsgRecipient.setReceive(userCode);
                innerMsgRecipientDao.saveNewObject(innerMsgRecipient);
                //DataPushSocketServer.pushMessage(userCode, "你有新邮件：" + recipient.getMsgTitle());
            }
        }
    }


    /*
     * 获取两者间来往消息列表
     *
     */
    @Override
    @Transactional
    public List<InnerMsgRecipient> getExchangeMsgRecipients(String sender, String receiver) {
        Map<String, String> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        return innerMsgRecipientDao.getExchangeMsgs(sender, receiver);
    }

    /*
     * 给部门成员，所有直属或间接下级部门成员发消息
     */
    @Override
    @Transactional
    public void noticeByUnitCode(String unitCode, InnerMsg msg) throws ObjectException {

        List<IUnitInfo> unitList = CodeRepositoryUtil.getSubUnits(unitCode);
        //(ArrayList<UnitInfo>) unitDao.listAllSubUnits(unitCode);
        Set<IUserInfo> userList = CodeRepositoryUtil.getUnitUsers(unitCode);
        for (IUnitInfo ui : unitList) {
            userList.addAll(CodeRepositoryUtil.getUnitUsers(ui.getUnitCode()));
        }

        if (userList.size() > 0) {
            String receiveName = CodeRepositoryUtil.getUnitName(unitCode);
            msg.setReceiveName(receiveName);
            msg.setMsgType("A");
            msg.setMsgCode(innerMsgDao.getNextKey());
            innerMsgDao.saveNewObject(msg);

            for (IUserInfo ui : userList) {
                if(!Objects.equals(msg.getSender(),ui.getUserCode())){
                    InnerMsgRecipient recipient = new InnerMsgRecipient();
                    recipient.setMsgState(msg.getMsgState());
                    recipient.setMailType(msg.getMailType());
                    recipient.setMInnerMsg(msg);
                    recipient.setMsgCode(msg.getMsgCode());
                    recipient.setReceive(ui.getUserCode());
                    recipient.setId(innerMsgRecipientDao.getNextKey());
                    innerMsgRecipientDao.saveNewObject(recipient);
                    //DataPushSocketServer.pushMessage(ui.getUserCode(), "你有新邮件：" + recipient.getMsgTitle());
                }
            }
        } else {
            throw new ObjectException("该机构中暂无用户");
        }
    }


    @Override
    @Transactional
    public void deleteMsgRecipientById(String id) {
        InnerMsgRecipient re = innerMsgRecipientDao.getObjectById(id);
        re.setMsgState("D");
        innerMsgRecipientDao.updateInnerMsgRecipient(re);
    }


    @Override
    @Transactional
    public long getUnreadMessageCount(String userCode) {
        return innerMsgRecipientDao.getUnreadMessageCount(userCode);
    }

    @Override
    @Transactional
    public List<InnerMsgRecipient> listUnreadMessage(String userCode) {
        return innerMsgRecipientDao.listUnreadMessage(userCode);
    }

    /**
     * 发送消息
     */
    @Override
    @Transactional
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        InnerMsg msg = new InnerMsg();
        msg.copyFromNoticeMessage(message);
        msg.setSendDate(new Date());
        msg.setMsgType("P");
        msg.setMailType("O");
        msg.setMsgState("U");
        msg.setReceiveName(CodeRepositoryUtil.getUserInfoByCode(receiver).getUserName());
        InnerMsgRecipient recipient = new InnerMsgRecipient();
        recipient.setMInnerMsg(msg);
        recipient.setReplyMsgCode(0);
        recipient.setReceiveType("P");
        recipient.setMailType("T");
        recipient.setMsgState("U");
        String[] receives = new String[]{receiver};
        sendToMany(receives, msg, recipient);
        return ResponseData.successResponse;
    }


    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap) {
        return innerMsgRecipientDao.listObjects(filterMap);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipients(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgRecipientDao.listObjects(filterMap, pageDesc);
    }

    @Override
    public InnerMsgRecipient getMsgRecipientById(String id) {
        return innerMsgRecipientDao.getObjectById(id);
    }

    @Override
    public List<InnerMsgRecipient> listMsgRecipientsCascade(Map<String, Object> filterMap){
//       return innerMsgRecipientDao.listObjectsCascade(filterMap);
        List<InnerMsgRecipient> recipients = innerMsgRecipientDao.listObjects(filterMap);
        for(InnerMsgRecipient recipient : recipients){
            recipient.setMInnerMsg(innerMsgDao.getObjectById(recipient.getMsgCode()));
        }
        return recipients;
    }
    @Override
    public List<InnerMsgRecipient> listMsgRecipientsCascade(Map<String, Object> filterMap, PageDesc pageDesc){
        List<InnerMsgRecipient> recipients = listMsgRecipients(filterMap, pageDesc);
        for(InnerMsgRecipient recipient : recipients){
            recipient.setMInnerMsg(innerMsgDao.getObjectById(recipient.getMsgCode()));
        }
        return recipients;
    }

    @Override
    public void updateInnerMsg(InnerMsg msg) {
        innerMsgDao.updateInnerMsg(msg);
    }


    @Override
    public void deleteInnerMsgById(String msgCode) {
        InnerMsg msg= innerMsgDao.getObjectById(msgCode);
        msg.setMsgState("D");
        innerMsgDao.updateInnerMsg(msg);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap) {
        return innerMsgDao.listObjects(filterMap);
    }

    @Override
    public List<InnerMsg> listInnerMsgs(Map<String, Object> filterMap, PageDesc pageDesc) {
        return innerMsgDao.listObjects(filterMap,pageDesc);
    }

    @Override
    public InnerMsg getInnerMsgById(String msgCode) {
        return innerMsgDao.getObjectById(msgCode);
    }

}
