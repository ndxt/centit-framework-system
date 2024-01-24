package com.centit.framework.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 租户列表分页查询的请求参数
 */
@ApiModel(value = "租户列表分页查询实体类", description = "租户列表分页查询实体类")
public class PageListTenantInfoQo {

    @Length(max = 1)
    @ApiModelProperty(value = "租户名称", name = "unitName")
    private String unitName;

    @ApiModelProperty(value = "审核状态，1：已审核，2：未审核", name = "checkState")
    private String checkState;

    @ApiModelProperty(value = "审核开始时间，示例：yyyy-MM-dd HH:mm:ss", name = "startApplyTime")
    private Date startApplyTime;

    @ApiModelProperty(value = "审核结束时间，示例：yyyy-MM-dd HH:mm:ss", name = "endApplyTime")
    private Date endApplyTime;

    @ApiModelProperty(value = "使用到期时间开始范围，示例：yyyy-MM-dd HH:mm:ss", name = "startUseLimittime")
    private Date startUseLimittime;

    @ApiModelProperty(value = "使用到期时间结束范围，示例：yyyy-MM-dd HH:mm:ss", name = "endUseLimittime")
    private Date endUseLimittime;

    @ApiModelProperty(value = "审核通过时间开始范围，示例：yyyy-MM-dd HH:mm:ss", name = "startPassTime")
    private Date startPassTime;

    @ApiModelProperty(value = "审核通过时间结束范围，示例：yyyy-MM-dd HH:mm:ss", name = "endPassTime")
    private Date endPassTime;

    @ApiModelProperty(value = "是否可以；示例：T：可用，F：不可用", name = "isAvailable")
    private String isAvailable;

    @ApiModelProperty(value = "租户所有者;", name = "ownUser")
    private String ownUser;


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public Date getStartApplyTime() {
        return startApplyTime;
    }

    public void setStartApplyTime(Date startApplyTime) {
        this.startApplyTime = startApplyTime;
    }

    public Date getEndApplyTime() {
        return endApplyTime;
    }

    public void setEndApplyTime(Date endApplyTime) {
        this.endApplyTime = endApplyTime;
    }

    public Date getStartUseLimittime() {
        return startUseLimittime;
    }

    public void setStartUseLimittime(Date startUseLimittime) {
        this.startUseLimittime = startUseLimittime;
    }

    public Date getEndUseLimittime() {
        return endUseLimittime;
    }

    public void setEndUseLimittime(Date endUseLimittime) {
        this.endUseLimittime = endUseLimittime;
    }

    public Date getStartPassTime() {
        return startPassTime;
    }

    public void setStartPassTime(Date startPassTime) {
        this.startPassTime = startPassTime;
    }

    public Date getEndPassTime() {
        return endPassTime;
    }

    public void setEndPassTime(Date endPassTime) {
        this.endPassTime = endPassTime;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getOwnUser() {
        return ownUser;
    }

    public void setOwnUser(String ownUser) {
        this.ownUser = ownUser;
    }

    @Override
    public String toString() {
        return "PageListTenantInfoQo{" +
            "unitName='" + unitName + '\'' +
            ", checkState='" + checkState + '\'' +
            ", startApplyTime=" + startApplyTime +
            ", endApplyTime=" + endApplyTime +
            ", startUseLimittime=" + startUseLimittime +
            ", endUseLimittime=" + endUseLimittime +
            ", startPassTime=" + startPassTime +
            ", endPassTime=" + endPassTime +
            ", isAvailable='" + isAvailable + '\'' +
            ", ownUser='" + ownUser + '\'' +
            '}';
    }
}
