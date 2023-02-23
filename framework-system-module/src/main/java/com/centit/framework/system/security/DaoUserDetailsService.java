package com.centit.framework.system.security;

import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.security.model.CentitSecurityMetadata;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.security.model.CentitUserDetailsService;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserSettingDao;
import com.centit.framework.system.po.FVUserRoles;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

//@Service("centitUserDetailsService")
public class DaoUserDetailsService
    implements CentitUserDetailsService,UserDetailsService,
        AuthenticationUserDetailsService<Authentication> {

    @Autowired
    @NotNull
    private PlatformEnvironment platformEnvironment;

    @Autowired
    @NotNull
    private UserSettingDao userSettingDao;

    @Autowired
    @NotNull
    private UserRoleDao userRoleDao;

    @Autowired
    @NotNull
    private UserInfoDao userInfoDao;

    @Transactional
    public Collection<GrantedAuthority> loadUserAuthorities(String loginName) throws UsernameNotFoundException {
        UserInfo userinfo = userInfoDao.getUserByLoginName(loginName);
        if(userinfo==null)
            return null;
        //edit by zhuxw  代码从原框架迁移过来，可和其它地方合并

        List<GrantedAuthority> authorities = new ArrayList<>(10);
        authorities.add( new SimpleGrantedAuthority(
          CentitSecurityMetadata.ROLE_PREFIX + "public" ));

        List<FVUserRoles> userRoles = userRoleDao.listUserRolesByUserCode(userinfo.getUserCode());
        if(userRoles!=null){
            for (FVUserRoles userRole : userRoles) {
                authorities.add(new SimpleGrantedAuthority(CentitSecurityMetadata.ROLE_PREFIX
                  + StringUtils.trim(userRole.getRoleCode())));
            }
        }
        Collections.sort(authorities,Comparator.comparing(GrantedAuthority::getAuthority));
        return authorities;
    }

    @Override
    public CentitUserDetails loadUserByUsername(String loginname) throws UsernameNotFoundException {
        if(StringUtils.isBlank(loginname)){
            throw new UsernameNotFoundException("用户名为不能为空！");
        }
        CentitUserDetails ud = null;
        if(loginname.indexOf('@')>0){
            ud = platformEnvironment.loadUserDetailsByRegEmail(loginname);
        } else if(loginname.charAt(0) >='0' && loginname.charAt(0) <='9'){
            ud = platformEnvironment.loadUserDetailsByRegCellPhone(loginname);
        }
        if(ud == null){
            ud = platformEnvironment.loadUserDetailsByLoginName(loginname);
        }

        if(ud == null){
          throw new UsernameNotFoundException("用户名或密码错误！");
        }
        return ud;
    }

    @Override
    public CentitUserDetails loadDetailsByLoginName(String loginname) {
          return loadUserByUsername(loginname);
    }

    @Override
    public CentitUserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        return loadUserByUsername(token.getName());
    }

    @Override
    @Transactional
    public void saveUserSetting(String userCode, String paramCode,String paramValue,
            String paramClass, String paramName) {

        userSettingDao.saveNewUserSetting(new UserSetting(userCode, paramCode, paramValue,
                 paramClass,  paramName));
    }

    @Override
    @Transactional
    public CentitUserDetails loadDetailsByUserCode(String userCode) {
        return platformEnvironment.loadUserDetailsByUserCode(userCode);
    }

    @Override
    @Transactional
    public CentitUserDetails loadDetailsByRegEmail(String regEmail) {
        return platformEnvironment.loadUserDetailsByRegEmail(regEmail);
    }

    @Override
    @Transactional
    public CentitUserDetails loadDetailsByRegCellPhone(String regCellPhone) {
        return platformEnvironment.loadUserDetailsByRegCellPhone(regCellPhone);
    }
}
