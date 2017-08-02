package com.centit.framework.system.dao;

import com.centit.framework.system.po.InnerMsgRecipient;

import java.util.List;
import java.util.Map;

public interface InnerMsgRecipientDao {

    void mergeObject(InnerMsgRecipient optMethod);
	
	 void deleteObject(InnerMsgRecipient optMethod);
			
	 void saveObject(InnerMsgRecipient optMethod);
	
	 List<InnerMsgRecipient> listObjects(Map<String, Object> filterMap);

	
     int  pageCount(Map<String, Object> filterDescMap);
     List<InnerMsgRecipient>  pageQuery(Map<String, Object> pageQureyMap);
    
    

	 InnerMsgRecipient getObjectById(String id);
    /**
     * 新建
     * @param recipient InnerMsgRecipient
     * @return String
     */
    void saveNewObject(InnerMsgRecipient recipient);
    
    /**
     * 两人间来往消息列表
     *         String queryString ="From InnerMsgRecipient where( (msgCode in (Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive= ?) " +
        		"or (msgCode in(Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive=? )) order by msgCode desc";
        List l = listObjectsAll(queryString, new Object[]{sender,receiver,receiver,sender});
        String sender, String receiver
     * @return List InnerMsgRecipient
     */
      List<InnerMsgRecipient> getExchangeMsgs(String sender, String receiver);
   
    /**
     *  Object obj= DatabaseOptUtils.getSingleObjectByHql(this, "select count(1)"
                + " from InnerMsgRecipient re Where re.receive = ? and msgState ='U'",userCode);
     * @param userCode userCode
     * @return long
     */
     long getUnreadMessageCount(String userCode);
    /**
     * String queryString ="From InnerMsgRecipient re Where re.receive = ? and re.msgState ='U' "
                + "  order by re.id desc";
     * @param userCode userCode
     * @return List InnerMsgRecipient
     */
     List<InnerMsgRecipient> listUnreadMessage(String userCode);
}
 