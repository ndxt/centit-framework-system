package com.centit.framework.system.po;

import com.centit.framework.model.basedata.IRoleInfo;
import com.centit.framework.model.basedata.IUserRole;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "F_V_USERROLES")
public class FVUserRoles implements IUserRole, IRoleInfo, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7725372179862779056L;


    @EmbeddedId
    private UserRoleId id;

    @Column(name = "ROLE_NAME")
    private String roleName; // 角色名称


  /**
   * 这个是新版本的一个新的性所有 添加了这个 默认实现
   * 用户获得这个角色的方式，
   * "D" 直接活的 ， "I" 从机构继承， "M" 从机构层级继承，至少夸一级，这个默认不打开
   */
    @Column(name = "OBTAIN_TYPE")
    private String obtainType;//

    @Column(name = "INHERITED_FROM")
    private String inheritedFrom;


    @Column(name = "IS_VALID")
    private String isValid; // 是否生效

    @Column(name = "ROLE_DESC")
    private String roleDesc; // 角色描述

    @Transient
    private List<RolePower> rolePowers;

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsValid() {
        return this.isValid;
    }

    public void setIsValid(String isvalid) {
        this.isValid = isvalid;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public void setUserCode(String userCode) {
        if (null == id) {
            id = new  UserRoleId();
        }
        id.setUserCode(userCode);
    }


    public void setRoleCode(String roleCode) {
        if (null == id) {
            id = new  UserRoleId();
        }
        id.setRoleCode(roleCode);
    }

    @Override
    public String getUserCode() {
        if (null == id) {
            return null;
        }
        return id.getUserCode();
    }

    @Override
    public String getRoleCode() {
        if (null == id) {
            return null;
        }
        return id.getRoleCode();
    }

    /**
     * 这个是新版本的一个新的性所有 添加了这个 默认实现
     * 用户获得这个角色的方式，
     * @return "D" 直接活的 ， "I" 从机构继承， "M" 从机构层级继承，至少夸一级，这个默认不打开
     */
    @Override
    public String getObtainType() {
      return obtainType;
    }

    public void setObtainType(String obtainType) {
      this.obtainType = obtainType;
    }

    @Override
    public String getInheritedFrom() {
        return inheritedFrom;
    }

    public void setInheritedFrom(String inheritedFrom) {
        this.inheritedFrom = inheritedFrom;
    }

    @Override
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
