package com.centit.framework.system.service.impl;

import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.system.dao.OptDataScopeDao;
import com.centit.framework.system.dao.OptInfoDao;
import com.centit.framework.system.dao.OptMethodDao;
import com.centit.framework.system.po.OptDataScope;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.support.algorithm.CollectionsOpt;
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

    @Override
    @Transactional
    public boolean hasChildren(String optId){
        return optInfoDao.countObject(
            CollectionsOpt.createHashMap("optId", optId)) > 0;
    }

    @Override
    @Transactional
    public List<OptDataScope> listAllDataScope(){
        return dataScopeDao.listAllDataScope();
    }

    @Override
    @Transactional
    public void saveNewOptInfo(OptInfo optInfo){
        //同步菜单上下级显示与否
        syncState(optInfo);
        OptInfo parentOpt = optInfoDao.getObjectById(optInfo.getPreOptId());
        if (parentOpt == null) {
            optInfo.setPreOptId("0");
        }

        optInfoDao.saveNewObject( optInfo );

        if(optInfo.getOptMethods()!=null && optInfo.getOptMethods().size()>0 ){
            // 对于显示的菜单添加显示权限
            for(OptMethod o : optInfo.getOptMethods()){
                o.setOptCode(optMethodDao.getNextOptCode());
                o.setOptId(optInfo.getOptId());
                optMethodDao.saveNewObject(o);
            }
        }else if (!"W".equals(optInfo.getOptType())) {
            OptMethod createDef = new OptMethod();
            createDef.setOptCode(optMethodDao.getNextOptCode());
            createDef.setOptId(optInfo.getOptId());
            createDef.setOptName("查看");
            createDef.setOptMethod("search");
            createDef.setOptUrl("/changeme");
            createDef.setOptReq("CRUD");
            createDef.setOptDesc("查看（系统默认）");
            optMethodDao.saveNewObject(createDef);
        }
        CodeRepositoryCache.evictCache("OptInfo");
        CodeRepositoryCache.evictCache("OptMethod");
    }


    @Override
    @Transactional
    public void updateOptInfo(OptInfo optInfo) {
        syncState(optInfo);
        optInfoDao.updateOptInfo(optInfo);
        CodeRepositoryCache.evictCache("OptInfo");
    }

    @Override
    @Transactional
    public void updateOperationPower(OptInfo optInfo) {

        optInfoDao.updateOptInfo(optInfo);

        List<OptMethod> newOptMethods = optInfo.getOptMethods();

        if(newOptMethods == null || newOptMethods.size() < 1){
            optMethodDao.deleteOptMethodsByOptID(optInfo.getOptId());
        }

        List<OptMethod> oldOptMethods = optMethodDao.listOptMethodByOptID(optInfo.getOptId());

        Triple<List<OptMethod>, List<Pair<OptMethod,OptMethod>>, List<OptMethod>> compareMethod =
            CollectionsOpt.compareTwoList(oldOptMethods, newOptMethods, Comparator.comparing(OptMethod::getOptId));

        if(compareMethod.getRight() != null) {
            for (OptMethod optMethod : compareMethod.getRight()) {
                optMethodDao.deleteObject(optMethod);
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
                pair.getLeft().copyNotNullProperty(pair.getRight());
                optMethodDao.updateOptMethod(pair.getLeft());
            }
        }

        List<OptDataScope>  newDataScopes = optInfo.getDataScopes();

        if(newDataScopes == null || newDataScopes.size() < 1){
            dataScopeDao.deleteDataScopeOfOptID(optInfo.getOptId());
        }

        List<OptDataScope> oldDataScopes = dataScopeDao.getDataScopeByOptID(optInfo.getOptId());

        Triple<List<OptDataScope>, List<Pair<OptDataScope, OptDataScope>>, List<OptDataScope>> compareScope =
              CollectionsOpt.compareTwoList(oldDataScopes, newDataScopes, Comparator.comparing(OptDataScope::getOptId));

        if(compareScope.getRight() != null){
            for(OptDataScope optDataScope : compareScope.getRight()){
                dataScopeDao.deleteObject(optDataScope);
            }
        }

        if(compareScope.getLeft() != null){
            for(OptDataScope optDataScope : compareScope.getLeft()){
                optDataScope.setOptScopeCode(dataScopeDao.getNextOptCode());
                optDataScope.setOptId(optInfo.getOptId());
                dataScopeDao.saveNewOPtDataScope(optDataScope);
            }
        }

        if(compareScope.getMiddle() != null){
            for(Pair<OptDataScope, OptDataScope> pair : compareScope.getMiddle()){
                pair.getLeft().copyNotNullProperty(pair.getRight());
                dataScopeDao.updateOptDataScope(pair.getLeft());
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
        CodeRepositoryCache.evictCache("OptInfo");
    }

    @Override
    @Transactional
    public void deleteOptInfo(OptInfo optinfo){
        deleteOptInfoById(optinfo.getOptId());
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
        return optInfoDao.listObjects(filterMap);
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

    private void syncState(OptInfo optInfo){
        if("N".equals(optInfo.getIsInToolbar())){
            List<OptInfo> optInfos = findSubOptInfo(optInfo.getOptId());
            for(OptInfo o : optInfos){
                o.setIsInToolbar("N");
                optInfoDao.updateOptInfo(o);
            }
        }else{
            List<OptInfo> optInfos = findPreOptInfo(optInfo.getPreOptId());
            for(OptInfo o : optInfos){
                o.setIsInToolbar("Y");
                optInfoDao.updateOptInfo(o);
            }
        }
        List<OptInfo> optInfos = findSubOptInfo(optInfo.getOptId());
        optInfos.addAll(findPreOptInfo(optInfo.getPreOptId()));
        for(OptInfo o : optInfos) {
          o.setOptType(optInfo.getOptType());
          optInfoDao.updateOptInfo(o);
        }
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

}
