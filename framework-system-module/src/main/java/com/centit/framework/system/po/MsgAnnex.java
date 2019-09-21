package com.centit.framework.system.po;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="M_MSGANNEX")
public class MsgAnnex implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JSONField(serialize=false)
    @Transient
    private InnerMsg mInnerMsg;//

    @Column(name = "INFO_CODE")
    @Length( max=16,message = "字段长度必须小于{max}")
    private String infoCode;//消息代码

    @Id
    @Column(name="MSG_ANNEX_ID")
    //@GeneratedValue(generator = "assignedGenerator")
    private String msgAnnexId;//附件主键

    public String getMsgAnnexId() {
        return msgAnnexId;
    }

    public InnerMsg getmInnerMsg() {
        return mInnerMsg;
    }

    public void setmInnerMsg(InnerMsg mInnerMsg) {
        this.mInnerMsg = mInnerMsg;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

}
