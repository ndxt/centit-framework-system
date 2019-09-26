package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

//M_InnerMsg_Recipient
@Entity
@Table(name="M_INNERMSG_RECIPIENT")
@ApiModel(value="接受者信息",description="接受者信息对象InnerMsgRecipient")
public class InnerMsgRecipient implements Serializable{

    /**
     * 接收人主键
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID")
    //@GeneratedValue(generator = "assignedGenerator")
//    @ValueGenerator(strategy = GeneratorType.SEQUENCE, value = "S_RECIPIENT")
    @ApiModelProperty(value = "接收人主键",name = "id",required = true)
    private String id;

    /**
     * 消息编码
     */
    @Column(name="MSG_CODE")
    private String msgCode;

    /**
     * 接收人编号
     */
    @Column(name = "RECEIVE")
    @NotBlank
    @Length(max = 2048, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="receiverName",value="userCode")
    @ApiModelProperty(value = "接收人编号",name = "receive",required = true)
    private String receive;

    /**
     * 回复消息
     */
    @Column(name = "REPLY_MSG_CODE")
    private int replyMsgCode;

    /**
     *  接收人类别:
        P=个人为消息
        A=机构为公告
        M=消息
     */
    @Column(name = "RECEIVE_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String receiveType;

    /**
     *  消息类型:
        T=收件人
        C=抄送
        B=密送
     */
    @Column(name = "MAIL_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String mailType;

    /**
     * 消息状态：
     *  U=未读
        R=已读
        D=删除
     */
    @Column(name = "MSG_STATE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String msgState;


    @Transient
    private InnerMsg mInnerMsg;

    public void setMInnerMsg(InnerMsg InnerMsg){
        this.mInnerMsg=InnerMsg;
    }

    public InnerMsgRecipient(){

    }

    public String getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }


    public InnerMsg getMInnerMsg() {
        return this.mInnerMsg;
    }

    public String getSender() {
        if (null != getMInnerMsg()) {
            return getMInnerMsg().getSender();
        }
        return "";
    }

    public String getMsgTitle() {
        if (null != getMInnerMsg()) {
            return getMInnerMsg().getMsgTitle();
        }

        return "";
    }

    public Date getSendDate() {
        if (null != getMInnerMsg()) {
            return getMInnerMsg().getSendDate();
        }

        return null;
    }

    public String getMsgState() {
        return msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public int getReplyMsgCode() {
        return replyMsgCode;
    }

    public void setReplyMsgCode(int replyMsgCode) {
        this.replyMsgCode = replyMsgCode;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgContent(){
        if(null!=this.getMInnerMsg())
            return this.getMInnerMsg().getMsgContent();
        else
           return null;
    }

    public void copy(InnerMsgRecipient other){
        this.setMsgCode(other.getMsgCode());
        this.setReceive(other.getReceive());
        this.setReplyMsgCode(other.getReplyMsgCode());
        this.setReceiveType(other.getReceiveType());
        this.setMailType(other.getMailType());
        this.setMsgState(other.getMsgState());
        this.setMInnerMsg(other.getMInnerMsg());
    }

    public void copyNotNullProperties(InnerMsgRecipient other){
        if(!Objects.isNull(other.getMsgCode())) {
            this.setMsgCode(other.getMsgCode());
        }
        if(!Objects.isNull(other.getReceive())) {
            this.setReceive(other.getReceive());
        }
        //if(!Objects.isNull(other.getReplyMsgCode())) {
            this.setReplyMsgCode(other.getReplyMsgCode());
        //}
        if(!Objects.isNull(other.getReceiveType())) {
            this.setReceiveType(other.getReceiveType());
        }
        if(!Objects.isNull(other.getMailType())) {
            this.setMailType(other.getMailType());
        }
        if(!Objects.isNull(other.getMsgState())) {
            this.setMsgState(other.getMsgState());
        }
        if(!Objects.isNull(other.getMInnerMsg())) {
            this.setMInnerMsg(other.getMInnerMsg());
        }
    }


}
