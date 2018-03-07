package com.centit.framework.system.dao;

import com.centit.framework.system.po.InnerMsgRecipient;

import java.util.List;
import java.util.Map;

/**
 * 消息接收Dao
 * @author god
 * updated by zou_wy@centit.com
 */
public interface InnerMsgRecipientDao {
    /**
     * 更新
     *
     * @param innerMsgRecipient InnerMsgRecipient
     */
    void updateInnerMsgRecipient(InnerMsgRecipient innerMsgRecipient);

    /**
     * 删除
     *
     * @param innerMsgRecipient InnerMsgRecipient
     */
    void deleteObject(InnerMsgRecipient innerMsgRecipient);

    /**
     * 根据条件查询
     *
     * @param filterMap 过滤条件
     * @return List&lt; InnerMsgRecipient &gt;
     */
    List<InnerMsgRecipient> listObjects(Map<String, Object> filterMap);

    /**
     * 查询条数 用户分页
     *
     * @param filterDescMap 过滤条件
     * @return int
     */
    int pageCount(Map<String, Object> filterDescMap);

    /**
     * 分页查询
     *
     * @param pageQueryMap 过滤条件
     * @return List&lt; InnerMsgRecipient &gt;
     */
    List<InnerMsgRecipient> pageQuery(Map<String, Object> pageQueryMap);

    /**
     * 根据Id查询
     *
     * @param id 主键
     * @return InnerMsgRecipient
     */
    InnerMsgRecipient getObjectById(String id);

    /**
     * 新建
     *
     * @param recipient InnerMsgRecipient
     */
    void saveNewObject(InnerMsgRecipient recipient);

    /**
     * 获取下一个序列值
     * @return 序列号 作为主键
     */
    String getNextKey();

    /**
     * 两人间来往消息列表
     *
     * @param sender   发送方
     * @param receiver 接收方
     * @return List InnerMsgRecipient
     */
    List<InnerMsgRecipient> getExchangeMsgs(String sender, String receiver);

    /**
     * 查询用户消息数量
     *
     * @param userCode userCode
     * @return long
     */
    long getUnreadMessageCount(String userCode);

    /**
     * 查询用户消息列表
     *
     * @param userCode userCode
     * @return List InnerMsgRecipient
     */
    List<InnerMsgRecipient> listUnreadMessage(String userCode);

    /*
     * 级联查询
     *
     * @param filterMap 过滤条件
     * @return List&lt; InnerMsgRecipient &gt;
     */
    //List<InnerMsgRecipient> listObjectsCascade(Map<String, Object> filterMap);
}
