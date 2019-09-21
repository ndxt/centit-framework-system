package com.centit.framework.system.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.NoticeMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "M_INNERMSG")
@ApiModel(value="消息对象",description="消息对象InnerMsg")
public class InnerMsg implements Serializable{

    /**
     * 内部消息管理，这些消息会在页面上主动弹出
     */
    private static final long serialVersionUID = 1L;
    /**
     * 消息编号
     */
    @Id
    @Column(name="MSG_CODE")
    @ApiModelProperty(value = "消息编号",name = "msgCode",required = true)
    //@GeneratedValue(generator = "assignedGenerator")
//    @ValueGenerator(strategy = GeneratorType.SEQUENCE, value = "S_MSGCODE")
    private String msgCode;

    /**
     * 发送人
     */
    @Column(name="SENDER")
    @NotBlank
    @Length(max = 128, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="senderName",value="userCode")
    @ApiModelProperty(value = "发送人",name = "sender",required = true)
    private String sender;

    /**
     * 发送时间
     */
    @OrderBy("desc")
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    /**
     * 标题
     */
    @Column(name="MSG_TITLE")
    @Length(max = 128, message = "字段长度不能大于{max}")
    private String msgTitle;

    /**
     * 消息类别：P=个人为消息   A=机构为公告  M=消息
     */
    @Column(name = "MSG_TYPE")
    @Length(max = 16, message = "字段长度必须为{max}")
    private String msgType;

    /**
     *  消息类别：I=收件箱 O=发件箱 D=草稿箱 T=废件箱
     */
    @Column(name = "MAIL_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String mailType;


    /**
     *  邮箱删除前状：I=收件箱 O=发件箱 D=草稿箱 T=废件箱
     */
    @Column(name = "MAIL_UNDEL_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String mailUnDelType;

    /**
     * 接收人中文名
     */
    @Column(name="RECEIVE_NAME")
    @Length(max = 2048, message = "字段长度不能大于{max}")
    private String receiveName;

    /**
         总数为发送人和接收人数量相加，发送和接收人删除消息时-1，当数量为0时真正删除此条记录
         消息类型为消息时不需要设置
     */
    @Column(name = "HOLD_USERS")
    private Long holdUsers;

    /**
             消息状态：未读/已读/删除
    */
    @Column(name = "MSG_STATE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String msgState;

    /**
     * 消息正文
     */
    @Column(name="MSG_CONTENT")
    @NotBlank(message = "字段不能为空")
    private String msgContent;

    /**
    *用户配置多邮箱时使用*/
    @Column(name="EMAIL_ID")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String emailId;

    /**
     *功能模块 */
    @Column(name="OPT_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optId;

    /**
     *操作方法 */
    @Column(name="OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optMethod;

    /**
     *操作业务标记
     * */
    @Column(name="OPT_TAG")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String optTag;

    /**
     *一个消息可以有多个收件人
     */

    @Transient
    @JSONField(serialize=false)
    private List<InnerMsgRecipient> recipients;

    public InnerMsg(){

    }

    public InnerMsg(String sender,String msgTitle,String msgContent){
        this.sender=sender;
        this.msgTitle=msgTitle;
        this.msgContent=msgContent;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getMailUnDelType() {
        return mailUnDelType;
    }

    public void setMailUnDelType(String mailUnDelType) {
        this.mailUnDelType = mailUnDelType;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Long getHoldUsers() {
        return holdUsers;
    }

    public void setHoldUsers(Long holdUsers) {
        this.holdUsers = holdUsers;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<InnerMsgRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<InnerMsgRecipient> recipients) {
        this.recipients = recipients;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getOptMethod() {
        return optMethod;
    }

    public void setOptMethod(String optMethod) {
        this.optMethod = optMethod;
    }


    public String getOptTag() {
        return optTag;
    }

    public void setOptTag(String optTag) {
        this.optTag = optTag;
    }

    public InnerMsg copy(InnerMsg other){
        this.emailId = other.getEmailId();
        this.holdUsers = other.getHoldUsers();
        this.mailType = other.getMailType();
        this.mailUnDelType = other.getMailUnDelType();
        this.msgCode = other.getMsgCode();
        this.msgContent = other.getMsgContent();
        this.msgState = other.getMsgState();
        this.msgTitle = other.getMsgTitle();
        this.msgType = other.getMsgType();
        this.optId = other.getOptId();
        this.optMethod = other.getOptMethod();
        this.optTag = other.getOptTag();
        this.receiveName = other.getReceiveName();
        this.sendDate = other.getSendDate();
        this.sender = other.getSender();
        this.recipients = other.getRecipients();
        return this;
    }

    public InnerMsg copyNotNullProperties(InnerMsg other){
        if(other.getEmailId() != null) {
            this.emailId = other.getEmailId();
        }
        if(other.getHoldUsers() != null) {
            this.holdUsers = other.getHoldUsers();
        }
        if(other.getMailType() != null) {
            this.mailType = other.getMailType();
        }
        if(other.getMailUnDelType() != null) {
            this.mailUnDelType = other.getMailUnDelType();
        }
        if(other.getMsgCode() != null) {
            this.msgCode = other.getMsgCode();
        }
        if(other.getMsgContent() != null) {
            this.msgContent = other.getMsgContent();
        }
        if(other.getMsgState() != null) {
            this.msgState = other.getMsgState();
        }
        if(other.getMsgTitle() != null) {
            this.msgTitle = other.getMsgTitle();
        }
        if(other.getMsgType() != null) {
            this.msgType = other.getMsgType();
        }
        if(other.getOptId() != null) {
            this.optId = other.getOptId();
        }
        if(other.getOptMethod() != null) {
            this.optMethod = other.getOptMethod();
        }
        if(other.getOptTag() != null) {
            this.optTag = other.getOptTag();
        }
        if(other.getReceiveName() != null) {
            this.receiveName = other.getReceiveName();
        }
        if(other.getSendDate() != null) {
            this.sendDate = other.getSendDate();
        }
        if(other.getSender() != null) {
            this.sender = other.getSender();
        }
        if(other.getRecipients() != null) {
            this.recipients = other.getRecipients();
        }
        return this;
    }

    public InnerMsg copyFromNoticeMessage(NoticeMessage other){
        this.msgContent = other.getMsgContent();
        this.msgTitle = other.getMsgSubject();
        this.msgType = other.getMsgType();
        this.optId = other.getOptId();
        this.optMethod = other.getOptMethod();
        this.optTag = other.getOptTag();
        return this;
    }
}
