package com.centit.framework.staticsystem.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.*;
import com.centit.framework.model.security.CentitPasswordEncoder;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.model.security.OptTreeNode;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.CachedObject;
import com.centit.support.common.ListAppendMap;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;

import java.util.*;

public abstract class AbstractStaticPlatformEnvironment
    implements PlatformEnvironment {

    /**
     * 数据字典列表
     */
    public CachedObject<List<OptDataScope>> optDataScopes  =
        new CachedObject<>(this::loadOptDataScope,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    /**数据字典**/
    public CachedObject<List<DataCatalog>> catalogRepo  =
        new CachedObject<>(this::loadAllDataCatalog,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    public CachedObject<List<RolePower>> allRolePower =
        new CachedObject<>(this::loadAllRolePower,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    public CachedObject<List<OptMethod>> allOptMethod =
        new CachedObject<>(this::loadAllOptMethod,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    public CachedObject<List<UserRole>> allUserRoleRepo =
        new CachedObject<>(this::loadAllUserRole,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    public CachedObject<List<UserUnit>> allUserUnitRepo =
        new CachedObject<>(this::loadAllUserUnit,
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    public CachedObject<List<CentitUserDetails>> allUserDetailsRepo =
        new CachedObject<>(()-> { this.reloadPlatformData(); return null;},
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    protected CentitPasswordEncoder passwordEncoder;

    public void setPasswordEncoder(CentitPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    protected abstract void reloadPlatformData();

    @SuppressWarnings("unchecked")
    protected void organizePlatformData() {

        for (RoleInfo ri : CodeRepositoryCache.roleInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)) {
            for (RolePower rp : CodeRepositoryCache.rolePowerMap
                .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT).get(ri.getRoleCode())) {
                    ri.addRolePower(rp);
            }
        }

        List<UserInfo> userinfos = CodeRepositoryCache.userInfoRepo.
            getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT).getListData();
        List<CentitUserDetails> userDetails = new ArrayList<>(userinfos.size());
        List<UserUnit> uus = allUserUnitRepo.getCachedTarget();

        Map<String, ? extends RoleInfo> codeToRoleMap = CollectionsOpt.createHashMap(
            CodeRepositoryCache.roleInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT),
            RoleInfo::getRoleCode);

        for (UserInfo ui : userinfos) {
            List<RoleInfo> roles = new ArrayList<>();
            Map<String, String> userOptList = new HashMap<>();
            for (UserRole ur : allUserRoleRepo.getCachedTarget()) {
                if (StringUtils.equals(ur.getUserCode(), ui.getUserCode())) {
                    RoleInfo ri = codeToRoleMap.get(ur.getRoleCode());
                    if (ri != null) {
                        roles.add(ri);
                        for (RolePower rp : ri.getRolePowers()) {
                            OptMethod om = CodeRepositoryCache.optMethodRepo
                                .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)
                                .getAppendMap().get(rp.getOptCode());
                            if (om != null && StringUtils.isNotBlank(om.getOptMethod())) {
                                //om.getOptCode()
                                userOptList.put(om.getOptId() + "-" + om.getOptMethod(), om.getOptCode());
                            }
                        }
                    }
                }
            }

            CentitUserDetails ud = new CentitUserDetails();
            ud.setUserInfo(ui);
            ud.mapAuthoritiesByRoles(roles);
            ud.setUserOptList(userOptList);
            List<UserUnit> uulist = new ArrayList<>(10);
            for(UserUnit uu : uus) {
                if (StringUtils.equals(uu.getUserCode(), ui.getUserCode())) {
                    uulist.add(uu);
                }
            }
            ud.setUserUnits(uulist);
            userDetails.add(ud);
        }
        allUserDetailsRepo.setFreshData(userDetails);
    }

    @Override
    public boolean checkUserPassword(String userCode,String userPassword){
        UserInfo ui = CodeRepositoryCache.userInfoRepo
            .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)
            .getAppendMap().get(userCode);
        if(ui == null) {
            return false;
        }
        return passwordEncoder.isPasswordValid(ui.getUserPin(), userPassword, userCode);
    }

    private static List<OptInfo> getMenuFuncs(List<OptInfo> preOpts, List<OptInfo> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<OptInfo> opts = new ArrayList<>();

        for (OptInfo opm : ls) {
            opts.add(opm);
            for (int i = 0; i < preOpts.size(); i++) {
                if (opm.getPreOptId() != null && opm.getPreOptId().equals(preOpts.get(i).getOptId())) {
                    isNeeds[i] = true;
                    break;
                }
            }
        }

        List<OptInfo> needAdd = new ArrayList<>();
        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                needAdd.add(preOpts.get(i));
            }
        }

        boolean isNeeds2[] = new boolean[preOpts.size()];
        while (true) {
            int nestedMenu = 0;
            for (int i = 0; i < preOpts.size(); i++)
                isNeeds2[i] = false;

            for (int i = 0; i < needAdd.size(); i++) {
                for (int j = 0; j < preOpts.size(); j++) {
                    if (!isNeeds[j] && needAdd.get(i).getPreOptId() != null
                        && needAdd.get(i).getPreOptId().equals(preOpts.get(j).getOptId())) {
                        isNeeds[j] = true;
                        isNeeds2[j] = true;
                        nestedMenu++;
                        break;
                    }
                }
            }
            if (nestedMenu == 0)
                break;

            needAdd.clear();
            for (int i = 0; i < preOpts.size(); i++) {
                if (isNeeds2[i]) {
                    needAdd.add(preOpts.get(i));
                }
            }

        }

        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                opts.add(preOpts.get(i));
            }
        }
        return opts;
    }

    /**
     * 将菜单列表组装为树状
     * @param optInfos 菜单列表
     * @return 树状菜单列表
     */
    private List<OptInfo> formatMenuTree(List<OptInfo> optInfos) {
        Iterator<OptInfo> menus = optInfos.iterator();

        List<OptInfo> parentMenu = new ArrayList<>();
        while (menus.hasNext()) {
            OptInfo optInfo = menus.next();
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

    /**
     * 将菜单列表组装为树状
     * @param optInfos 菜单列表
     * @param superOptId 顶级菜单ID 不为空时返回该菜单的下级菜单
     * @return 树状菜单列表
     */
    private List<OptInfo> formatMenuTree(List<OptInfo> optInfos,String superOptId) {
        if (StringUtils.isEmpty(superOptId)){
            return Collections.emptyList();
        }

        Iterator<OptInfo> menus = optInfos.iterator();
        OptInfo parentOpt = null;

        while (menus.hasNext()) {
            OptInfo optInfo = menus.next();
            if (StringUtils.equals(superOptId, optInfo.getOptId())) {
                parentOpt=optInfo;
            }
            for (OptInfo opt : optInfos) {
                if (opt.getOptId().equals(optInfo.getPreOptId())) {
                    opt.addChild(optInfo);
                }
            }
        }
        if (parentOpt!=null){
            return parentOpt.getChildren();
        }else {
            return Collections.emptyList();
        }
    }

    private List<OptInfo> listUserOptInfos(String userCode){
        List<OptInfo> userOptinfos = new ArrayList<>();

        Set<String> optIds = new HashSet<>(20);
        List<UserRole> userRoles = listUserRoles(
            GlobalConstValue.NO_TENANT_TOP_UNIT, userCode);
        userRoles.add(new UserRole(new UserRoleId(userCode, SecurityContextUtils.PUBLIC_ROLE_CODE)));
        Map<String, ? extends RoleInfo> codeToRoleMap = CollectionsOpt.createHashMap(
            CodeRepositoryCache.roleInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT),
            RoleInfo::getRoleCode);

        Map<String, ? extends OptInfo> codeToOptMap = CollectionsOpt.createHashMap(
            CodeRepositoryCache.optInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT),
            OptInfo::getOptId);

        for (UserRole ur : userRoles) {
            RoleInfo ri = codeToRoleMap.get(ur.getRoleCode());
            if (ri != null) {
                for (RolePower rp : ri.getRolePowers()) {
                    OptMethod om = CodeRepositoryCache.optMethodRepo
                        .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)
                        .getAppendMap().get(rp.getOptCode());
                    if (om != null)
                        optIds.add(om.getOptId());
                }
            }
        }

        for(String optId : optIds){
            OptInfo oi = (OptInfo)codeToOptMap.get(optId);
            if("Y".equals(oi.getIsInToolbar())){
                OptInfo soi = new OptInfo();
                soi.copy(oi);
                soi.setOptId(oi.getOptId());
                userOptinfos.add(soi);
            }
        }
        return userOptinfos;
    }


    protected List<OptInfo> getDirectOptInfo(){
        List<OptInfo> dirOptInfos = new ArrayList<>();
        for(OptInfo oi : CodeRepositoryCache.optInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)){
            if(StringUtils.equals(oi.getOptUrl(),"...")){
                OptInfo soi = new OptInfo();
                soi.copy((OptInfo)oi);
                soi.setOptId(oi.getOptId());
                dirOptInfos.add(soi);
            }
        }
        return dirOptInfos;
    }

    /*@Override
    public List<OptInfo> listUserMenuOptInfos(String userCode, boolean asAdmin) {
        List<OptInfo> userOptinfos =listUserOptInfos(userCode*//*ud.getUserCode()*//*);
        List<OptInfo> preOpts = getDirectOptInfo();

        List<OptInfo> allUserOpt = getMenuFuncs(preOpts,userOptinfos);

        Collections.sort(allUserOpt, (o1, o2) -> // Long.compare(o1.getOrderInd() , o2.getOrderInd())) ;
            ( o2.getOrderInd() == null && o1.getOrderInd() == null)? 0 :
                ( (o2.getOrderInd() == null)? 1 :
                    (( o1.getOrderInd() == null)? -1 :
                        ((o1.getOrderInd() > o2.getOrderInd())? 1:(
                            o1.getOrderInd() < o2.getOrderInd()?-1:0 ) ))));
        return formatMenuTree(allUserOpt);
    }*/

    @Override
    public List<OptInfo> listUserMenuOptInfosUnderSuperOptId(
        String userCode, String superOptId , boolean asAdmin) {

       /* CentitUserDetails ud =  loadUserDetailsByUserCode(userCode);
        if(ud==null)
            return null;*/
        List<OptInfo> userOptinfos =listUserOptInfos(userCode/*ud.getUserCode()*/);
        List<OptInfo> preOpts = getDirectOptInfo();

        List<OptInfo> allUserOpt = getMenuFuncs(preOpts,userOptinfos);

        Collections.sort(allUserOpt, (o1, o2) -> // Long.compare(o1.getOrderInd() , o2.getOrderInd())) ;
            ( o2.getOrderInd() == null && o1.getOrderInd() == null)? 0 :
                ( (o2.getOrderInd() == null)? 1 :
                    (( o1.getOrderInd() == null)? -1 :
                        ((o1.getOrderInd() > o2.getOrderInd())? 1:(
                            o1.getOrderInd() < o2.getOrderInd()?-1:0 ) ))));

        return formatMenuTree(allUserOpt,superOptId);
    }

    @Override
    public List<OptInfo> listMenuOptInfosUnderOsId(String osId) {
        return Collections.emptyList();
    }

    @Override
    public OptInfo addOptInfo(OptInfo optInfo) {
        return optInfo;
    }

    @Override
    public OptInfo updateOptInfo(OptInfo optInfo) {
        return optInfo;
    }

    @Override
    public List<DataDictionary> listDataDictionaries(String catalogCode) {
        for(DataCatalog data : catalogRepo.getCachedTarget()){
            if( StringUtils.equals(catalogCode, data.getCatalogCode())){
                return data.getDataDictionaries();
            }
        }
        return null;
    }

    @Override
    public void deleteDataDictionary(String catalogCode) {

    }

    @Override
    public int[] updateOptIdByOptCodes(String optId, List<String> optCodes) {
        return new int[0];
    }

    @Override
    public boolean deleteOptInfoByOptId(String optId) {
        return false;
    }


    @Override
    public void batchSaveWorkGroup(List<WorkGroup> workGroups) {

    }

    @Override
    public List<WorkGroup> listWorkGroup(String groupId, String userCode, String roleCode) {
        return Collections.emptyList();
    }

    @Override
    public List<UserUnit> listUserUnits(String topUnit, String userCode) {
        List<UserUnit> userUnits = new ArrayList<>(10);
        for (UserUnit uu : allUserUnitRepo.getCachedTarget()) {
            if(StringUtils.equals(userCode,uu.getUserCode())){
                userUnits.add((UserUnit)uu);
            }
        }
        return userUnits;
    }

    @Override
    public List<UserUnit> listUnitUsers(/*String topUnit,*/String unitCode) {
        List<UserUnit> unitUsers = new ArrayList<>(10);
        for (UserUnit uu : allUserUnitRepo.getCachedTarget()) {
            if(StringUtils.equals(unitCode,uu.getUnitCode())){
                unitUsers.add((UserUnit)uu);
            }
        }
        return unitUsers;
    }

    /**
     * 获取 用户角色关系
     * @param userCode 用户代码
     * @return  List 用户所有菜单功能
     */
    @Override
    public List<UserRole> listUserRoles(String topUnit, String userCode){
        List<UserRole> roles = new ArrayList<>();
        for (UserRole ur : allUserRoleRepo.getCachedTarget()) {
            if (StringUtils.equals(ur.getUserCode(),userCode)) {
                roles.add(ur);
            }
        }
        return roles;
    }

    /**
     * 获取 角色用户关系
     * @param roleCode 角色代码
     * @return  List 用户所有菜单功能
     */
    @Override
    public List<UserRole> listRoleUsers(String topUnit, String roleCode){
        List<UserRole> users = new ArrayList<>();
        for (UserRole ur : allUserRoleRepo.getCachedTarget()) {
            if (StringUtils.equals(ur.getRoleCode(),roleCode)) {
                users.add(ur);
            }
        }
        return users;
    }

    @Override
    public CentitUserDetails loadUserDetailsByLoginName(String loginName) {
        for(CentitUserDetails ud : allUserDetailsRepo.getCachedTarget()){
            if(StringUtils.equals(ud.getUserInfo().getLoginName(), loginName))
                return ud;
        }
        return null;
    }

    @Override
    public CentitUserDetails loadUserDetailsByUserCode(String userCode) {
        for(CentitUserDetails ud : allUserDetailsRepo.getCachedTarget()){
            if(StringUtils.equals(ud.getUserInfo().getUserCode(), userCode))
                return ud;
        }
        return null;
    }

    @Override
    public CentitUserDetails loadUserDetailsByRegEmail(String regEmail) {
        for(CentitUserDetails ud : allUserDetailsRepo.getCachedTarget()){
            if(StringUtils.equals(ud.getUserInfo().getRegEmail(), regEmail))
                return ud;
        }
        return null;
    }

    @Override
    public CentitUserDetails loadUserDetailsByRegCellPhone(String regCellPhone) {
        for(CentitUserDetails ud : allUserDetailsRepo.getCachedTarget()){
            if(StringUtils.equals(ud.getUserInfo().getRegCellPhone(), regCellPhone))
                return ud;
        }
        return null;
    }

    @Override
    public UnitInfo loadUnitInfo(String unitCode){
        ListAppendMap<UnitInfo> us =
            CodeRepositoryCache.unitInfoRepo.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT);
        if(us==null){
            return null;
        }
        for(UnitInfo ud : us.getListData()){
            if(StringUtils.equals(ud.getUnitCode(), unitCode))
                return ud;
        }
        return null;
    }

    @Override
    public UserSetting getUserSetting(String userCode, String paramCode) {
        CentitUserDetails ud =  loadUserDetailsByUserCode(userCode);
        if(ud==null)
            return null;
        //userCode, String paramCode,String paramValue, String paramName
        return new UserSetting(ud.getUserCode(),paramCode,
            ud.getUserSettingValue(paramCode), "用户参数");
    }

    @Override
    public List<UserSetting> listUserSettings(String userCode){
        CentitUserDetails ud =  loadUserDetailsByUserCode(userCode);
        if(ud==null) {
            return null;
        }
        //userCode, String paramCode,String paramValue, String paramName
        Map<String, String> settingMap =  ud.getUserSettings();
        if(settingMap==null){
            return null;
        }
        List<UserSetting> userSettings = new ArrayList<>(settingMap.size()+1);
        for(Map.Entry<String, String> ent : ud.getUserSettings().entrySet()){
            userSettings.add( new UserSetting(ud.getUserCode(),
                ent.getKey(),ent.getValue(),"用户参数"));
        }
        return userSettings;
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        UserInfo ui = CodeRepositoryCache.userInfoRepo.
            getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT)
            .getAppendMap().get(userInfo.getUserCode());
        if(ui==null)
            return;
        ui.copyFromUserInfo(userInfo);
    }

    @Override
    public void saveUserSetting(UserSetting userSetting) {
        CentitUserDetails ud =  loadUserDetailsByUserCode(userSetting.getUserCode());
        if(ud==null)
            return;
        ud.setUserSettingValue(userSetting.getParamCode(),userSetting.getParamValue());
    }

    @Override
    public List<UserInfo> listAllUsers(String topUnit) {
        reloadPlatformData();
        return CodeRepositoryCache.userInfoRepo.getCachedValue(
            GlobalConstValue.NO_TENANT_TOP_UNIT).getListData();
    }

    @Override
    public List<OsInfo> listOsInfos(String topUnit) {
        reloadPlatformData();
        return CodeRepositoryCache.osInfoCache.getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT);
    }

    @Override
    public OsInfo getOsInfo(String osId) {
        return null;
    }

    @Override
    public OsInfo deleteOsInfo(String osId) {
        return null;
    }

    @Override
    public OsInfo updateOsInfo(OsInfo osInfo) {
        return null;
    }

    @Override
    public OsInfo addOsInfo(OsInfo osInfo) {
        return null;
    }

    @Override
    public List<UnitInfo> listAllUnits(String topUnit) {
        reloadPlatformData();
        //return CodeRepositoryCache.unitInfoRepo.getCachedTarget(topUnit);
        return CodeRepositoryCache.unitInfoRepo.
            getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT).getListData();
    }

    @Override
    public List<UserUnit> listAllUserUnits(String topUnit){
        reloadPlatformData();
        //return CodeRepositoryCache.userUnitRepo.getCachedTarget();
        return this.allUserUnitRepo.getCachedTarget();
    }

    @Override
    public List<DataCatalog> listAllDataCatalogs(String topUnit){
        reloadPlatformData();
        return catalogRepo.getCachedTarget();
    }

    /**
     * 获取所有角色信息
     *
     * @return List 操作方法信息
     */
    @Override
    public List<RoleInfo> listAllRoleInfo(String topUnit) {
        reloadPlatformData();
        return CodeRepositoryCache.roleInfoRepo
            .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT);
    }

    /**
     * 获取业务操作信息
     *
     * @return List 业务信息
     */
    @Override
    public List<OptInfo> listAllOptInfo(String topUnit) {
        reloadPlatformData();
        return CodeRepositoryCache.optInfoRepo
            .getCachedValue(GlobalConstValue.NO_TENANT_TOP_UNIT);
    }

    @Override
    public List<OptInfo> listOptInfoByRole(String roleCode) {
        return null;
    }

    /**
     * 获取所有角色和权限对应关系
     * @return List 操作方法信息
     */
    @Override
    public List<RolePower> listAllRolePower(String topUnit){
        return allRolePower.getCachedTarget();
    }

    protected List<RolePower> loadAllRolePower() {
        reloadPlatformData();
        return allRolePower.getCachedTarget();
    }

    protected List<DataCatalog> loadAllDataCatalog() {
        reloadPlatformData();
        return catalogRepo.getCachedTarget();
    }

    protected List<UserRole> loadAllUserRole() {
        reloadPlatformData();
        return allUserRoleRepo.getCachedTarget();
    }
    protected List<UserUnit> loadAllUserUnit() {
        reloadPlatformData();
        return allUserUnitRepo.getCachedTarget();
    }

    protected List<OptMethod> loadAllOptMethod() {
        reloadPlatformData();
        return allOptMethod.getCachedTarget();
    }

    protected List<OptDataScope> loadOptDataScope() {
        reloadPlatformData();
        return optDataScopes.getCachedTarget();
    }

    @Override
    public List<OptMethod> listAllOptMethod(String topUnit){
        return allOptMethod.getCachedTarget();
    }

    @Override
    public OptTreeNode getSysOptTree() {
        return null;
    }

    @Override
    public List<OptMethod> listOptMethodByRoleCode(String roleCode) {
        return null;
    }

    @Override
    public OptMethod addOptMethod(OptMethod optMethod) {
        return optMethod;
    }

    @Override
    public OptMethod mergeOptMethod(OptMethod optMethod) {
        return optMethod;
    }

    @Override
    public void deleteOptMethod(String optCode) {

    }

    @Override
    public List<ConfigAttribute> getRolesWithApiId(String apiId) {
        return Collections.emptyList();
    }

    /**
     * 获取租户下所有的数据范围定义表达式
     * @param superOptId 最上层业务操作代码
     * @return 所有的数据范围定义表达式
     */
    @Override
    public List<OptDataScope> listAllOptDataScope(String superOptId) {
        return optDataScopes.getCachedTarget();
    }
    /**
     * 根据用户代码获得 用户的所有租户，顶级机构
     *
     * @param userCode userCode
     * @return List 用户所有的机构信息
     */
    @Override
    public List<UnitInfo> listUserTopUnits(String userCode) {
        return CollectionsOpt.createList(
            new UnitInfo(GlobalConstValue.NO_TENANT_TOP_UNIT,
                "T", "不支持租户时的默认顶级机构"));
    }

    @Override
    public List<UnitRole> listUnitRoles(String unitCode) {
        return Collections.emptyList();
    }

    @Override
    public List<UnitRole> listRoleUnits(String roleCode) {
        return Collections.emptyList();
    }

    @Override
    public JSONObject getTenantInfoByTopUnit(String topUnit) {
        return null;
    }

    @Override
    public JSONObject fetchUserTenantGroupInfo(String userCode, String topUnit) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByIdCardNo(String idCardNo) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByUserWord(String userWord) {
        return null;
    }
}
