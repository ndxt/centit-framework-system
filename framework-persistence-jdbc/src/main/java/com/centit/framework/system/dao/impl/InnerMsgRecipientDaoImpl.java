package com.centit.framework.system.dao.impl;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.system.dao.InnerMsgRecipientDao;
import com.centit.framework.system.po.InnerMsgRecipient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("innerMsgRecipientDao")
public class InnerMsgRecipientDaoImpl extends BaseDaoImpl<InnerMsgRecipient, String>
        implements InnerMsgRecipientDao {


    /*
     * 两人间来往消息列表
     * 
     */
    @Transactional
    public  List<InnerMsgRecipient> getExchangeMsgs(String sender, String receiver) {
        
        String queryString ="From InnerMsgRecipient where( (msgCode in (Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive= ?) " +
        		"or (msgCode in(Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive=? )) order by msgCode desc";
        List<InnerMsgRecipient> l = listObjects(queryString, new Object[]{sender,receiver,receiver,sender});
        return l;
    }
   
    public long getUnreadMessageCount(String userCode){
        Object obj= DatabaseOptUtils.getSingleObjectByHql(this, "select count(1)"
                + " from InnerMsgRecipient re Where re.receive = ? and msgState ='U'",userCode);
        if (obj == null)
            return 0;
        if (obj instanceof Long)
            return ((Long) obj).longValue();
        if (obj instanceof String)
            return Long.valueOf(obj.toString()).longValue();
        if (obj instanceof BigDecimal)
            return ((BigDecimal) obj).longValue();
        return 0;
    }
    
    public List<InnerMsgRecipient> listUnreadMessage(String userCode){
        String queryString ="From InnerMsgRecipient re Where re.receive = ? and re.msgState ='U' "
                + "  order by re.id desc";
        return listObjects(queryString, userCode);
    }
}
 