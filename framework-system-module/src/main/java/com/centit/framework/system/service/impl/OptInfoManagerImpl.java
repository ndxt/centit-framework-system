package com.centit.framework.system.service.impl;

import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.basedata.OptDataScope;
import com.centit.framework.model.basedata.OptInfo;
import com.centit.framework.model.basedata.OptMethod;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("functionManager")
public class OptInfoManagerImpl implements OptInfoManager {

    @Autowired
    private OptInfoDao optInfoDao;

    @Autowired
    private OptMethodDao optMethodDao;

    @Autowired
    private OptDataScopeDao dataScopeDao;

    @Autowired
    private OsInfoDao osInfoDao;
    @Autowired
    private RolePowerDao rolePowerDao;

    @Override
    @Transactional
    public boolean hasChildren(String optId){
        return optInfoDao.countObjectByProperties(
            CollectionsOpt.createHashMap("preOptId", optId)) > 0;
    }

    @Override
    @Transactional
    public List<OptDataScope> listAllDataScope(){
        return dataScopeDao.listAllDataScope();
    }

    private void checkOptInfoProperties(OptInfo optInfo, boolean checkDown) {
        if (optInfo.getPreOptId() == null) {
            optInfo.setPreOptId("0");
        }
        if (StringUtils.isBlank(optInfo.getIsInToolbar())) {
            optInfo.setIsInToolbar("Y");
        }
        if(checkDown) {
            List<OptInfo> allSubOpts = findSubOptInfo(optInfo.getOptId());
            for (OptInfo o : allSubOpts) {
                boolean needUpdate = false;
                if (!StringUtils.equals(o.getOptType(), optInfo.getOptType())) {
                    needUpdate = true;
                    o.setOptType(optInfo.getOptType());
                }
                if ("N".equals(optInfo.getIsInToolbar())) {
                    if (!"N".equals(o.getIsInToolbar())) {
                        o.setIsInToolbar("N");
                        needUpdate = true;
                    }
                }
                if (needUpdate) {
                    optInfoDao.updateObject(new String[]{"isInToolbar", "optType"}, o);
                }
            }
        }

        List<OptInfo> allPreOpts = findPreOptInfo(optInfo.getPreOptId());
        for (OptInfo o : allPreOpts) {
            boolean needUpdate = false;
            if (!StringUtils.equals(o.getOptType(), optInfo.getOptType())) {
                needUpdate = true;
                o.setOptType(optInfo.getOptType());
            }
            if ("Y".equals(optInfo.getIsInToolbar())) {
                if (!"Y".equals(o.getIsInToolbar())) {
                    o.setIsInToolbar("Y");
                    needUpdate = true;
                }
            }
            if (needUpdate) {
                optInfoDao.updateObject(new String[]{"isInToolbar", "optType"}, o);
            }
        }

        if(StringUtils.isBlank(optInfo.getPreOptId()) || "0".equalsIgnoreCase(optInfo.getPreOptId())){
            optInfo.setPreOptId("0");
        } else {
            OptInfo parentOpt = optInfoDao.getObjectById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId("0");
            } else {
                optInfo.setTopOptId(parentOpt.getTopOptId());
                if (StringUtils.isBlank(optInfo.getOsId())) {
                    optInfo.setOsId(parentOpt.getOsId());
                }
            }
        }
        //添加校验数据
        OsInfo osInfo = null;
        if (StringUtils.isNotBlank(optInfo.getTopOptId())) {
            osInfo = osInfoDao.getOsInfoByRelOpt(optInfo.getTopOptId());
        }
        if (osInfo == null && StringUtils.isNotBlank(optInfo.getOsId())){
            osInfo = osInfoDao.getObjectById(optInfo.getOsId());
        }
        if (osInfo == null && StringUtils.isNotBlank(optInfo.getTopOptId())){
            osInfo = osInfoDao.getObjectById(optInfo.getTopOptId());
        }

