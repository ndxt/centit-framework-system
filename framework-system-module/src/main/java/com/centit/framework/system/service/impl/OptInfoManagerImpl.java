package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.OptDataScopeDao;
import com.centit.framework.system.dao.OptInfoDao;
import com.centit.framework.system.dao.OptMethodDao;
import com.centit.framework.system.dao.RolePowerDao;
import com.centit.framework.system.po.OptDataScope;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.service.OptInfoManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service("functionManager")
public class OptInfoManagerImpl implements OptInfoManager {

    @Resource
    @NotNull
    protected OptInfoDao optInfoDao;

    @Resource(name = "optMethodDao")
    @NotNull
    protected OptMethodDao optMethodDao;


    @Resource(name = "optDataScopeDao")
    @NotNull
    protected OptDataScopeDao dataScopeDao;

    @Resource(name = "rolePowerDao")
    @NotNull
    protected RolePowerDao rolePowerDao;


    @Override
    @Transactional(readOnly = true)
    public Map<String, OptInfo> listObjectToOptRepo() {
        Map<String, OptInfo> optRepo = new HashMap<>();
        List<OptInfo> optList = listObjects();
        if (optList != null) {
            for (OptInfo optinfo : optList) {
                optRepo.put(optinfo.getOptId(), optinfo);
            }
        }

        return optRepo;
    }

    @Override
    @CacheEvict(value="OptInfo",allEntries = true)
    @Transactional
    public void updateOptInfoProperties(OptInfo optinfo){
        optInfoDao.mergeObject(optinfo);
    }

    @Override
    @Transactional
    public boolean hasChildren(String optId){
        return optInfoDao.countChildrenSum(optId) > 0;
    }

    @Override
    @CacheEvict(value="OptInfo",allEntries = true)
    @Transactional
    public void saveNewOptInfo(OptInfo optInfo){

        syncState(optInfo);
        // 父级url必须设成...
        OptInfo parentOpt = optInfoDao.getObjectById(optInfo.getPreOptId());
        if (null != parentOpt) {
            if(!"...".equals(parentOpt.getOptRoute())){
                parentOpt.setOptRoute("...");
                optInfoDao.mergeObject(parentOpt);
            }
        }else{
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
            createDef.setOptName("新建");
            createDef.setOptUrl("/");
            createDef.setOptReq("C");
            createDef.setOptDesc("新建（系统默认）");
            optMethodDao.saveNewObject(createDef);

            OptMethod updateDef = new OptMethod();
            updateDef.setOptCode(optMethodDao.getNextOptCode());
            updateDef.setOptId(optInfo.getOptId());
            updateDef.setOptName("编辑");
            updateDef.setOptUrl("/*");
            updateDef.setOptReq("U");
            updateDef.setOptDesc("编辑（系统默认）");
            optMethodDao.saveNewObject(updateDef);

            OptMethod deleteDef = new OptMethod();
            deleteDef.setOptCode(optMethodDao.getNextOptCode());
            deleteDef.setOptId(optInfo.getOptId());
            deleteDef.setOptName("删除");
            deleteDef.setOptUrl("/*");
            deleteDef.setOptReq("D");
            deleteDef.setOptDesc("删除（系统默认）");
            optMethodDao.saveNewObject(deleteDef);
        }
    }


    @Override
    @CacheEvict(value="OptInfo",allEntries = true)
    @Transactional
    public void updateOptInfo(OptInfo optInfo) {

        syncState(optInfo);

        optInfoDao.mergeObject(optInfo);

    }

