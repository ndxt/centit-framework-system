package com.centit.framework.system.po;

import com.centit.framework.core.po.EntityWithTimestamp;
import com.centit.framework.model.basedata.IRoleInfo;
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
public class RoleInfo implements IRoleInfo,EntityWithTimestamp, java.io.Serializable{

    // Fields
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_CODE")
    //@GeneratedValue(generator = "assignedGenerator")
    private String roleCode; // 角色代码

    @Column(name = "ROLE_NAME")
    @Length(max = 300, message = "字段长度不能大于{max}")
    private String roleName; // 角色名称

    @Column(name = "IS_VALID")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1, message = "字段长度必须为{max}")
    @Pattern(regexp = "[TFA]", message = "字段值必须是T或F,A为新建可以删除")
    private String isValid; // 是否生效

    /**
     * 角色的类别 F （Fixe）系统内置的，固有的， G （global） 全局的
     *          P （public） 公用的，指 系统全局 和 部门之间公用的
     *          D （department）部门（机构）特有的角色
     *          I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     *  角色的类别 F/G/P/D/I/W
     */
    @Column(name = "ROLE_TYPE")
    @Length(max = 1, message = "字段长度必须为{max}")
    private String roleType; // 角色类别

    @Column(name = "UNIT_CODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String unitCode; // 角色所属机构


    @Column(name = "ROLE_DESC")
    @Length(max = 256, message = "字段长度不能大于{max}")
    private String roleDesc; // 角色描述

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;

    /**
     * CREATOR(创建人) 创建人
     */
    @Column(name = "CREATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  creator;
       /**
     * UPDATOR(更新人) 更新人
     */
    @Column(name = "UPDATOR")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  updator;
    /**
     * UPDATEDATE(更新时间) 更新时间
     */
    @Column(name = "UPDATE_DATE")
    private Date  updateDate;

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
     * @param rolecode String
     * @param isvalid String
     */
    public RoleInfo(String rolecode, String isvalid) {
        this.roleCode = rolecode;
        this.isValid = isvalid;
        this.roleType = "G";
    }

    public RoleInfo(String rolecode, String rolename,String roleType,
            String unitCode,String isvalid, String roledesc) {
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

    public String toString() {
        return this.roleName;
    }

    public void setRoleName(String rolename) {
        this.roleName = rolename;
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
     *          P （public） 公用的，指 系统全局 和 部门之间公用的
     *          D （department）部门（机构）特有的角色
     *          I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     *  角色的类别 F/G/P/D/I/W
     * @return 角色的类别 F/G/P/D/I/W
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * 角色的类别 F （Fixe）系统内置的，固有的， G （global） 全局的
     *          P （public） 公用的，指 系统全局 和 部门之间公用的
     *          D （department）部门（机构）特有的角色
     *          I ( Item )为项目角色 W (workflow)工作流角色 ，这两个为保留类别，暂时没有使用
     * @param roleType 角色的类别 F/G/P/D/I/W
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String getRoleOwner() {
      return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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
        if(other.getRoleCode() != null)
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
            this.creator =other.getCreator();
        if (other.getUpdator() != null)
            this.updator =other.getUpdator();
        if(other.getCreateDate() != null)
            this.createDate = other.getCreateDate();
        if (other.getUpdateDate() != null)
            this.updateDate =other.getUpdateDate();
    }

    public void copy(RoleInfo other) {
        this.roleCode = other.getRoleCode();
        this.roleName = other.getRoleName();
        this.isValid = other.getIsValid();
        this.roleDesc = other.getRoleDesc();
        this.roleType = other.getRoleType();
        this.unitCode = other.getUnitCode();
        this.creator=other.creator;
        this.updator=other.updator;
        this.createDate = other.createDate;
        this.updateDate=other.updateDate;
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
        if(CollectionUtils.isEmpty(rolePowers)) {
            return;
        }
        getRolePowers().addAll(rolePowers);
    }
}
