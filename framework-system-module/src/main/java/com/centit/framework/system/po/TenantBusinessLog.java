package com.centit.framework.system.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "租户转让记录", description = "租户转让记录")
@Table(name = "t_tenant_business_log")
public class TenantBusinessLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BUSINESS_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    @Length(max = 32)
    @ApiModelProperty(value = "主键id", name = "businessId")
    private String businessId;

    @Column(name = "TOP_UNIT")
    @Length(max = 32)
    @ApiModelProperty(value = "租户代码", name = "topUnit")
    @NotEmpty
    private String topUnit;

    @Column(name = "ASSIGNOR_USER_CODE")
    @Length(max = 64)
    @ApiModelProperty(value = "转让方userCode", name = "assignorUserCode")
    @NotEmpty
    private String assignorUserCode;

    @Column(name = "ASSIGNOR_USER_NAME")
    @Length(max = 64)
    @ApiModelProperty(value = "转让方姓名", name = "assignorUserName")
    private String assignorUserName;

    @Column(name = "ASSIGNEE_USER_CODE")
    @Length(max = 64)
    @ApiModelProperty(value = "受让方userCode", name = "assigneeUserCode")
    @NotEmpty
    private String assigneeUserCode;

    @Column(name = "ASSIGNEE_USER_NAME")
    @Length(max = 64)
    @ApiModelProperty(value = "受让方姓名", name = "assigneeUserName")
    private String assigneeUserName;

    @Column(name = "BUSINESS_REASON")
    @Length(max = 500)
    @ApiModelProperty(value = "转让原因", name = "businessReason")
    private String businessReason;

    @Column(name = "BUSINESS_REMARK")
    @Length(max = 500)
    @ApiModelProperty(value = "转让备注", name = "businessRemark")
    private String businessRemark;

    @Column(name = "APPLY_BUSINESS_TIME")
    @ApiModelProperty(value = "申请转让时间", name = "applyBusinessTime")
    private Date applyBusinessTime;

    @Column(name = "SUCCESS_BUSINESS_TIME")
    @ApiModelProperty(value = "转让成功时间", name = "successBusinessTime")
    private Date successBusinessTime;

    @Column(name = "BUSINESS_STATE")
    @ApiModelProperty(value = "转让成功状态", name = "businessState")
    @Length(max = 1)
    private String businessState;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTopUnit() {
        return topUnit;
    }

    public void setTopUnit(String topUnit) {
        this.topUnit = topUnit;
    }

    public String getAssignorUserCode() {
        return assignorUserCode;
    }

    public void setAssignorUserCode(String assignorUserCode) {
        this.assignorUserCode = assignorUserCode;
    }

    public String getAssignorUserName() {
        return assignorUserName;
    }

    public void setAssignorUserName(String assignorUserName) {
        this.assignorUserName = assignorUserName;
    }

    public String getAssigneeUserCode() {
        return assigneeUserCode;
    }

    public void setAssigneeUserCode(String assigneeUserCode) {
        this.assigneeUserCode = assigneeUserCode;
    }

    public String getAssigneeUserName() {
        return assigneeUserName;
    }

    public void setAssigneeUserName(String assigneeUserName) {
        this.assigneeUserName = assigneeUserName;
    }

    public String getBusinessReason() {
        return businessReason;
    }

    public void setBusinessReason(String businessReason) {
        this.businessReason = businessReason;
    }

    public String getBusinessRemark() {
        return businessRemark;
    }

    public void setBusinessRemark(String businessRemark) {
        this.businessRemark = businessRemark;
    }

    public Date getApplyBusinessTime() {
        return applyBusinessTime;
    }

    public void setApplyBusinessTime(Date applyBusinessTime) {
        this.applyBusinessTime = applyBusinessTime;
    }

    public Date getSuccessBusinessTime() {
        return successBusinessTime;
    }

    public void setSuccessBusinessTime(Date successBusinessTime) {
        this.successBusinessTime = successBusinessTime;
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    @Override
    public String toString() {
        return "TenantBusinessLog{" +
            "businessId='" + businessId + '\'' +
            ", topUnit='" + topUnit + '\'' +
            ", assignorUserCode='" + assignorUserCode + '\'' +
            ", assignorUserName='" + assignorUserName + '\'' +
            ", assigneeUserCode='" + assigneeUserCode + '\'' +
            ", assigneeUserName='" + assigneeUserName + '\'' +
            ", businessReason='" + businessReason + '\'' +
            ", businessRemark='" + businessRemark + '\'' +
            ", applyBusinessTime=" + applyBusinessTime +
            ", successBusinessTime=" + successBusinessTime +
            ", businessState='" + businessState + '\'' +
            '}';
    }
}
