package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.SysRoleManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysRoleManager")
public class SysRoleManagerImpl implements SysRoleManager {

    public static Logger logger = LoggerFactory.getLogger(SysRoleManagerImpl.class);

    @Resource
    @NotNull
    private OptInfoDao optInfoDao;

    @Resource
    @NotNull
    private OptMethodDao optMethodDao;

    @Resource
    @NotNull
    private RolePowerDao rolePowerDao;

    @Resource
    @NotNull
    protected RoleInfoDao roleInfoDao;

    @Resource
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

        return rolePowerDao.listObjects(filterMap);
    }

    @Value("${framework.roleinfo.id.generator:}")
    protected String roleIdFormat;

    @Override
    @CacheEvict(value="RoleInfo",key="'roleCodeMap'")
    @Transactional
    public Serializable saveNewRoleInfo(RoleInfo o){
        if(StringUtils.isBlank(o.getRoleCode())) {
            String roleCode = roleInfoDao.getNextKey();
            if (StringUtils.isNotBlank(roleIdFormat)) {
                //{"prefix":"U","length":8,"pad":"0"}
                JSONObject idFormat = (JSONObject) JSON.parse(roleIdFormat);
                if (idFormat != null) {
                    roleCode = StringBaseOpt.midPad(roleCode,
                        NumberBaseOpt.castObjectToInteger(idFormat.get("length"), 1),
                        idFormat.getString("prefix"),
                        idFormat.getString("pad"));
                }
            }
            o.setRoleCode(roleCode);
        }
        roleInfoDao.saveNewObject(o);
        return o.getRoleCode();
    }

    // 获取菜单TREE
    @Transactional
    public List<VOptTree> getVOptTreeList() {
        return roleInfoDao.getVOptTreeList();
    }

    @Transactional
    public List<RolePower> listAllRolePowers() {
        return rolePowerDao.listObjectsAll();
    }

    @Transactional
    public List<OptMethod> listAllOptMethods() {
         return optMethodDao.listObjects();
    }

    private List<OptMethodUrlMap> listAllOptMethodUrlMap() {
         return optInfoDao.listAllOptMethodUrlMap();
    }

    @Override
    @CacheEvict(value="RoleInfo",allEntries = true)
    @Transactional
    public void updateRoleInfo(RoleInfo o) {
        roleInfoDao.updateRole(o);
    }

    @Override
    @CacheEvict(value="RoleInfo",allEntries = true)
    @Transactional
    public List<RolePower> updateRolePower(RoleInfo o) {
        roleInfoDao.updateRole(o);
        List<RolePower> newRPs = o.getRolePowers();

        List<RolePower> rps = rolePowerDao.listRolePowersByRoleCode(o.getRoleCode());

        if(newRPs == null || newRPs.size()<1) {
          rolePowerDao.deleteRolePowersByRoleCode(o.getRoleCode());
          return rps;
        }

        for(RolePower rp : newRPs){
            rp.setRoleCode(o.getRoleCode());
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
                    List<RoleInfo> roleInfos = roleInfoDao.listObjects(
                        QueryUtils.createSqlParamsMap("unitCode", unitCode, "roleType", "D"));
                    for(RoleInfo roleInfo : roleInfos){
                        rolePowerDao.deleteObjectById(new RolePowerId(roleInfo.getRoleCode(), optCode));
                    }
                }
            }
        }
        if( forUpdate.getLeft() != null){
            for(RolePower rp : forUpdate.getLeft()){
                rolePowerDao.saveNewRolePower(rp);
            }
        }

        if(forUpdate.getMiddle() != null){
            for(Pair<RolePower, RolePower> rp : forUpdate.getMiddle()){
                RolePower oldRolePower = rp.getLeft();
                RolePower newRolePower = rp.getRight();
                oldRolePower.copyNotNullProperty(newRolePower);
                rolePowerDao.updateRolePower(oldRolePower);
            }
        }
        return rps;
    }

    @Override
    @CacheEvict(value="RoleInfo",allEntries = true)
    @Transactional
    public void deleteRoleInfo(String roleCode){
        rolePowerDao.deleteRolePowersByRoleCode(roleCode);
        roleInfoDao.deleteObjectById(roleCode);

    }

    @Override
    @Transactional
    public RoleInfo getRoleInfo(String roleCode){
        RoleInfo rf = roleInfoDao.getObjectById(roleCode);
        rf.addAllRolePowers( rolePowerDao.listRolePowersByRoleCode(roleCode) );
        return rf;

    }

    @Override
    @Transactional
    public List<RoleInfo> listObjects(Map<String, Object> filterMap) {

        return roleInfoDao.listObjects(filterMap);
    }

    @Override
    @Transactional
    public List<RoleInfo> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return roleInfoDao.pageQuery(
            QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(filterMap,pageDesc,roleInfoDao.pageCount(filterMap)),RoleInfo.class));
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
        return userRoleDao.pageCount(QueryUtils.createSqlParamsMap("roleCode",roleCode));
    }

    @Override
    @Transactional
    public boolean judgeSysRoleNameExist(String roleName, String roleCode, String unitCode){

        Map<String, Object> filterMap = new HashMap<>(4);
        //系统角色
      if(unitCode == null) {
        filterMap.put("NP_ALL", "true");
        //部门角色
      }else{
        filterMap.put("roleType", "D");
        filterMap.put("unitCode", unitCode);
      }
      filterMap.put("roleNameEq", roleName);

      List<RoleInfo> roleInfos = roleInfoDao.listObjects(filterMap);
      return (roleInfos==null || roleInfos.size() == 0) ||
            (roleCode!=null && roleCode.equals(roleInfos.get(0).getRoleCode()));
    }
}