    @Override
    @CacheEvict(value="OptInfo",allEntries = true)
    @Transactional
    public Map<String, List> updateOperationPower(OptInfo optInfo) {

//        syncState(optInfo);
//
//        optInfoDao.mergeObject(optInfo);

        Map<String, List> result = new HashMap<>();

        List<OptMethod>  newOpts = optInfo.getOptMethods();

        if(newOpts.size()>0 ){
            // 对于显示的菜单添加显示权限
            for(OptMethod o : newOpts){
                if(StringUtils.isBlank(o.getOptCode())){
                    o.setOptCode(optMethodDao.getNextOptCode());
                }
                o.setOptId(optInfo.getOptId());
            }
        }

        List<OptMethod> oldOpts = optMethodDao.listOptMethodByOptID(optInfo.getOptId());
        List<OptMethod> optMethods = new ArrayList<>();
        for(OptMethod o : oldOpts){
            OptMethod optMethod = new OptMethod();
            BeanUtils.copyProperties(o, optMethod);
            optMethods.add(optMethod);
        }
        result.put("methods", optMethods);

        if( oldOpts != null){
            for(OptMethod o : oldOpts){
                if(! newOpts.contains(o)){
                    optMethodDao.deleteObject(o);
                    rolePowerDao.deleteRolePowersByOptCode(o.getOptCode());
                }
            }
        }

        for(OptMethod o : newOpts){
            optMethodDao.mergeObject(o);
        }

        List<OptDataScope>  newDataScopes = optInfo.getDataScopes();
        if(newDataScopes.size()>0 ){
            // 对于显示的菜单添加显示权限
            for(OptDataScope s : newDataScopes){
                if(StringUtils.isBlank(s.getOptScopeCode())){
                    s.setOptScopeCode(dataScopeDao.getNextOptCode());
                }
                s.setOptId(optInfo.getOptId());
            }
        }

        List<OptDataScope> oldDataScopes = dataScopeDao.getDataScopeByOptID(optInfo.getOptId());
        List<OptDataScope> dataScopes = new ArrayList<>();
        for(OptDataScope o : oldDataScopes){
            OptDataScope optDataScope = new OptDataScope();
            BeanUtils.copyProperties(o, optDataScope);
            dataScopes.add(optDataScope);
        }
        result.put("scopes", dataScopes);

        if( oldDataScopes != null){
            for(OptDataScope s : oldDataScopes){
                if(! newDataScopes.contains(s)){
                    dataScopeDao.deleteObject(s);
                }
            }
        }

        for(OptDataScope s : newDataScopes){
            dataScopeDao.mergeObject(s);
        }
        return result;
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
    @CacheEvict(value="OptInfo",allEntries = true)
    @Transactional
    public void deleteOptInfoById(String optId) {
        dataScopeDao.deleteDataScopeOfOptID(optId);
        optMethodDao.deleteOptMethodsByOptID(optId);
        optInfoDao.deleteObjectById(optId);
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
    public List<String> listUserDataFiltersByOptIDAndMethod(String sUserCode, String sOptId, String sOptMethod){

        List<String> dataScopes = optInfoDao.listUserDataPowerByOptMethod(sUserCode, sOptId, sOptMethod);

        if(dataScopes==null || dataScopes.size()==0)
            return null;

        Set<String> scopeCodes = new HashSet<String>();
        for(String scopes : dataScopes){
            if(scopes==null || "null".equalsIgnoreCase(scopes) || "all".equalsIgnoreCase(scopes))
                return null;
            String [] codes = scopes.split(",");
            for (String code : codes) {
                if(code!=null && !"".equals(code.trim()))
                    scopeCodes.add(code.trim());
            }
        }
        if(scopeCodes.size()==0)
            return null;

        return dataScopeDao.listDataFiltersByIds(scopeCodes);
    }


    @Override
    @Transactional
    public List<OptInfo> listObjectFormatTree(List<OptInfo> optInfos,boolean fillDefAndScope) {
        // 获取当前菜单的子菜单
        Iterator<OptInfo> menus = optInfos.iterator();
        List<OptInfo> parentMenu = new ArrayList<OptInfo>();
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
            if(!getParent)
                parentMenu.add(optInfo);
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
                if(optInfo.getOptId().equals(def.getOptId()))
                    optInfo.addOptMethod(def);
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
            if(parents==0)
                break;
            preParents = parentMenu;
        }
        List<OptInfo> roleOptInfos = new ArrayList<OptInfo>();
        roleOptInfos.addAll(roleOpts);
        return roleOptInfos;
    }

    @Override
    @Transactional
    public List<OptInfo> getFunctionsByRoleCode(String unitRoleCode) {
//        String hql="From OptInfo where (optId in "
//                    + "(Select optId From OptMethod where optCode in"
//                    +     " (select id.optCode from RolePower  where id.roleCode=?) )) "
//                + "and (optType='S' or optType='O')";
//        return optInfoDao.listObjectsByRoleCode(unitRoleCode );//zou_wy
        return new ArrayList<>();
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
        List<OptInfo> optInfos = optInfoDao.listObjectByProperty("preOptId",optId);
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
                optInfoDao.mergeObject(o);
            }
        }else{
            List<OptInfo> optInfos = findPreOptInfo(optInfo.getPreOptId());
            for(OptInfo o : optInfos){
                o.setIsInToolbar("Y");
                optInfoDao.mergeObject(o);
            }
        }
    }

}
