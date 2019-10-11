package com.centit.framework.system.service.impl;

import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.model.basedata.IDataDictionary;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
@Service("sysUserUnitManager")
@Transactional
public class SysUserUnitManagerImpl
    implements SysUserUnitManager {
    public static final Logger logger = LoggerFactory.getLogger(SysUserUnitManagerImpl.class);

    @Resource
    @NotNull
    protected UserUnitDao userUnitDao;

    @Resource
    private UnitInfoDao unitInfoDao;

    @Resource
    @NotNull
    private UserInfoDao userInfoDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private RoleInfoDao roleInfoDao;

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listObjectByUserUnit(String userCode,String unitCode){
        Map<String,String>map=new HashMap<>();
        map.put("userCode", userCode);
        map.put("unitCode", unitCode);

        List<UserUnit> userUnits = userUnitDao.listObjectByUserUnit(userCode, unitCode);
        if(userUnits!=null){
            for (UserUnit uu : userUnits) {
                if (null == uu) {
                    continue;
                }
                // 设置行政角色等级
                IDataDictionary dd = CodeRepositoryUtil.getDataPiece("RankType", uu.getUserRank());
                if (dd != null && dd.getExtraCode() != null && StringRegularOpt.isNumber(dd.getExtraCode())) {
                    try {
                        uu.setXzRank(Integer.valueOf(dd.getExtraCode()));
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        uu.setXzRank(CodeRepositoryUtil.MAXXZRANK);
                    }
                 }
            }
        }
        return userUnits;
    }

    private static boolean isMultiToMulti() {
        IDataDictionary agencyMode = CodeRepositoryUtil.getDataPiece("SYSPARAM","userUnitMode");

        if (agencyMode!=null) {
            return ("M".equalsIgnoreCase(agencyMode.getDataValue()));
        }
        return true;
    }

    private void addUserRoleWhenNotExist(String userCode, String roleCodeOrName , List<FVUserRoles> userRoles){
        if(StringUtils.isNotBlank(roleCodeOrName)){
            boolean hasRole = false;
            for(FVUserRoles userRole : userRoles){
                if(userRole.getRoleCode().equals(roleCodeOrName) ||
                    (("G".equals(userRole.getRoleType()) || "P".equals(userRole.getRoleType()))
                        && userRole.getRoleName().equals(roleCodeOrName))){
                  hasRole = true;
                }
            }
            if(!hasRole){
            //            IRoleInfo roleInfo = CodeRepositoryUtil.getRoleByRoleCode(roleCode);
                RoleInfo roleInfo = roleInfoDao.getRoleByCodeOrName(roleCodeOrName);
                if(roleInfo != null){
                    UserRole newUserRole = new UserRole(
                    new UserRoleId(userCode,roleInfo.getRoleCode()),
                    DatetimeOpt.currentUtilDate(), "根据用户岗位自动授予");
                    userRoleDao.saveNewObject(newUserRole);
                    FVUserRoles fvUserRoles = new FVUserRoles( userCode,roleInfo.getRoleCode());
                    fvUserRoles.setRoleName(roleInfo.getRoleName());
                    fvUserRoles.setRoleType(roleInfo.getRoleType());
                    userRoles.add(fvUserRoles);
                }
            }
            //}
        }
    }

    @Override
    public String saveNewUserUnit(UserUnit userunit) {
        // 一对多模式, 删除主机构    多对多，将当前主机构设置为非主机构
        if (! isMultiToMulti()) {
            UserUnit pUserUnit = userUnitDao.getPrimaryUnitByUserId(userunit.getUserCode());
            if (null != pUserUnit) {
              userUnitDao.deleteObjectById(pUserUnit.getUserUnitId());
            }
        }

        if(StringBaseOpt.isNvl(userunit.getUserUnitId())){
            userunit.setUserUnitId(userUnitDao.getNextKey());
        }

        if ("T".equals(userunit.getIsPrimary())) {
            UserUnit origPrimUnit=userUnitDao.getPrimaryUnitByUserId(userunit.getUserCode());
            if(origPrimUnit!=null){
                origPrimUnit.setIsPrimary("F");
                //userunit.setIsPrimary("T");
                userUnitDao.updateUserUnit(origPrimUnit);
            }
            UserInfo user=userInfoDao.getUserByCode(userunit.getUserCode());
            if(user != null) {
                user.setPrimaryUnit(userunit.getUnitCode());
                userInfoDao.updateUser(user);
            }
        }
        // userunit.setIsprimary("T");//modify by hx bug：会默认都是主机构
        userUnitDao.saveNewObject(userunit);
        List<FVUserRoles> userRoles = userRoleDao.listUserRolesByUserCode(userunit.getUserCode());
        IDataDictionary dd = CodeRepositoryUtil.getDataPiece("StationType",userunit.getUserStation());
        addUserRoleWhenNotExist(userunit.getUserCode(),dd.getExtraCode2(), userRoles);
        dd = CodeRepositoryUtil.getDataPiece("RankType",userunit.getUserRank());
        addUserRoleWhenNotExist(userunit.getUserCode(),dd.getExtraCode2(), userRoles);
        CodeRepositoryCache.evictCache("UserUnit");
        return userunit.getUserUnitId();
    }



    @Override
    public UserUnit getPrimaryUnitByUserCode(String userCode) {
        UserUnit uu=userUnitDao.getPrimaryUnitByUserId(userCode);
        return uu;
    }

    @Override
    public boolean hasUserStation(String stationCode,String userCode) {
        HashMap <String ,Object>filterDesc=new HashMap<>();
        filterDesc.put("userStation", stationCode);
        filterDesc.put("userCode", userCode);
        List<UserUnit> list=userUnitDao.listObjects(filterDesc);
        if(null!=list&& list.size()!=0) {
          return true;
        } else {
          return false;
        }
    }

    @Override
    public void updateUserUnit(UserUnit userunit) {
        if ("T".equals(userunit.getIsPrimary())) {
            UserUnit origPrimUnit=userUnitDao.getPrimaryUnitByUserId(userunit.getUserCode());
            if(origPrimUnit!=null && ! origPrimUnit.getUserUnitId().equals(userunit.getUserUnitId())){
                origPrimUnit.setIsPrimary("F");
                userunit.setIsPrimary("T");
                userUnitDao.updateUserUnit(origPrimUnit);
            }
            UserInfo user=userInfoDao.getUserByCode(userunit.getUserCode());
            if(user != null) {
                user.setPrimaryUnit(userunit.getUnitCode());
                userInfoDao.updateUser(user);
            }
        }
        userUnitDao.updateUserUnit(userunit);
        CodeRepositoryCache.evictCache("UserUnit");
    }

    @Override
    public UserUnit getObjectById(String userUnitId) {
        return userUnitDao.getObjectById(userUnitId);
    }

    @Override
    public void deleteObject(UserUnit userUnit) {
        userUnitDao.deleteObject(userUnit);
    }

    @Override
    public List<UserUnit> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return userUnitDao.listObjects(filterMap, pageDesc);
    }

    @Override
    @Transactional
    public List<UserUnit> listUnitUsersByUnitCode(String unitCode){
        return userUnitDao.listUnitUsersByUnitCode(unitCode);
    }

    @Override
    @Transactional
    public List<UserUnit> listUserUnitssByUserCode(String userCode){
        return userUnitDao.listUserUnitsByUserCode(userCode);
    }
    @Override
    @Transactional
    public void deletePrimaryUnitByUserCode(String userCode){
        if(userUnitDao.getPrimaryUnitByUserId(userCode) != null) {
            userUnitDao.deleteObject(userUnitDao.getPrimaryUnitByUserId(userCode));
        }
    }

    @Override
    @Transactional
    public List<UserUnit> listSubUsersByUnitCode(String unitCode, Map<String, Object> map, PageDesc pageDesc){
        UnitInfo unitInfo = unitInfoDao.getObjectById(unitCode);
        if(unitInfo != null){
            map.put("unitPath", unitInfo.getUnitPath());
            map.put("isValid", "T");
            return userUnitDao.querySubUserUnits(
                QueryParameterPrepare.makeMybatisOrderByParam(
              QueryParameterPrepare.prepPageParams(map,pageDesc,userUnitDao.countSubUserUnits(map)),UserUnit.class));
        }
        return null;
    }

    public List<UserUnit> listUserUnitsUnderUnitByUserCode(String userCode, String unitCode, PageDesc pageDesc){
        UnitInfo unitInfo = unitInfoDao.getObjectById(unitCode);
        Map<String, Object> map = new HashMap<>(5);
        map.put("userCode", userCode);
        map.put("unitPath", unitInfo.getUnitPath());
        map.put("unitIsValid", "T");
        return userUnitDao.querySubUserUnits(
            QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(map, pageDesc, userUnitDao.countSubUserUnits(map)), UserUnit.class));
    }
}
