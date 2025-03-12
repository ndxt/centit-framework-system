package com.centit.framework.system.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import lombok.Data;

@Data
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

}
