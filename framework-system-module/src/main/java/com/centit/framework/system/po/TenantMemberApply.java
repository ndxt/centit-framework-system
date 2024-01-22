package com.centit.framework.system.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_tenant_member_apply")
@ApiModel(value = "成员申请表", description = "用户申请加入租户或者租户管理员邀请用户记录")
public class TenantMemberApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_code")
    @Length(max = 32)
    @ApiModelProperty(value = "用户代码", name = "userCode")
    @NotEmpty
    private String userCode;

    @Id
    @Column(name = "top_unit")
    @Length(max = 32)
    @ApiModelProperty(value = "租户代码", name = "topUnit")
    @NotEmpty
    private String topUnit;

    @Column(name = "inviter_user_code")
    @Length(max = 64)
    @ApiModelProperty(value = "邀请人,提交人的userCode", name = "inviterUserCode")
    @NotEmpty
    private String inviterUserCode;

    @Column(name = "apply_type")
    @Length(max = 1)
    @ApiModelProperty(value = "申请类型，1：申请 2：受邀", name = "applyType")
    @NotEmpty
    private String applyType;

    @Column(name = "apply_time")
    @ApiModelProperty(value = "申请时间", name = "applyTime")
    private Date applyTime;

    @Column(name = "apply_state")
    @Length(max = 1)
    @ApiModelProperty(value = "1.申请2重新申请3同意4不同意", name = "applyState")
    private String applyState;

    @Column(name = "apply_remark")
    @Length(max = 500)
    @ApiModelProperty(value = "申请备注", name = "applyRemark")
    private String applyRemark;

    @Column(name = "approve_remark")
    @Length(max = 500)
    @ApiModelProperty(value = "受邀备注", name = "approveRemark")
    private String approveRemark;

    @Column(name = "unit_code")
    @Length(max = 32)
    @ApiModelProperty(value = "机构代码", name = "unitCode")
    private String unitCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTopUnit() {
        return topUnit;
    }

    public void setTopUnit(String topUnit) {
        this.topUnit = topUnit;
    }

    public String getInviterUserCode() {
        return inviterUserCode;
    }

    public void setInviterUserCode(String inviterUserCode) {
        this.inviterUserCode = inviterUserCode;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getApproveRemark() {
        return approveRemark;
    }

    public void setApproveRemark(String approveRemark) {
        this.approveRemark = approveRemark;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    @Override
    public String toString() {
        return "TenantMemberApply{" +
            "userCode='" + userCode + '\'' +
            ", topUnit='" + topUnit + '\'' +
            ", inviterUserCode='" + inviterUserCode + '\'' +
            ", applyType='" + applyType + '\'' +
            ", applyTime=" + applyTime +
            ", applyState='" + applyState + '\'' +
            ", applyRemark='" + applyRemark + '\'' +
            ", approveRemark='" + approveRemark + '\'' +
            ", unitCode='" + unitCode + '\'' +
            '}';
    }
}
