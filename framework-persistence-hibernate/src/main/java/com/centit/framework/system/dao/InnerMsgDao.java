package com.centit.framework.system.dao;

import com.centit.framework.system.po.InnerMsg;

import java.util.List;
import java.util.Map;

public interface InnerMsgDao {
    
     InnerMsg getObjectById(String msgCode);

    void mergeObject(InnerMsg innerMsg);

     void deleteObject(InnerMsg innerMsg);

     void saveObject(InnerMsg innerMsg);
    /** 新建
     * @param o InnerMsg
     * @return String
     */
    // String msgCode = DatabaseOptUtils.getNextKeyBySequence(this, "S_MSGCODE",16);
    void saveNewObject(InnerMsg o);
    
     List<InnerMsg> listObjects(Map<String, Object> filterMap);


    //分页  //startRow  startRow
     int  pageCount(Map<String, Object> filterDescMap);
     List<InnerMsg>  pageQuery(Map<String, Object> pageQureyMap);
    
}
