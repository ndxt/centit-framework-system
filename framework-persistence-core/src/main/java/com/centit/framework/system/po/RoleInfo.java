package com.centit.framework.system.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.framework.core.po.EntityWithTimestamp;
import com.centit.framework.model.basedata.IRoleInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FRoleinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */
// 角色信息表
@Entity
@Table(name = "F_ROLEINFO")
@ApiModel(value = "系统角色信息对象", description = "系统角色信息对象 RoleInfo")
public class RoleInfo implements IRoleInfo, EntityWithTimestamp, java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;
    @Id
    @Column(name = "ROLE_CODE")
    //@GeneratedValue(generator = "assignedGenerator")
    @ApiModelProperty(value = "角色代码,可以选择不填后台自动生成", name = "roleCode")
    private String roleCode; // 角色代码
    @Column(name = "ROLE_NAME")
    @Length(max = 300, message = "字段长度不能大于{max}")
    @ApiModelProperty(value = "角色名称", name = "roleName", required = true)
    private String roleName; // 角色名称
    @Column(name = "IS_VALID")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度必须为{max}")
    @Pattern(regexp = "[TFA]", message = "字段值必须是T或F,A为新建可以删除")
    @ApiModelProperty(value = "是否生效 T:生效 F:失效 A:新建可以删除", name = "isValid", required = true)
    private String isValid; // 是否生效
    /**
     * 角色的类别 F （Fixe）系统内置的，固有的， G （global） 全局的
     * P （public） 公用的，指 系统全局 和 部门之间公用的
     * D （department）部门（机构）特有的角色
     * I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     * H (HIDE)系统内置的不要显示的，是部门可以自己支配的操作权限集合
     * 角色的类别 F/G/P/D/I/W
     */
    @Column(name = "ROLE_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    @ApiModelProperty(value = "角色的类别 F:系统内置的 G:全局的 P:公用的 D:部门（机构）特有的角色 I:项目角色 W:工作流角色", name = "roleType", required = true)
    @DictionaryMap(fieldName = "roleTypeText", value = "RoleType")
    private String roleType; // 角色类别
    @Column(name = "UNIT_CODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String unitCode; // 角色所属机构
    @Column(name = "ROLE_DESC")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String roleDesc; // 角色描述
    /**
     * CREATOR(创建人) 创建人
     */
    @Column(name = "CREATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String creator;
    /**
     * UPDATOR(更新人) 更新人
     */
    @Column(name = "UPDATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String updator;
    /**
     * UPDATEDATE(更新时间) 更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Transient
    private List<RolePower> rolePowers;

    /**
     * default constructor
     */
    public RoleInfo() {
        roleType = "G";
    }

    /**
     * minimal constructor
     *
     * @param rolecode String
     * @param isvalid  String
     */
    public RoleInfo(String rolecode, String isvalid) {
        this.roleCode = rolecode;
        this.isValid = isvalid;
        this.roleType = "G";
    }

    public RoleInfo(String rolecode, String rolename, String roleType,
                    String unitCode, String isvalid, String roledesc) {
        this.roleCode = rolecode;
        this.roleName = rolename;
        this.roleType = roleType;
        this.unitCode = unitCode;
        this.isValid = isvalid;
        this.roleDesc = roledesc;
    }

    // Property accessors
    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String rolecode) {
        this.roleCode = rolecode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String rolename) {
        this.roleName = rolename;
    }

    public String toString() {
        return this.roleName;
    }

    public String getIsValid() {
        return this.isValid;
    }

    public void setIsValid(String isvalid) {
        this.isValid = isvalid;
    }

    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roledesc) {
        this.roleDesc = roledesc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 角色的类别 F （Fixe）系统内置的，固有的， G （global） 全局的
     * P （public） 公用的，指 系统全局 和 部门之间公用的
     * D （department）部门（机构）特有的角色
     * I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     * H (HIDE)系统内置的不要显示的，是部门可以自己支配的操作权限集合
     * 角色的类别 F/G/P/D/I/W
     *
     * @return 角色的类别 F/G/P/D/I/W
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 角色的类别 F （Fixed）系统内置的，固有的， G （global） 全局的
     * P （public） 公用的，指 系统全局 和 部门之间公用的
     * D （department）部门（机构）特有的角色
     * I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     * H (HIDE)系统内置的不要显示的，是部门可以自己支配的操作权限集合
     *
     * @param roleType 角色的类别 F/G/P/D/I/W/H
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getRoleOwner() {
        return unitCode;
    }

    @Override
    public Date getLastModifyDate() {
        return updateDate;
    }

    @Override
    public void setLastModifyDate(Date lastModifyDate) {
        this.updateDate = lastModifyDate;
    }

    public void copyNotNullProperty(RoleInfo other) {
        if (other.getRoleCode() != null)
            this.roleCode = other.getRoleCode();
        if (other.getRoleName() != null)
            this.roleName = other.getRoleName();
        if (other.getIsValid() != null)
            this.isValid = other.getIsValid();
        if (other.getRoleDesc() != null)
            this.roleDesc = other.getRoleDesc();
        if (other.getRoleType() != null)
            this.roleType = other.getRoleType();
        if (other.getUnitCode() != null)
            this.unitCode = other.getUnitCode();
        if (other.getCreator() != null)
            this.creator = other.getCreator();
        if (other.getUpdator() != null)
            this.updator = other.getUpdator();
        if (other.getCreateDate() != null)
            this.createDate = other.getCreateDate();
        if (other.getUpdateDate() != null)
            this.updateDate = other.getUpdateDate();
    }

    public void copy(RoleInfo other) {
        this.roleCode = other.getRoleCode();
        this.roleName = other.getRoleName();
        this.isValid = other.getIsValid();
        this.roleDesc = other.getRoleDesc();
        this.roleType = other.getRoleType();
        this.unitCode = other.getUnitCode();
        this.creator = other.creator;
        this.updator = other.updator;
        this.createDate = other.createDate;
        this.updateDate = other.updateDate;
    }


    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return this.updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    //@JSONField(serialize=true,deserialize=false,label="rolePowers")
    public List<RolePower> getRolePowers() {
        if (null == rolePowers) {
            rolePowers = new ArrayList<>();
        }
        return rolePowers;
    }

    public void setRolePowers(List<RolePower> rolePowers) {
        this.rolePowers = rolePowers;
    }

    public void addAllRolePowers(List<RolePower> rolePowers) {
        getRolePowers().clear();
        if (CollectionUtils.isEmpty(rolePowers)) {
            return;
        }
        getRolePowers().addAll(rolePowers);
    }
}
