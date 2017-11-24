package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ObjectException;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.system.dao.UserInfoDao;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.SysUserManager;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysUserManager")
public class SysUserManagerImpl implements SysUserManager {
    public static Logger logger = LoggerFactory.getLogger(SysUserManagerImpl.class);
    // 加密
    @Resource
    @NotNull
    private CentitPasswordEncoder passwordEncoder;

    @Resource
    @NotNull
    private UserUnitDao userUnitDao;

    @Resource
    @NotNull
    private UserRoleDao userRoleDao;

    @Resource
    protected UserInfoDao userInfoDao;


    private String getDefaultPassword(String userCode) {
        //final String defaultPassword = "000000";
        return passwordEncoder.createPassword("000000", userCode);
    }

    @Override
    @Transactional
    public List<RoleInfo> listUserValidRoles(String userCode) {
       // List<RoleInfo> roles = userRoleDao.getSysRolesByUserId(userCode);

        //edit by zhuxw  代码从原框架迁移过来，可和其它地方合并
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
       //add  end

        return roles;
    }



    @Override
    @Transactional
    public void resetPwd(String userCode) {
        UserInfo user = userInfoDao.getObjectById(userCode);
        user.setUserPin(getDefaultPassword(user.getUserCode()));
        userInfoDao.restPwd(user);
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
        UserInfo user = userInfoDao.getObjectById(userCode);
        if (!passwordEncoder.isPasswordValid(user.getUserPin(),oldPassword,user.getUserCode() ))
            throw new ObjectException("旧密码不正确！");

        if (user.getUserPin().equals(passwordEncoder.encodePassword(newPassword, user.getUserCode())))
            throw new ObjectException("新密码和旧密码一致，请重新输入新密码！");

        user.setUserPin(passwordEncoder.encodePassword(newPassword, user.getUserCode()));
        userInfoDao.mergeObject(user);
    }

    /**
     * f服务端强制设置用户密码，密码没有在前段处理过
     * @param userCode userCode
     * @param newPassword newPassword
     */
    @Override
    @Transactional
    public void forceSetPassword(String userCode, String newPassword){
        UserInfo user = userInfoDao.getObjectById(userCode);
        user.setUserPin(passwordEncoder.createPassword(newPassword, user.getUserCode()));
        userInfoDao.mergeObject(user);
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
        Map<String,String> map =new HashMap<String,String>();
        map.put("userCode", StringUtils.isBlank(userCode)?"null":userCode);
        map.put("loginName", StringUtils.isBlank(loginName)?"null":loginName);
        map.put("regCellPhone", StringUtils.isBlank(regPhone)?"null":regPhone);
        map.put("regEmail", StringUtils.isBlank(regEmail)?"null":regEmail);

        return userInfoDao.isAnyOneExist(userCode, loginName, regPhone, regEmail) > 0;
    }

    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    @Transactional
    public void saveNewUserInfo(UserInfo userInfo, UserUnit userUnit){
        String userCode = userInfoDao.getNextKey();
        String userIdFormat = SysParametersUtils.getStringValue("framework.userinfo.id.generator");
        if(StringUtils.isBlank(userIdFormat)){
            userCode = StringBaseOpt.midPad(userCode,8,"U",'0');
        }else{
            //{"prefix":"U","length":8,"pad":"0"}
            JSONObject idFormat = (JSONObject)JSON.parse(userIdFormat);
            if(idFormat!=null) {
                userCode = StringBaseOpt.midPad(userCode,
                NumberBaseOpt.castObjectToInteger(idFormat.get("length"), 1),
                idFormat.getString("prefix"),
                idFormat.getString("pad"));
            }
        }

        userInfo.setUserCode(userCode);
        userInfo.setUserPin(getDefaultPassword(userInfo.getUserCode()));
        userInfoDao.saveNewObject(userInfo);
        //resetPwd(userInfo.getUserCode());
        userUnit.setUserUnitId(userUnitDao.getNextKey());
        userUnit.setUserCode(userInfo.getUserCode());
        userUnit.setUnitCode(userInfo.getPrimaryUnit());
        userUnit.setIsPrimary("T");
        userUnitDao.saveNewObject(userUnit);
//        if(null!=userInfo.getUserUnits()){
//            for(UserUnit uu:userInfo.getUserUnits()){
//                userUnitDao.saveNewObject(uu);
//            }
//        }
        if(null!=userInfo.listUserRoles()){
            for(UserRole ur:userInfo.listUserRoles()){
                userRoleDao.saveNewObject(ur);
            }
        }
    }


    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    @Transactional
    public void updateUserInfo(UserInfo userinfo){

        userInfoDao.mergeObject(userinfo);

        /*List<UserUnit> oldUserUnits = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
         if(oldUserUnits!=null){
            for(UserUnit uu: oldUserUnits ){
                if(null ==userinfo.getUserUnits() ||
                        ! userinfo.getUserUnits().contains(uu)){
                    userUnitDao.deleteObject(uu);
                }
            }
        }

        if(userinfo.getUserUnits() !=null){
            for(UserUnit uu: userinfo.getUserUnits() ){
                 userUnitDao.mergeObject(uu);
            }
        }

        */
    }

    @Override
    @CacheEvict(value ="UserInfo",allEntries = true)
    @Transactional
    public void updateUserProperities(UserInfo userinfo){
        userInfoDao.mergeObject(userinfo);
    }

    @Override
    @CacheEvict(value ={"UserInfo","UnitUsers","UserUnits","AllUserUnits"},allEntries = true)
    @Transactional
    public void deleteUserInfo(String userCode){
        userUnitDao.deleteUserUnitByUser(userCode);
        userRoleDao.deleteByUserId(userCode);
        userInfoDao.deleteObjectById(userCode);
    }

    @Override
    @Transactional
    public String getNextUserCode() {
        return userInfoDao.getNextKey();
    }

    @Override
    @Transactional
    public UserInfo loadUserByLoginname(String userCode){
        return userInfoDao.getUserByLoginName(userCode);
    }

    @Override
    @Transactional
    public List<FVUserOptList> getAllOptMethodByUser(String userCode){
        return userInfoDao.getAllOptMethodByUser(userCode);
    }

    @Override
    @Transactional
    public boolean checkUserPassword(String userCode, String oldPassword) {
        UserInfo user = userInfoDao.getObjectById(userCode);
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
        return userInfoDao.pageQuery(
            QueryParameterPrepare.prepPageParams(filterMap,pageDesc,
              userInfoDao.pageCount(filterMap)));
    }

    @Override
    @Transactional
    public UserInfo getObjectById(String userCode) {
        return userInfoDao.getObjectById(userCode);
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
}
