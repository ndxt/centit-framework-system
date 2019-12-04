package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统操作日志
 */
@Entity
@Table(name = "F_OPT_LOG")
@ApiModel(value="系统操作日志对象",description="系统操作日志对象 OptLog")
public class OptLog implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LOG_ID")
    //@GeneratedValue(generator = "assignedGenerator")
    private Long logId;

    /**
     * 日志级别
     * 使用常量LEVEL_INFO和LEVEL_ERROR表示
     * 默认级别为LEVEL_INFO
     */
    @Column(name = "LOG_LEVEL")
    @NotBlank(message = "字段不能为空")
    @Length(max = 2, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "日志级别 使用常量LEVEL_INFO和LEVEL_ERROR表示 默认级别为LEVEL_INFO",name = "logLevel")
    @ValueGenerator(strategy = GeneratorType.CONSTANT, occasion = GeneratorTime.NEW, value = OperationLog.LEVEL_INFO)
    private String logLevel;


    @Column(name = "USER_CODE")
    @NotBlank(message = "字段不能为空")
    @Length(max = 8, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="userName",value="userCode")
    @ApiModelProperty(value = "用户代码",name = "userCode",required = true)
    private String userCode;

    @OrderBy("desc")
    @Column(name = "OPT_TIME")
    @NotNull(message = "字段不能为空")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optTime;

    /**
     * 业务操作ID，如记录的是用户管理模块，optId=F_OPT_INFO表中操作用户管理模块业务的主键
     */
    @Column(name = "OPT_ID")
    @ValueGenerator(strategy = GeneratorType.CONSTANT, occasion = GeneratorTime.NEW, value = "system")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @DictionaryMap(fieldName="optName",value="optId")
    private String optId;

    /**
     * 操作业务标记
     * 一般用于关联到业务主体
     */
    @Column(name = "OPT_TAG")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String optTag;

    /**
     * 操作方法
     * 方法，或者字段
     * 方法使用 P_OPT_LOG_METHOD... 常量表示
     */
    @Column(name = "OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String optMethod;

    /**
     * 操作内容描述
     */
    @Column(name = "OPT_CONTENT")
    @NotBlank(message = "字段不能为空")
    private String optContent;

    /**
     * 新值
     */
    @Column(name = "NEW_VALUE")
    private String newValue;
    /**
     * 原值
     */
    @Column(name = "OLD_VALUE")
    private String oldValue;

    public String getOptTag() {
        return optTag;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setOptTag(String optTag) {
        this.optTag = optTag;
    }


    // Constructors

    /**
     * default constructor
     */
    public OptLog() {
    }

    public OptLog(String userCode, String optId, String optTag, String optmethod, String oldvalue, String optcontent) {
        this.userCode = userCode;
        // this.userCode = ((FUserDetail)
        // (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsercode();

        this.optId = optId;
        this.optTag = optTag;
        this.optMethod = optmethod;
        this.optContent = optcontent;
        this.oldValue = oldvalue;
    }

    public OptLog(Long logid, String loglevel, String userCode, Date opttime, String optid, String optmethod,
                  String optcontent, String oldvalue) {

        this.logId = logid;

        this.logLevel = loglevel;
        this.userCode = userCode;
        this.optTime = opttime;
        this.optId = optid;
        this.optMethod = optmethod;
        this.optContent = optcontent;
        this.oldValue = oldvalue;
    }

    public Long getLogId() {
        return this.logId;
    }

    public void setLogId(Long logid) {
        this.logId = logid;
    }

    // Property accessors

    public String getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(String loglevel) {
        this.logLevel = loglevel;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Date getOptTime() {
        return this.optTime;
    }

    public void setOptTime(Date opttime) {
        this.optTime = opttime;
    }

    public String getOptId() {
        return this.optId;
    }

    public void setOptId(String optid) {
        this.optId = optid;
    }

    public String getOptMethod() {
        return this.optMethod;
    }

    public void setOptMethod(String optmethod) {
        this.optMethod = optmethod;
    }

    public String getOptContent() {
        return this.optContent;
    }

    public void setOptContent(String optcontent) {
        this.optContent = optcontent;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public void setOldValue(String oldvalue) {
        this.oldValue = oldvalue;
    }

    public void copy(OperationLog other) {
        this.logLevel = other.getLogLevel();
        this.userCode = other.getUserCode();
        this.optTime = other.getOptTime();
        this.optId = other.getOptId();
        this.optTag = other.getOptTag();
        this.optMethod = other.getOptMethod();
        this.optContent = other.getOptContent();
        this.newValue = other.getNewValue();
        this.oldValue = other.getOldValue();
    }

    public String getOptMethodText() {
        if (OperationLog.P_OPT_LOG_METHOD_C.equals(getOptMethod())) {
            return "新增";
        }
        if (OperationLog.P_OPT_LOG_METHOD_D.equals(getOptMethod())) {
            return "删除";
        }
        if (OperationLog.P_OPT_LOG_METHOD_U.equals(getOptMethod())) {
            return "更新";
        }

        return this.optMethod;
    }
}
