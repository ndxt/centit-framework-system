package com.centit.framework.system.dao.mybatisimpl;

import com.centit.framework.system.po.InnerMsgRecipient;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InnerMsgRecipientDao
  extends com.centit.framework.system.dao.InnerMsgRecipientDao {

      /**
       * 两人间来往消息列表
       *         String queryString ="From InnerMsgRecipient where( (msgCode in (Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive= ?) " +
                  "or (msgCode in(Select msgCode from InnerMsg where sender= ? and (mailType='I' or mailType='O')) and Receive=? )) order by msgCode desc";
          List l = listObjectsAll(queryString, new Object[]{sender,receiver,receiver,sender});
          String sender, String receiver
       * @return List InnerMsgRecipient
       * @param receiver 接收方
       * @param sender 发送方
       */
      @Override
      List<InnerMsgRecipient> getExchangeMsgs(@Param("sender") String sender, @Param("receiver") String receiver);


}
