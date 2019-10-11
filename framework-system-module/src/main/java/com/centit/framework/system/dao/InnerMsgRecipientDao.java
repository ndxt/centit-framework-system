package com.centit.framework.system.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.system.po.InnerMsgRecipient;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("innerMsgRecipientDao")
public class InnerMsgRecipientDao extends BaseDaoImpl<InnerMsgRecipient, String> {

    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();
            filterField.put("receive", "receive = :receive");
            filterField.put("sender", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.SENDER = :sender )");
            filterField.put("(like)msgContent", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.MSG_CONTENT LIKE :msgContent )");
            filterField.put("(like)msgTitle", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.MSG_TITLE LIKE :msgTitle )");
            filterField.put("mailType", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.MAIL_TYPE = :mailType )");
            filterField.put("mailTypeNot", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.MAIL_TYPE != :mailTypeNot )");
            filterField.put("msgStateNot", "msgState != :msgStateNot");
            filterField.put("innerMsgStateNot", "msgCode in (select im.MSG_CODE from M_INNERMSG im where im.MSG_STATE != :innerMsgStateNot )");
            filterField.put("isRecycled", CodeBook.EQUAL_HQL_ID);
            filterField.put("MSGSTATE", CodeBook.EQUAL_HQL_ID);
            filterField.put("msgType", "msgCode in ( select im.MSG_CODE from M_INNERMSG im where im.MSG_TYPE = :msgType )");
        }
        return filterField;
    }

    @Override
    public List<InnerMsgRecipient> listObjects(Map<String, Object> filterMap) {
        return super.listObjectsByProperties(filterMap);
    }

    public InnerMsgRecipient getObjectById(String id) {
        return super.getObjectById(id);
    }

    /*
         * 两人间来往消息列表
         *
         */
    @Transactional
    public  List<InnerMsgRecipient> getExchangeMsgs(String sender, String receiver) {

        String queryString ="where( (MSG_CODE in (Select im.MSG_CODE from M_INNERMSG im where im.SENDER= ? " +
                " and (im.MAIL_TYPE='I' or im.MAIL_TYPE='O')) and RECEIVE= ?) " +
                "or (MSG_CODE in(Select  im.MSG_CODE from M_INNERMSG im where im.sender= ? " +
                " and (im.MAIL_TYPE='I' or im.MAIL_TYPE='O')) and RECEIVE= ? )) order by msgCode desc";
        List<InnerMsgRecipient> l = listObjectsByFilter(queryString,
                new Object[]{sender,receiver,receiver,sender});
        return l;
    }

    public long getUnreadMessageCount(String userCode){

        Object obj = DatabaseOptUtils.getScalarObjectQuery(this, "select count(1)"
                + " Where receive = ? and msgState ='U'",
                new Object[]{userCode});
        Long l = NumberBaseOpt.castObjectToLong(obj);
        return l==null?0L:l;
    }

    public List<InnerMsgRecipient> listUnreadMessage(String userCode){
        return listObjectsByProperties(CollectionsOpt.createHashMap(
                "receive", userCode,"msgState","U"));
    }

    public void updateInnerMsgRecipient(InnerMsgRecipient innerMsgRecipient){
        super.updateObject(innerMsgRecipient);
    }

    /*@Override
    public List<InnerMsgRecipient> listObjectsCascade(Map<String, Object> filterMap){
        List<InnerMsgRecipient> recipients = listObjectsAll(filterMap);
        for(InnerMsgRecipient recipient : recipients){
            DatabaseOptUtils.getObjectCascadeById(inn)
        }
    }*/

    @Transactional
    public String getNextKey() {
        return StringBaseOpt.objectToString(
            DatabaseOptUtils.getSequenceNextValue(this, "S_RECIPIENT"));
    }

}
