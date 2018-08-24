package com.centit.framework.system.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.security.model.CentitSecurityMetadata;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

/**
 * UserInfo entity.
 * @author MyEclipse Persistence Tools
 */

public class CentitUserDetailsImpl implements CentitUserDetails, java.io.Serializable{
    private static final long serialVersionUID = 1L;

    public CentitUserDetailsImpl(){
        arrayAuths = null;
    }

    public CentitUserDetailsImpl(UserInfo userInfo) {
        this.userInfo = userInfo;
        arrayAuths = null;
    }

    private String currentStationId;
    protected UserInfo userInfo;

    private Map<String, String> userSettings;
    private Map<String, String> userOptList;
    private List<RoleInfo> userRoles;
    private List<UserUnit> userUnits;
    // role
    // private Date lastUpdateRoleTime;
    @JSONField(serialize = false)
    private List<GrantedAuthority> arrayAuths;

    @JSONField(serialize = false)
    public String getUserCode(){
        return getUserInfo().getUserCode();
    }

    @Override
    public UserInfo getUserInfo() {
      return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
      this.userInfo = userInfo;
    }

    public Map<String, String> getUserSettings() {
        if(userSettings==null)
            userSettings = new HashMap<>();
        return userSettings;
    }

    public void putUserSettingsParams(String paramCode, String paramValue) {
        if(userSettings==null)
            userSettings = new HashMap<>();
        userSettings.put(paramCode, paramValue);
    }
    public void setUserSettings(Map<String, String> userSettings) {
        this.userSettings = userSettings;
    }

    public Map<String, String> getUserOptList() {
        if(userOptList==null)
            userOptList = new HashMap<>();
        return userOptList;
    }

    public void setUserOptList(Map<String, String> userOptList) {
        this.userOptList = userOptList;
    }

    @Override
    public boolean checkOptPower(String optId, String optMethod){
        String s = userOptList.get(optId + "-" + optMethod);
        if (s == null) {
            return false;
        }
        return "T".equals(s);
    }

    @Override
    public List<RoleInfo> getUserRoles() {
      return userRoles;
    }

    public void setUserRoles(List<RoleInfo> userRoles) {
      this.userRoles = userRoles;
    }

    @Override
    @JSONField(serialize = false)
    public UserUnit getCurrentStation() {

        List<UserUnit> uus = this.getUserUnits();
        if (uus != null) {
            for (UserUnit uu : uus) {
                if (StringUtils.equals(currentStationId, uu.getUserUnitId())) {
                    return uu;
                }

                if (StringUtils.isBlank(currentStationId) && "T".equals(uu.getIsPrimary())) {
                    return uu;
                }
            }
        }
        return null;
    }

    @Override
    public void setCurrentStationId(String userUnitId) {
        currentStationId = userUnitId;
    }

    public String getCurrentStationId(){
        return currentStationId;
    }

    @Override
    @JSONField(serialize = false)
    public String getCurrentUnitCode(){
        IUserUnit cs = getCurrentStation();
        return cs != null? cs.getUnitCode() : getUserInfo().getPrimaryUnit();
    }

    @Override
    @JSONField(serialize = false)
    public String getTopUnitCode(){
        IUserUnit cs = getCurrentStation();
        IUnitInfo unitInfo = cs == null ?
            CodeRepositoryUtil.getUnitInfoByCode(getUserInfo().getPrimaryUnit()):
            CodeRepositoryUtil.getUnitInfoByCode(cs.getUnitCode());
        if(unitInfo == null){
            return cs == null ? getUserInfo().getPrimaryUnit() : cs.getUnitCode();
        }
        int pos = unitInfo.getUnitPath().indexOf('/');
        return pos<1 ? unitInfo.getUnitPath() :
            unitInfo.getUnitPath().substring(0,pos);
    }

    private void makeUserAuthorities(){
      arrayAuths = new ArrayList<>();
      if (this.userRoles.size() < 1)
        return;

        boolean havePublicRole = false;
        for (RoleInfo role : this.userRoles) {
            arrayAuths.add(new SimpleGrantedAuthority(CentitSecurityMetadata.ROLE_PREFIX
                + StringUtils.trim(role.getRoleCode())));
            if(SecurityContextUtils.PUBLIC_ROLE_CODE.equalsIgnoreCase(role.getRoleCode())){
                havePublicRole = true;
            }
        }
        if(!havePublicRole){
            arrayAuths.add(new SimpleGrantedAuthority(CentitSecurityMetadata.ROLE_PREFIX
                + SecurityContextUtils.PUBLIC_ROLE_CODE));
        }
      //排序便于后面比较
      Collections.sort(arrayAuths,Comparator.comparing(GrantedAuthority::getAuthority));
      //lastUpdateRoleTime = new Date(System.currentTimeMillis());
    }
  // Property accessors
    public void setAuthoritiesByRoles(List<RoleInfo> roles) {
        setUserRoles(roles);
        makeUserAuthorities();
    }

    @Override
    @JSONField(serialize = false)
    public Collection<GrantedAuthority> getAuthorities() {
        /*if (arrayAuths == null || lastUpdateRoleTime == null
                || (new Date(System.currentTimeMillis())).getTime() - lastUpdateRoleTime.getTime() > 10 * 60 * 1000)
            loadAuthoritys();*/
        return arrayAuths;
    }

    @Override
    public void setLoginIp(String loginHost) {
        this.userInfo.setLoginIp(loginHost);
    }

    @Override
    public void setActiveTime(Date loginTime) {
        this.userInfo.setActiveTime(loginTime);
    }

    @Override
    public String getUsername() {
        return this.userInfo.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return "T".equals(this.userInfo.getIsValid());
    }

    @Override
    public boolean isAccountNonLocked() {
        return "T".equals(this.userInfo.getIsValid());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return "T".equals(this.userInfo.getIsValid());
    }

    @Override
    public boolean isEnabled() {
      return "T".equals(this.userInfo.getIsValid());
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return this.userInfo.getUserPin();
    }

    @Override
    public String getUserSettingValue(String paramCode){
        if(userSettings==null)
            return null;
        return userSettings.get(paramCode);
    }

    public void setUserSettingValue(String paramCode, String paramValue) {
        if(userSettings==null)
            userSettings=new HashMap<>();
        userSettings.put(paramCode, paramValue);
    }

    @Override
    public String toString() {
        return "CentitUserDetail:" + super.toString();
    }

    @Override
    public boolean equals(Object other) {
       if(other instanceof CentitUserDetails)
           return this.userInfo.getUserCode().equals(((CentitUserDetails) other).getUserInfo().getUserCode());

       return false;
    }

    @Override
    public int hashCode() {
        return this.userInfo.getUserCode().hashCode();
    }

    @Override
    @JSONField(serialize = false)
    public Object getCredentials() {
        return this.userInfo.getUserPin();
    }

    @Override
    @JSONField(serialize = false)
    public Object getDetails() {
        return this;
    }

    @Override
    @JSONField(serialize = false)
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return this.userInfo.getLoginName();
    }

    private void addUserUnit(UserUnit userunit) {
        this.getUserUnits().add(userunit);

    }

    private void removeUserUnit(UserUnit odt) {
        this.getUserUnits().remove(odt);

    }

    @Override
    public List<UserUnit> getUserUnits() {
        if (userUnits == null)
            userUnits = new ArrayList<>();
        return userUnits;
    }

    public void setUserUnits(List<UserUnit> userUnits) {
        this.userUnits = userUnits;
    }

}
