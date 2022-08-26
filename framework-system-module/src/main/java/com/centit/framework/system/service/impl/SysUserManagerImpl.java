package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.SysUserManager;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("sysUserManager")
public class SysUserManagerImpl implements SysUserManager {
    public static Logger logger = LoggerFactory.getLogger(SysUserManagerImpl.class);
    // 加密
    @Autowired
    @NotNull
    private CentitPasswordEncoder passwordEncoder;

    @Autowired
    @NotNull
    private UserUnitDao userUnitDao;

    @Autowired
    @NotNull
    private UserRoleDao userRoleDao;

    @Autowired
    protected UserInfoDao userInfoDao;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @Value("${framework.password.default.generator:}")
    protected String defaultPassWorkFormat;

    private String getDefaultPassword(String userCode) {
        String rawPass = "000000";
        if(StringUtils.isNotBlank(defaultPassWorkFormat)){
            rawPass = Pretreatment.mapTemplateString(defaultPassWorkFormat,userCode);
        }
        return passwordEncoder.createPassword(rawPass, userCode);
    }

    private String getPasswordOnCondition(UserInfo userInfo) {
        String rawPass = "000000";
        String userCode = userInfo.getUserCode();
        String userPwd = userInfo.getUserPwd();

        if (StringUtils.isBlank(userCode)){
            //新注册用户
            if (StringUtils.isNotBlank(userPwd)){
                return passwordEncoder.createPassword(userPwd, userCode);
            }
        }else {
            //用户没有已经注册的手机号或邮箱号
            UserInfo dbUserInfo = userInfoDao.getUserByCode(userCode);
            if (null != dbUserInfo && !StringUtils.isAllBlank(dbUserInfo.getRegEmail(),dbUserInfo.getRegCellPhone())){
                //如果已经存在联系方式，不允许设置密码
                return null;
            }else if (StringUtils.isNotBlank(userPwd)){
                return passwordEncoder.createPassword(userPwd, userCode);
            }
        }

        if(StringUtils.isNotBlank(defaultPassWorkFormat)){
            rawPass = Pretreatment.mapTemplateString(defaultPassWorkFormat,userCode);
        }
        return passwordEncoder.createPassword(rawPass, userCode);
    }
    @Override
    @Transactional
    public List<RoleInfo> listUserValidRoles(String userCode) {
        // List<RoleInfo> roles = userRoleDao.getSysRolesByUserId(userCode);
        // edit by zhuxw  代码从原框架迁移过来，可和其它地方合并
        List<RoleInfo> roles = new ArrayList<>();
        //所有的用户 都要添加这个角色
        roles.add(new RoleInfo("public", "general public","G",
                "G","T", "general public"));
        List<FVUserRoles> ls = userRoleDao.listUserRolesByUserCode(userCode);
        if(ls!=null){
            for (FVUserRoles l : ls) {
                RoleInfo roleInfo = new RoleInfo();
                BeanUtils.copyProperties(l, roleInfo);
                roles.add(roleInfo);
            }
        }
        //add end
        return roles;
    }

