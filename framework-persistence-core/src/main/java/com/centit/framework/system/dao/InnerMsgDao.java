package com.centit.framework.system.dao;

import com.centit.framework.system.po.InnerMsg;

import java.util.List;
import java.util.Map;

/**
 * 内部消息Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface InnerMsgDao {

    /**
    * 根据Id查询
    * @param msgCode 消息编号
    * @return InnerMsg 消息对象
    */
    InnerMsg getObjectById(String msgCode);

    /**
    * 更新
    * @param innerMsg 消息对象
    */
    void updateInnerMsg(InnerMsg innerMsg);

    /**
    * 删除
    * @param innerMsg 消息对象
    */
    void deleteObject(InnerMsg innerMsg);

    /** 新建
    * @param innerMsg 消息对象
    */
    void saveNewObject(InnerMsg innerMsg);

    /**
     * 获取下一个序列值
     * @return 序列号可作为主键
     */
    String getNextKey();

    /**
    * 根据条件查询
    * @param filterMap 过滤条件
    * @return List&lt; InnerMsg &gt;
    */
    List<InnerMsg> listObjects(Map<String, Object> filterMap);

    /**
    * 查询条数 用户分页
    * @param filterDescMap 过滤条件
    * @return int
    */
    int pageCount(Map<String, Object> filterDescMap);

    /**
    * 分页查询
    * @param pageQueryMap 过滤条件
    * @return List&lt; InnerMsg &gt;
    */
    List<InnerMsg> pageQuery(Map<String, Object> pageQueryMap);

}
