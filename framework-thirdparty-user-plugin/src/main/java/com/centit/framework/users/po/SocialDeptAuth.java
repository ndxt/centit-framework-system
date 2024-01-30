package com.centit.framework.users.po;

import com.centit.support.database.orm.GeneratorCondition;
import com.centit.support.database.orm.GeneratorTime;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "F_SOCIAL_DEPT_AUTH")
@ApiModel(value = "系统部门平台关联", description = "系统部门平台关联 SocialDept")
public class SocialDeptAuth implements java.io.Serializable {
    private static final long serialVersionUID = -2538006375160615889L;

    @Id
    @Column(name = "UNIT_CODE")
    @ApiModelProperty(value = "机构代码", name = "unitCode")
    private String unitCode;


    @Column(name = "DEPT_ID")
    @Length(max = 64, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "平台部门", name = "appKey")
    private String deptId;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    protected Date createDate;

    /**
     * UPDATEDATE(更新时间) 更新时间
     */
    @Column(name = "UPDATE_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, occasion = GeneratorTime.NEW_UPDATE,
        condition = GeneratorCondition.ALWAYS, value = "today()")
    private Date updateDate;

    /**
     * default constructor
     */
    public SocialDeptAuth() {
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
