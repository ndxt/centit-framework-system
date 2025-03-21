package com.centit.framework.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "租户成员查询实体类", description = "租户成员查询实体类")
@Data
public class TenantMemberQo {

    @ApiModelProperty(value = "租户代码", name = "topUnit",required = true)
    @NotEmpty
    @Length(max = 64)
    private String topUnit;

    @ApiModelProperty(value = "用户姓名", name = "userName")
    private String userName;

    @ApiModelProperty(value = "用户角色，租户管理员:ZHGLY, 部门管理员 orgadmin ", name = "roleCode")
    @Length(max = 32)
    private String roleCode;

    @ApiModelProperty(value = "管理部门, 部门管理员 orgadmin ", name = "unitCode")
    @Length(max = 32)
    private String unitCode;

    @ApiModelProperty(value = "待分配角色用户code", name = "memberUserCode")
    private String memberUserCode;


    @Override
    public String toString() {
        return "TenantMemberQo{" +
            "topUnit='" + topUnit + '\'' +
            ", userName='" + userName + '\'' +
            ", roleCode='" + roleCode + '\'' +
            ", unitCode='" + unitCode + '\'' +
            ", memberUserCode='" + memberUserCode + '\'' +
            '}';
    }
}
