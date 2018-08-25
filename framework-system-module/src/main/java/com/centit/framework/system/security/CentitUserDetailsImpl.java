package com.centit.framework.system.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.security.model.AbstractCentitUserDetails;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserUnit;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * UserInfo entity.
 * @author MyEclipse Persistence Tools
 */

public class CentitUserDetailsImpl extends AbstractCentitUserDetails {
    private static final long serialVersionUID = 1L;

    public CentitUserDetailsImpl(){
        arrayAuths = null;
    }

    public CentitUserDetailsImpl(UserInfo userInfo) {
        this.userInfo = userInfo;
        arrayAuths = null;
    }

    protected UserInfo userInfo;
    private List<RoleInfo> userRoles;
    private List<UserUnit> userUnits;
    // role
    // private Date lastUpdateRoleTime;
    @JSONField(serialize = false)
    private List<GrantedAuthority> arrayAuths;

    @Override
    public UserInfo getUserInfo() {
      return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
    }

    @Override
    public List<RoleInfo> getUserRoles() {
      return userRoles;
    }

    public void setUserRoles(List<RoleInfo> userRoles) {
      this.userRoles = userRoles;
    }

  // Property accessors
    public void setAuthoritiesByRoles(List<RoleInfo> roles) {
        setUserRoles(roles);
        makeUserAuthorities();
    }

    @Override
    public List<? extends IUserUnit> getUserUnits() {
        if (userUnits == null)
            userUnits = new ArrayList<>();
        return userUnits;
    }

    public void setUserUnits(List<UserUnit> userUnits) {
        this.userUnits = userUnits;
    }

}
