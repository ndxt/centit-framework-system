package com.centit.framework.users.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author zfg
 */
@ApiModel(value = "同步系统机构至钉钉机构部门", description = "同步系统机构至钉钉机构部门")
public class DingUnitDTO {

    @ApiModelProperty(value = "机构代码", name = "unitCode", required = true)
    @Length(max = 32)
    private String unitCode;

    @ApiModelProperty(value = "机构名称", name = "unitName", required = true)
    @Length(max = 300)
    private String unitName;

    @ApiModelProperty(value = "上级机构代码", name = "parentUnit", required = true)
    @Length(max = 32)
    private String parentUnit;

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

    public String getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(String parentUnit) {
        this.parentUnit = parentUnit;
    }
}
