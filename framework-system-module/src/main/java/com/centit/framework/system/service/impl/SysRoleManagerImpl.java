package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.model.basedata.*;
import com.centit.framework.security.CentitSecurityMetadata;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.service.SysRoleManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Service("sysRoleManager")
public class SysRoleManagerImpl implements SysRoleManager {

    public static Logger logger = LoggerFactory.getLogger(SysRoleManagerImpl.class);

    @Autowired
    @NotNull
    private OptInfoDao optInfoDao;

    @Autowired
    @NotNull
    private OptMethodDao optMethodDao;

    @Autowired
    @NotNull
    private RolePowerDao rolePowerDao;

    @Autowired
    @NotNull
    protected RoleInfoDao roleInfoDao;

    @Autowired
    @NotNull
    private UserRoleDao userRoleDao;

    // 各种角色代码获得该角色的操作权限 1对多
    @Transactional
    public List<RolePower> getRolePowers(String rolecode) {
        return rolePowerDao.listRolePowersByRoleCode(rolecode);
    }

    @Override
    @Transactional
    public List<RolePower> getRolePowersByDefCode(String optCode) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("optCode", optCode);

        return rolePowerDao.listObjectsByProperties(filterMap);
    }

    @Value("${framework.roleinfo.id.generator:}")
    protected String roleIdFormat;

    @Override
    @Transactional
    public Serializable saveNewRoleInfo(RoleInfo o){
//        if(StringUtils.isBlank(o.getRoleCode()) && !"default".equals(roleIdFormat)) {
//            String roleCode =
//                PersistenceUtils.makeIdByFormat(roleInfoDao.getNextKey(),roleIdFormat,
//                    "R",8,"0");
//            o.setRoleCode(roleCode);
//        }
        roleInfoDao.saveNewObject(o);
        CodeRepositoryCache.evictCache("RoleInfo");
        return o.getRoleCode();
    }

    @Transactional
    public List<RolePower> listAllRolePowers() {
        return rolePowerDao.listObjectsAll();
    }

    @Transactional
    public List<OptMethod> listAllOptMethods() {
         return optMethodDao.listObjectsAll();
    }

    private List<OptMethodUrlMap> listAllOptMethodUrlMap() {
         return optInfoDao.listAllOptMethodUrlMap();
    }

    @Override
    @Transactional
    public void updateRoleInfo(RoleInfo o) {
        if(StringUtils.equalsAny(o.getRoleCode(),
            SecurityContextUtils.ANONYMOUS_ROLE_CODE,
            SecurityContextUtils.PUBLIC_ROLE_CODE) ){
            return;
        }
        roleInfoDao.updateRole(o);
        CodeRepositoryCache.evictCache("RoleInfo");
    }

    @Override
    @Transactional
    public List<RolePower> updateRolePower(RoleInfo o) {
        roleInfoDao.updateRole(o);
        List<RolePower> newRPs = o.getRolePowers();
        List<RolePower> rps = rolePowerDao.listRolePowersByRoleCode(o.getRoleCode());

        if(newRPs == null || newRPs.size()<1) {
          rolePowerDao.deleteRolePowersByRoleCode(o.getRoleCode());
          return rps;
        }
        updateRolePower(o, newRPs, rps);
        CodeRepositoryCache.evictCache("RoleInfo");
        CodeRepositoryCache.evictCache("RolePower");
        return rps;
    }

    @Override
    @Transactional
    public List<RolePower> updateRolePower(RoleInfo roleInfo, String topUnit) {
        boolean isAPCode = StringUtils.equalsAny(roleInfo.getRoleCode(),
            SecurityContextUtils.ANONYMOUS_ROLE_CODE,
            SecurityContextUtils.PUBLIC_ROLE_CODE);
        /*if (!isAPCode){
            roleInfoDao.updateRole(roleInfo);
        }*/
        List<RolePower> newRPs = roleInfo.getRolePowers();
        List<RolePower> rps = isAPCode ? rolePowerDao.listRolePowerByTopUnitAndRoleCode(topUnit,roleInfo.getRoleCode()):
            rolePowerDao.listRolePowersByRoleCode(roleInfo.getRoleCode());
        if(CollectionUtils.sizeIsEmpty(newRPs)) {
            rps.forEach(rp->rolePowerDao.deleteObjectById(rp.getId()));
            return rps;
        }
        updateRolePower(roleInfo, newRPs, rps);
        CodeRepositoryCache.evictCache("RoleInfo");
        CodeRepositoryCache.evictCache("RolePower");
        if(DBPlatformEnvironment.SYSTEM.equals(topUnit)) {
            CodeRepositoryCache.evictCache("SystemOpt");
        }
        CentitSecurityMetadata.evictCache(1);
        return rps;
    }

    /**
     * 更新 rolePower 详情
     * @param roleInfo
     * @param newRPs
     * @param rps
     */
    private void updateRolePower(RoleInfo roleInfo, List<RolePower> newRPs, List<RolePower> rps) {
        for(RolePower rp : newRPs){
            rp.setRoleCode(roleInfo.getRoleCode());
        }

        Triple<List<RolePower>, List<Pair<RolePower,RolePower>>, List<RolePower>>
            forUpdate =  CollectionsOpt.compareTwoList(rps, newRPs, Comparator.comparing(RolePower::getOptCode));

        if( forUpdate.getRight() != null){
            for(RolePower rp : forUpdate.getRight()){
                rolePowerDao.deleteObjectById(new RolePowerId(rp.getRoleCode(), rp.getOptCode()));
                //如果删除部门操作权限,则要同步删除该部门下角色对应的操作权限
                if(rp.getRoleCode().startsWith("G$")){
                    String optCode = rp.getOptCode();
                    String unitCode = rp.getRoleCode().substring(2);
                    List<RoleInfo> roleInfos = roleInfoDao.listObjectsByProperties(
                        CollectionsOpt.createHashMap("unitCode", unitCode, "roleType", "D"));
                    for(RoleInfo ri : roleInfos){
                        rolePowerDao.deleteObjectById(new RolePowerId(ri.getRoleCode(), optCode));
                    }
                }
            }
        }
        if( forUpdate.getLeft() != null){
            for(RolePower rp : forUpdate.getLeft()){

                rolePowerDao.saveNewRolePower(rp);
            }
        }
        //有用信息只有主键 所以没有必要修改
        if(forUpdate.getMiddle() != null){
            for(Pair<RolePower, RolePower> rp : forUpdate.getMiddle()){
                RolePower oldRolePower = rp.getLeft();
                RolePower newRolePower = rp.getRight();
                if(!StringUtils.equals(oldRolePower.getOptScopeCodes(),
                    newRolePower.getOptScopeCodes())) {
                    rolePowerDao.updateObject(
                        CollectionsOpt.createList("updator", "updateDate"),
                        newRolePower);
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleInfo(String roleCode){
        rolePowerDao.deleteRolePowersByRoleCode(roleCode);
        roleInfoDao.deleteObjectById(roleCode);
        CodeRepositoryCache.evictCache("RoleInfo");
    }

    @Override
    @Transactional
    public RoleInfo getRoleInfo(String roleCode){
        RoleInfo rf = roleInfoDao.getObjectById(roleCode);
        if(rf!=null) {
            rf.addAllRolePowers(rolePowerDao.listRolePowersByRoleCode(roleCode));
        }
        return rf;

    }

    @Override
    @Transactional
    public List<RoleInfo> listObjects(Map<String, Object> filterMap) {
        return roleInfoDao.listObjectsByProperties(filterMap);
    }

    @Override
    @Transactional
    public List<RoleInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return roleInfoDao.listObjectsByProperties(filterMap,pageDesc);
    }

    @Override
    @Transactional
    public RoleInfo getObjectById(String roleCode) {
        return roleInfoDao.getObjectById(roleCode);
    }

    @Override
    @Transactional
    public int countRoleUserSum(String roleCode){
//        return roleInfoDao.countRoleUserSum(roleCode);
        return userRoleDao.countObjectByProperties(CollectionsOpt.createHashMap("roleCode",roleCode));
    }

    /**
     * 检查 角色名称是可以用
     * @param roleName 角色名称 角色名称
     * @param roleCode 角色代码 （新增时设为null）
     * @param unitCode 部门代码 （系统角色设为null）
     * @return true 不冲突 可以用 false 冲突 不可用
     */
    @Override
    @Transactional
    public boolean judgeSysRoleNameCanBeUsed(String roleName, String roleCode, String unitCode){
        Map<String, Object> filterMap = new HashMap<>(4);
        //系统角色
        if(unitCode == null) {
            filterMap.put("NP_ALL", "true");
            //部门角色
        }else{
            filterMap.put("NP_OWNER", "D/S");
            filterMap.put("unitCode", unitCode);
        }
        filterMap.put("roleNameEq", roleName);

        List<RoleInfo> roleInfos = roleInfoDao.listObjectsByProperties(filterMap);
        return roleInfos==null || roleInfos.size() == 0 ||
               StringUtils.equals(roleCode, roleInfos.get(0).getRoleCode());
    }

    @Override
    public List<RoleInfo> listRoleInfoByOptCode(String optCode) {
        return roleInfoDao.listRoleInfoByOptCode(optCode);
    }

    @Override
    public JSONArray listRoleInfoAndPowerByOptCode(String optCode) {

        return rolePowerDao.listRoleInfoAndPowerByOptCode(optCode);
    }

    @Override
    @Transactional
    public void updateRolePower(String optCode, String roleCode) {
        rolePowerDao.deleteRolePowersByOptCode(optCode);
       if (StringUtils.isBlank(roleCode)){
           CodeRepositoryCache.evictCache("RolePower");
           return;
       }
        Arrays.stream(roleCode.split(",")).forEach(singleRoleCode->
            rolePowerDao.saveNewRolePower(new RolePower(new RolePowerId(singleRoleCode,optCode))));
        CodeRepositoryCache.evictCache("RolePower");

    }

    @Override
    @Transactional
    public void updateRolePowersByOptCode(String optCode, List<RolePower> rolePowers) {
        if (StringUtils.isBlank(optCode)){
            return;
        }
        rolePowerDao.deleteRolePowersByOptCode(optCode);
        if (CollectionUtils.sizeIsEmpty(rolePowers)){
            CodeRepositoryCache.evictCache("RolePower");
            return;
        }
        rolePowers.forEach(rolePower -> rolePowerDao.saveNewRolePower(rolePower));
        CodeRepositoryCache.evictCache("RolePower");
        CentitSecurityMetadata.evictCache(1);
    }
}
