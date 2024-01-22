package com.centit.framework.system.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

public class TenantMemberApplyVo {

    @ApiModelProperty(value = "用户代码", name = "userCode")
    @NotEmpty
    @Length(max = 32)
    private String userCode;

    @ApiModelProperty(value = "用户姓名", name = "userName")
    private String userName;


    @ApiModelProperty(value = "租户代码", name = "topUnit")
    @NotEmpty
    @Length(max = 32)
    private String topUnit;


    @ApiModelProperty(value = "邀请人,提交人的userCode", name = "inviterUserCode")
    private String inviterUserCode;

    @ApiModelProperty(value = "邀请人,提交人的用户名", name = "inviterUserName")
    private String inviterUserName;


    @ApiModelProperty(value = "申请类型，1：申请 2：受邀", name = "applyType")
    private String applyType;

    @ApiModelProperty(value = "申请时间", name = "applyTime")
    private Date applyTime;

    @ApiModelProperty(value = "是否同意", name = "applyState")
    private String applyState;

    @ApiModelProperty(value = "申请备注", name = "applyRemark")
    private String applyRemark;

    @ApiModelProperty(value = "受邀备注", name = "approveRemark")
    private String approveRemark;

    @ApiModelProperty(value = "机构代码", name = "unitCode")
    private String unitCode;

    @ApiModelProperty(value = "机构名称", name = "unitName")
    private String unitName;

    @ApiModelProperty(value = "操作人身份1：租户，2：普通用户", name = "optUserType")
    @NotEmpty
    @Length(max = 1)
    private String optUserType;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getInviterUserName() {
        return inviterUserName;
    }

    public void setInviterUserName(String inviterUserName) {
        this.inviterUserName = inviterUserName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOptUserType() {
        return optUserType;
    }

    public void setOptUserType(String optUserType) {
        this.optUserType = optUserType;
    }
}
