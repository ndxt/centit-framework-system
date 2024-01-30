package com.centit.framework.users.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author zfg
 */
@ApiModel(value = "同步系统用户至钉钉用户", description = "同步系统用户至钉钉用户")
public class DingUserDTO {

    @ApiModelProperty(value = "用户姓名", name = "userName", required = true)
    @Length(max = 300)
    private String userName;

    @ApiModelProperty(value = "手机号码", name = "regCellPhone", required = true)
    @Length(max = 15)
    private String regCellPhone;

    @ApiModelProperty(value = "默认部门", name = "primaryUnit", required = true)
    @Length(max = 32)
    private String primaryUnit;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegCellPhone() {
        return regCellPhone;
    }

    public void setRegCellPhone(String regCellPhone) {
        this.regCellPhone = regCellPhone;
    }

    public String getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(String primaryUnit) {
        this.primaryUnit = primaryUnit;
    }
}