    @Override
    @Transactional
    public void resetPwd(String userCode) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        user.setUserPin(getDefaultPassword(user.getUserCode()));
        userInfoDao.updateUser(user);
    }

    @Override
    @Transactional
    public void resetPwd(String[] userCodes) {
        for (String userCode : userCodes) {
            resetPwd(userCode);
        }
    }

    /**
     * 用户修改密码
     * @param userCode userCode
     * @param oldPassword 旧密码，前段已经处理过
     * @param newPassword   新密码 前段也已经处理过
     */
    @Override
    @Transactional
    public void setNewPassword(String userCode, String oldPassword, String newPassword) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        if (!passwordEncoder.isPasswordValid(user.getUserPin(),oldPassword,user.getUserCode() ))
            throw new ObjectException("旧密码不正确！");

        if (user.getUserPin().equals(passwordEncoder.encodePassword(newPassword, user.getUserCode())))
            throw new ObjectException("新密码和旧密码一致，请重新输入新密码！");

        user.setUserPin(passwordEncoder.encodePassword(newPassword, user.getUserCode()));
        userInfoDao.updateUser(user);
    }

    /**
     * f服务端强制设置用户密码，密码没有在前段处理过
     * @param userCode userCode
     * @param newPassword newPassword
     */
    @Override
    @Transactional
    public void forceSetPassword(String userCode, String newPassword){
        forceSetPasswordPermissionCheck(userCode);
        UserInfo user = userInfoDao.getUserByCode(userCode);
        user.setUserPin(passwordEncoder.encodePassword(newPassword, user.getUserCode()));
        userInfoDao.updateUser(user);
    }

    /**
     * 强制重置密码权限验证
     * 租户管理员且用户没有绑定联系方式可以重置密码
     * @param userCode 用户code
     */
    private void forceSetPasswordPermissionCheck(String userCode) {
        UserInfo userInfo = userInfoDao.getUserByCode(userCode);
        if (null == userInfo){
            throw new ObjectException(ResponseData.ERROR_PRECONDITION_FAILED,"用户信息不存在!");
        }

        if (!StringUtils.isAllBlank(userInfo.getRegCellPhone(),userInfo.getRegEmail())){
            throw new ObjectException(ResponseData.ERROR_PRECONDITION_FAILED,"该用户不允许重置密码");
        }
    }
    @Override
    @Transactional
    public boolean checkIfUserExists(UserInfo user) {
        return isLoginNameExist(user.getUserCode(),user.getLoginName());
    }

    @Override
    @Transactional
    public boolean isLoginNameExist(String userCode, String loginName){
        return userInfoDao.isLoginNameExist(userCode, loginName) > 0;
    }

    @Override
    @Transactional
    public boolean isCellPhoneExist(String userCode, String regPhone){

        return userInfoDao.isCellPhoneExist(userCode, regPhone) > 0;
    }
    @Override
    @Transactional
    public boolean isEmailExist(String userCode, String regEmail){

        return userInfoDao.isEmailExist(userCode, regEmail) > 0;
    }
    @Override
    @Transactional
    public boolean isAnyOneExist(String userCode, String loginName,String regPhone,String regEmail){
        return userInfoDao.isAnyOneExist(userCode, loginName, regPhone, regEmail) > 0;
    }

    @Value("${framework.userinfo.id.generator:}")
    protected String userIdFormat;

    @Override
    @Transactional
    public void saveNewUserInfo(UserInfo userInfo, UserUnit userUnit){
        userInfo.setUserPin(getPasswordOnCondition(userInfo));
        userInfo.setUserPwd(null);
        UnitInfo unitInfo = unitInfoDao.getObjectById(userInfo.getPrimaryUnit());
        if (null != unitInfo && StringUtils.isNotBlank(unitInfo.getTopUnit())) {
            userInfo.setTopUnit(unitInfo.getTopUnit());
        }
        if (null != unitInfo && StringUtils.isBlank(userInfo.getTopUnit()) && StringUtils.isNotBlank(unitInfo.getUnitPath())) {
            String[] unitCodeArray = unitInfo.getUnitPath().split("/");
            if (ArrayUtils.isNotEmpty(unitCodeArray) && unitCodeArray.length > 1) {
                userInfo.setTopUnit(unitCodeArray[1]);
            }
        }
        userInfoDao.saveNewObject(userInfo);
        //resetPwd(userInfo.getUserCode());
        userUnit.setUserUnitId(userUnitDao.getNextKey());
        userUnit.setUserCode(userInfo.getUserCode());
        userUnit.setUnitCode(userInfo.getPrimaryUnit());
        userUnit.setRelType("T");
        userUnit.setTopUnit(userInfo.getTopUnit());
        userUnitDao.saveNewObject(userUnit);

        if(null!=userInfo.getUserRoles()){
            for(UserRole ur:userInfo.getUserRoles()){
                userRoleDao.saveNewObject(ur);
            }
        }

        CodeRepositoryCache.evictCache("UserInfo");
        CodeRepositoryCache.evictCache("UserUnit");
    }


    @Override
    @Transactional
    public void updateUserInfo(UserInfo userinfo){
        userInfoDao.updateUser(userinfo);
        CodeRepositoryCache.evictCache("UserInfo");
    }

    @Override
    @Transactional
    public void updateUserProperities(UserInfo userinfo){
        userInfoDao.updateUser(userinfo);
        CodeRepositoryCache.evictCache("UserInfo");
    }

    @Override
    @Transactional
    public void deleteUserInfo(String userCode){
        userUnitDao.deleteUserUnitByUser(userCode);
        userRoleDao.deleteByUserId(userCode);
        userInfoDao.deleteObjectById(userCode);
        CodeRepositoryCache.evictCache("UserInfo");
        CodeRepositoryCache.evictCache("UserUnit");
    }

    @Override
    @Transactional
    public UserInfo loadUserByLoginname(String userCode){
        return userInfoDao.getUserByLoginName(userCode);
    }

    @Override
    @Transactional
    public boolean checkUserPassword(String userCode, String oldPassword) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        return passwordEncoder.isPasswordValid(
                user.getUserPin(),oldPassword, user.getUserCode());
    }

    @Override
    @Transactional
    public List<UserInfo> listObjects(Map<String, Object> filterMap) {
        return userInfoDao.listObjects(filterMap);
    }

    @Override
    @Transactional
    public List<UserInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return userInfoDao.listObjects(filterMap,pageDesc);
    }
    @Override
    public JSONArray listObjectsByUnit(Map<String, Object> filterMap, PageDesc pageDesc){
        return userInfoDao.listObjectsByUnit(filterMap,pageDesc);
    }
    @Override
    @Transactional
    public UserInfo getObjectById(String userCode) {
        return userInfoDao.getUserByCode(userCode);
    }

    @Override
    @Transactional
    public UserInfo getUserByRegEmail(String regEmail) {
        return userInfoDao.getUserByRegEmail(regEmail);
    }

    @Override
    @Transactional
    public UserInfo getUserByRegCellPhone(String regCellPhone) {
        return userInfoDao.getUserByRegCellPhone(regCellPhone);
    }

    @Override
    @Transactional
    public UserInfo getUserByUserWord(String userWord){
        return userInfoDao.getUserByUserWord(userWord);
    }

    @Override
    public List<OptMethod> listUserPowers(String topUnit, String userCode) {
        List<FVUserOptList> fvUserOptLists = userInfoDao.listUserPowers(topUnit, userCode);
        List<OptMethod> optMethods = new ArrayList<>();
        fvUserOptLists.forEach(fv -> {
            OptMethod method = new OptMethod();
            method.setOptId(fv.getOptId());
            method.setUserCode(fv.getUserCode());
            method.setOptCode(fv.getOptcode());
            method.setOptName(fv.getOptName());
            method.setOptMethod(fv.getOptMethod());
            optMethods.add(method);
        });
        return optMethods;
    }
}