        if (osInfo == null){
            throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                "Data invalid, OSInfo is not found. TopOptId=" + optInfo.getTopOptId() +"，OS_ID=" + optInfo.getOsId());
        }

        optInfo.setTopOptId(StringUtils.isBlank(osInfo.getRelOptId())?osInfo.getOsId():osInfo.getRelOptId());
        optInfo.setOsId(osInfo.getOsId());
    }

    @Override
    @Transactional
    public void saveNewOptInfo(OptInfo optInfo){
        //同步菜单上下级显示与否, 新建的菜单不会有子菜单
        checkOptInfoProperties(optInfo, false);
        optInfoDao.saveNewObject(optInfo);

        if(optInfo.getOptMethods()!=null && optInfo.getOptMethods().size()>0 ){
            // 对于显示的菜单添加显示权限
            for(OptMethod o : optInfo.getOptMethods()){
                o.setOptCode(optMethodDao.getNextOptCode());
                o.setOptId(optInfo.getOptId());
                optMethodDao.saveNewObject(o);
            }
        }
        CodeRepositoryCache.evictCache("OptInfo");
        CodeRepositoryCache.evictCache("OptMethod");
    }


    @Override
    @Transactional
    public void updateOptInfo(OptInfo optInfo) {
        OptInfo dbOptInfo = optInfoDao.getObjectById(optInfo.getOptId());
        if(dbOptInfo==null){
            return;
        }
        checkOptInfoProperties(optInfo, true);
        optInfoDao.updateOptInfo(optInfo);
        CodeRepositoryCache.evictCache("OptInfo");
    }

    @Override
    @Transactional
    public void updateOperationPower(OptInfo optInfo) {
        optInfoDao.updateObject(new String[]{"formCode", "optRoute", "optUrl", "pageType"}, optInfo);
        List<OptMethod> newOptMethods = optInfo.getOptMethods();
        List<OptMethod> oldOptMethods = optMethodDao.listOptMethodByOptID(optInfo.getOptId());
        if(newOptMethods == null || newOptMethods.size() < 1){
            optMethodDao.deleteOptMethodsByOptID(optInfo.getOptId());
            if(CollectionUtils.isNotEmpty(oldOptMethods)){
                for (OptMethod oldOptMethod : oldOptMethods) {
                    rolePowerDao.deleteRolePowersByOptCode(oldOptMethod.getOptCode());
                }
            }
        }

        Triple<List<OptMethod>, List<Pair<OptMethod,OptMethod>>, List<OptMethod>> compareMethod =
            CollectionsOpt.compareTwoList(oldOptMethods, newOptMethods, Comparator.comparing(OptMethod::getOptCode));

        if(compareMethod.getRight() != null) {
            for (OptMethod optMethod : compareMethod.getRight()) {
                optMethodDao.deleteObject(optMethod);
                rolePowerDao.deleteRolePowersByOptCode(optMethod.getOptCode());
            }
        }

        if(compareMethod.getLeft() != null){
            for(OptMethod optMethod : compareMethod.getLeft()){
                optMethod.setOptCode(optMethodDao.getNextOptCode());
                optMethod.setOptId(optInfo.getOptId());
                optMethodDao.saveNewObject(optMethod);
            }
        }

        if(compareMethod.getMiddle() != null){
            for(Pair<OptMethod, OptMethod> pair : compareMethod.getMiddle()){
                optMethodDao.updateOptMethod(pair.getRight());
            }
        }

        List<OptDataScope>  newDataScopes = optInfo.getDataScopes();

        if(newDataScopes == null || newDataScopes.size() < 1){
            dataScopeDao.deleteDataScopeOfOptID(optInfo.getOptId());
        }else {
            newDataScopes.forEach(dataScope->{
                    if(StringUtils.isBlank(dataScope.getOptScopeCode())) {
                    dataScope.setOptScopeCode(dataScopeDao.getNextOptCode());
                }});
        }

        List<OptDataScope> oldDataScopes = dataScopeDao.getDataScopeByOptID(optInfo.getOptId());
        Triple<List<OptDataScope>, List<Pair<OptDataScope, OptDataScope>>, List<OptDataScope>> compareScope =
              CollectionsOpt.compareTwoList(oldDataScopes, newDataScopes, Comparator.comparing(OptDataScope::getOptScopeCode));

        if(compareScope.getRight() != null){
            for(OptDataScope optDataScope : compareScope.getRight()){
                dataScopeDao.deleteObject(optDataScope);
            }
        }

        if(compareScope.getLeft() != null){
            for(OptDataScope optDataScope : compareScope.getLeft()){
                optDataScope.setOptId(optInfo.getOptId());
                dataScopeDao.saveNewOPtDataScope(optDataScope);
            }
        }

        if(compareScope.getMiddle() != null){
            for(Pair<OptDataScope, OptDataScope> pair : compareScope.getMiddle()){
                dataScopeDao.updateOptDataScope(pair.getRight());
            }
        }

        CodeRepositoryCache.evictCache("OptInfo");
        CodeRepositoryCache.evictCache("OptMethod");
    }

    @Transactional
    public OptInfo getOptInfoById(String optId){
        OptInfo oinfo = optInfoDao.getObjectById(optId);
        if(oinfo!=null){
            oinfo.addAllOptMethods(optMethodDao.listOptMethodByOptID(optId) );
            oinfo.addAllDataScopes(dataScopeDao.getDataScopeByOptID(optId));
        }
        return oinfo;
    }

    @Override
    @Transactional
    public void deleteOptInfoById(String optId) {
        dataScopeDao.deleteDataScopeOfOptID(optId);
        optMethodDao.deleteOptMethodsByOptID(optId);
        optInfoDao.deleteObjectById(optId);
        String sql="DELETE FROM f_rolepower WHERE OPT_CODE IN (SELECT OPT_CODE FROM f_optdef WHERE OPT_ID =?)";
        DatabaseOptUtils.doExecuteSql(optInfoDao, sql, new Object[]{optId});
        CodeRepositoryCache.evictCache("RoleInfo");
        CodeRepositoryCache.evictCache("RolePower");
        CodeRepositoryCache.evictCache("OptInfo");
    }

    @Override
    @Transactional
    public int countSubOptInfo(String optId){
        return optInfoDao.countObjectByProperties(
            CollectionsOpt.createHashMap("preOptId", optId));
    }

    @Override
    @Transactional
    public List<OptInfo> listSysAndOptPowerOpts(){
//        return optInfoDao.listObjectsByCon("  (optType='S' or optType='O')");
        return optInfoDao.listMenuByTypes("S", "O");
    }

    @Override
    @Transactional
    public List<OptInfo> listItemPowerOpts(){
//        return optInfoDao.listObjectsByCon("  optType='I'");
        return optInfoDao.listMenuByTypes("I");
    }

    /**
     * 获取用户数据权限过滤器
     * @param sUserCode sUserCode
     * @param sOptId 业务名称
     * @param sOptMethod 对应的方法名称
     * @return 过滤条件列表，null或者空位不过来
     */
    @Override
    @Transactional
    public List<String> listUserDataFiltersByOptIdAndMethod(String sUserCode, String sOptId, String sOptMethod){
        List<String> dataScopes = optInfoDao.listUserDataPowerByOptMethod(sUserCode, sOptId, sOptMethod);
        if(dataScopes==null || dataScopes.size()==0) {
          return null;
        }

        Set<String> scopeCodes = new HashSet<String>();
        for(String scopes : dataScopes){
            if(scopes==null || "null".equalsIgnoreCase(scopes) || "all".equalsIgnoreCase(scopes)) {
              return null;
            }
            String [] codes = scopes.split(",");
            for (String code : codes) {
                if(code!=null && !"".equals(code.trim())) {
                  scopeCodes.add(code.trim());
                }
            }
        }
        if(scopeCodes.size()==0) {
          return null;
        }
        return dataScopeDao.listDataFiltersByIds(scopeCodes);
    }


    @Override
    @Transactional
    public List<OptInfo> listObjectFormatTree(List<OptInfo> optInfos,boolean fillDefAndScope) {
        // 获取当前菜单的子菜单
        Iterator<OptInfo> menus = optInfos.iterator();
        List<OptInfo> parentMenu = new ArrayList<>();
        while (menus.hasNext()) {

            OptInfo optInfo = menus.next();
            //去掉级联关系后需要手动维护这个属性
            if(fillDefAndScope){
                optInfo.addAllOptMethods( optMethodDao.listOptMethodByOptID(optInfo.getOptId()));
                optInfo.addAllDataScopes( dataScopeDao.getDataScopeByOptID(optInfo.getOptId()));
            }

            boolean getParent = false;
            for (OptInfo opt : optInfos) {
                if (opt.getOptId().equals(optInfo.getPreOptId())) {
                    opt.addChild(optInfo);
                    getParent = true;
                    break;
                }
            }
            if(!getParent) {
              parentMenu.add(optInfo);
            }
        }
        return parentMenu;
    }

    @Override
    @Transactional
    public List<OptInfo> listOptWithPowerUnderUnit(String sUnitCode) {
//        List<OptInfo>  allOpts = optInfoDao.listObjectsByCon(" (optType='S' or optType='O')");
        List<OptInfo>  allOpts = optInfoDao.listMenuByTypes("S", "O");
        List<OptMethod> optDefs = optMethodDao.listOptMethodByRoleCode("G$"+sUnitCode);
        Set<OptInfo> roleOpts = new HashSet<>();

        for(OptInfo optInfo : allOpts) {
            //去掉级联关系后需要手动维护这个属性
            for(OptMethod def: optDefs){
                if(optInfo.getOptId().equals(def.getOptId())) {
                  optInfo.addOptMethod(def);
                }
            }
            if(optInfo.getOptMethods().size()>0){
                optInfo.addAllDataScopes( dataScopeDao.getDataScopeByOptID(optInfo.getOptId()));
                roleOpts.add(optInfo);
            }
        }
        Set<OptInfo> preParents  = new HashSet<OptInfo>();
        preParents.addAll(roleOpts);
        while(true){
            int parents= 0;
            Set<OptInfo> parentMenu = new HashSet<OptInfo>();
            for(OptInfo optInfo :preParents){
                for (OptInfo opt : allOpts) {
                    if (opt.getOptId().equals(optInfo.getPreOptId())) {
                        //opt.getChildren().add(optInfo);
                        parentMenu.add(opt);
                        roleOpts.add(opt);
                        parents ++;
                        break;
                    }
                }
            }
            if(parents==0) {
              break;
            }
            preParents = parentMenu;
        }
        List<OptInfo> roleOptInfos = new ArrayList<OptInfo>();
        roleOptInfos.addAll(roleOpts);
        return roleOptInfos;
    }

    @Override
    public List<OptInfo> listObjects() {
        return optInfoDao.listObjectsAll();
    }

    @Override
    public List<OptInfo> listObjects(Map<String, Object> filterMap) {
        return optInfoDao.listObjectsByProperties(filterMap);
    }

    @Override
    public OptInfo getObjectById(String optId) {
        return optInfoDao.getObjectById(optId);
    }

    private List<OptInfo> findSubOptInfo(String optId){
        List<OptInfo> result = new ArrayList<>();
        List<OptInfo> optInfos = optInfoDao.listObjectByParentOptid(optId);
        if(optInfos != null && optInfos.size() > 0){
            result.addAll(optInfos);
            for(OptInfo o : optInfos){
                result.addAll(findSubOptInfo(o.getOptId()));
            }
        }
        return result;
    }

    private List<OptInfo> findPreOptInfo(String preId){
        List<OptInfo> result = new ArrayList<>();
        OptInfo optInfo = optInfoDao.getObjectById(preId);
        if(optInfo != null){
            result.add(optInfo);
            result.addAll(findPreOptInfo(optInfo.getPreOptId()));
        }
        return result;
    }


    @Override
    @Transactional
    public List<OptInfo> listUserAllPower(String userCode, boolean asAdmin){
        List<OptInfo> preOpts = optInfoDao.listParentMenuFunc();
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls = optInfoDao.listUserAllSubMenu(userCode, optType);
        List<OptInfo> menuFunsByUser = DBPlatformEnvironment.getMenuFuncs(preOpts,  ls);
        return menuFunsByUser;
    }

    @Override
    public List<OptInfo> listFromParent(Map<String, Object> filterMap) {
        return optInfoDao.listFromParent(filterMap);
    }

    @Override
    public List<OptInfo> listAllOptInfoByUnit(String topUnit) {
        return optInfoDao.listAllOptInfoByUnit(topUnit);
    }

    @Override
    public List<OptInfo> listUserOptinfos(String topUnit, String userCode) {
        List<OptInfo> preOpts = optInfoDao.listParentMenuFunc();
        List<OptInfo> ls;
        if (StringUtils.isBlank(topUnit) || GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)) {
            ls = optInfoDao.listUserAllSubMenu(userCode, "o");
        } else {
            ls = optInfoDao.listUserOptinfos(topUnit, userCode);
        }
        List<OptInfo> menuFunsByUser =  DBPlatformEnvironment.getMenuFuncs(preOpts, ls);
        return DBPlatformEnvironment.getFormatMenuTree(menuFunsByUser);
    }
}
