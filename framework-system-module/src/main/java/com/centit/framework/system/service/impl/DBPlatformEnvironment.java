package com.centit.framework.system.service.impl;

import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.*;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.security.model.CentitSecurityMetadata;
import com.centit.framework.security.model.OptTreeNode;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.po.*;
import com.centit.framework.system.security.CentitUserDetailsImpl;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.ListOpt;
import com.centit.support.algorithm.StringRegularOpt;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

//@Service("platformEnvironment")
public class DBPlatformEnvironment implements PlatformEnvironment {

    public static final Logger logger = LoggerFactory.getLogger(DBPlatformEnvironment.class);

    @Resource
    private CentitPasswordEncoder passwordEncoder;

    public void setPasswordEncoder(CentitPasswordEncoder p){
        this.passwordEncoder = p;
    }

    @Resource
    @NotNull
    private UserSettingDao userSettingDao;

    public void setUserSettingDao(UserSettingDao userSettingDao){
        this.userSettingDao = userSettingDao;
    }

    @Resource
    @NotNull
    private OptInfoDao optInfoDao;

    public void setOptInfoDao(OptInfoDao optInfoDao){
        this.optInfoDao = optInfoDao;
    }

    @Resource
    @NotNull
    private UserInfoDao userInfoDao;

    public void setUserInfoDao(UserInfoDao userInfoDao){
        this.userInfoDao = userInfoDao;
    }

    @Resource
    @NotNull
    private DataDictionaryDao dataDictionaryDao;

    public void setDataDictionaryDao(DataDictionaryDao dataDictionaryDao){
        this.dataDictionaryDao = dataDictionaryDao;
    }

    @Resource
    @NotNull
    private DataCatalogDao dataCatalogDao;

    public void setDataCatalogDao(DataCatalogDao dataCatalogDao){
        this.dataCatalogDao = dataCatalogDao;
    }

    @Resource
    @NotNull
    private UserUnitDao userUnitDao;

    public void setUserUnitDao(UserUnitDao userUnitDao){
        this.userUnitDao = userUnitDao;
    }

    @Resource
    @NotNull
    private UnitInfoDao unitInfoDao;

    public void setUnitInfoDao(UnitInfoDao unitInfoDao){
        this.unitInfoDao = unitInfoDao;
    }

    @Resource
    @NotNull
    private RoleInfoDao roleInfoDao;

    public void setRoleInfoDao(RoleInfoDao roleInfoDao){
        this.roleInfoDao = roleInfoDao;
    }

    @Resource
    @NotNull
    private UserRoleDao userRoleDao;

    public void setUserRoleDao(UserRoleDao userRoleDao){
        this.userRoleDao = userRoleDao;
    }

    @Resource
    @NotNull
    private UnitRoleDao unitRoleDao;

    public void setUnitRoleDao(UnitRoleDao unitRoleDao){
        this.unitRoleDao = unitRoleDao;
    }

    @Resource
    @NotNull
    private OptMethodDao optMethodDao;

    public void setOptMethodDao(OptMethodDao optMethodDao){
        this.optMethodDao = optMethodDao;
    }

    @Resource
    @NotNull
    private RolePowerDao rolePowerDao;

    public void setRolePowerDao(RolePowerDao rolePowerDao){
        this.rolePowerDao = rolePowerDao;
    }

    /**
     * 刷新数据字典
     *
     * @return  boolean 刷新数据字典
     */
    @Override
    public boolean reloadDictionary() {
        return false;
    }

    /**
     * 刷新权限相关的元数据
     *
     * @return boolean 刷新权限相关的元数据
     */
    @Override
    @Transactional(readOnly=true)
    public boolean reloadSecurityMetadata() {
        CentitSecurityMetadata.optMethodRoleMap.clear();
        List<RolePower> rplist = rolePowerDao.listObjectsAll();
        if(rplist==null || rplist.size()==0)
            return false;
        for(RolePower rp: rplist ){
            List<ConfigAttribute/*roleCode*/> roles = CentitSecurityMetadata.optMethodRoleMap.get(rp.getOptCode());
            if(roles == null){
                roles = new ArrayList</*roleCode*/>();
            }
            roles.add(new SecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode())));
            CentitSecurityMetadata.optMethodRoleMap.put(rp.getOptCode(), roles);
        }
        //将操作和角色对应关系中的角色排序，便于权限判断中的比较
        CentitSecurityMetadata.sortOptMethodRoleMap();

        List<OptMethodUrlMap> oulist = optInfoDao.listAllOptMethodUrlMap();
        CentitSecurityMetadata.optTreeNode.setChildList(null);
        CentitSecurityMetadata.optTreeNode.setOptCode(null);
        for(OptMethodUrlMap ou:oulist){
            List<List<String>> sOpt = CentitSecurityMetadata.parseUrl(ou.getOptDefUrl() ,ou.getOptReq());

            for(List<String> surls : sOpt){
                OptTreeNode opt = CentitSecurityMetadata.optTreeNode;
                for(String surl : surls)
                    opt = opt.setChildPath(surl);
                opt.setOptCode(ou.getOptCode());
            }
        }
        return true;
    }

    @Override
    public List<UserSetting> getAllSettings(){
        return userSettingDao.getAllSettings();
    }

    @Override
    @Transactional(readOnly = true)
    public UserSetting getUserSetting(String userCode, String paramCode) {
        return userSettingDao.getObjectById(new UserSettingId(userCode,paramCode));
    }

    @Override
    @Transactional
    public void saveUserSetting(IUserSetting userSetting) {
        // regCellPhone  idCardNo  regEmail
        if(StringUtils.equalsAny(userSetting.getParamCode(),
            "regCellPhone", "idCardNo","regEmail")) {
            UserInfo ui = userInfoDao.getUserByCode(userSetting.getUserCode());
            if (ui == null) return;
            if("regCellPhone".equals(userSetting.getParamCode())){
                ui.setRegCellPhone(userSetting.getParamValue());
            }else if("idCardNo".equals(userSetting.getParamCode())){
                ui.setIdCardNo(userSetting.getParamValue());
            }else if("regEmail".equals(userSetting.getParamCode())){
                ui.setRegEmail(userSetting.getParamValue());
            }
            userInfoDao.updateUser(ui);
        }else{
            if(StringUtils.isBlank(userSetting.getParamValue())){
                userSettingDao.deleteObjectById(
                  new UserSettingId(userSetting.getUserCode(),userSetting.getParamCode()) );
            }else {
                UserSetting us = userSettingDao.getObjectById(
                    new UserSettingId(userSetting.getUserCode(), userSetting.getParamCode()));
                if(us==null) {
                    us = new UserSetting();
                    us.copyNotNullProperty(userSetting);
                    us.setCreateDate(DatetimeOpt.currentUtilDate());
                    userSettingDao.saveNewUserSetting(us);
                }else {
                    us.copyNotNullProperty(userSetting);
                    userSettingDao.updateObject(us);
                }
            }
        }
    }

    /**
     * 将菜单列表组装为树状
     * @param optInfos 菜单列表
     * @param superOptId 顶级菜单ID 不为空时返回该菜单的下级菜单
     * @return 树状菜单列表
     */
    private List<OptInfo> formatMenuTree(List<OptInfo> optInfos,String... superOptId) {
        Iterator<OptInfo> menus = optInfos.iterator();
        OptInfo parentOpt = null;

        List<OptInfo> parentMenu = new ArrayList<>();
        while (menus.hasNext()) {
            OptInfo optInfo = menus.next();
            if (superOptId!=null && ArrayUtils.contains(superOptId, optInfo.getOptId())) {
                parentOpt=optInfo;
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
        if (superOptId!=null && parentOpt!=null){
            return parentOpt.getChildren();
        }else {
          return parentMenu;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfos(String userCode, boolean asAdmin) {

        List<OptInfo> preOpts = optInfoDao.getMenuFuncByOptUrl();
        String optType = asAdmin ? "S" : "O";
        List<FVUserOptMoudleList> ls = optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser,null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfosUnderSuperOptId(String userCode, String superOptId,boolean asAdmin) {
        List<OptInfo> preOpts=optInfoDao.getMenuFuncByOptUrl();
        String optType = asAdmin ? "S" : "O";
        List<FVUserOptMoudleList> ls=optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser,superOptId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FVUserRoles> listUserRolesByUserCode(String userCode) {
        return userRoleDao.listUserRolesByUserCode(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfo> listRoleUserByRoleCode(String roleCode) {
      return userInfoDao.listUsersByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FVUserRoles> listUserRoles(String userCode) {
        return userRoleDao.listUserRolesByUserCode(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FVUserRoles> listRoleUsers(String roleCode) {
        return userRoleDao.listRoleUsersByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUnitRole> listUnitRoles(String unitCode) {
        return unitRoleDao.listUnitRolesByUnitCode(unitCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUnitRole> listRoleUnits(String roleCode) {
        return unitRoleDao.listUnitRolesByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getUserInfoByUserCode(String userCode) {
        return userInfoDao.getUserByCode(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getUserInfoByLoginName(String loginName) {
        return userInfoDao.getUserByLoginName(loginName);
    }

    @Override
    @Transactional(readOnly = true)
    public IUnitInfo getUnitInfoByUnitCode(String unitCode) {
        return unitInfoDao.getObjectById(unitCode);
    }

    @Override
    @Transactional
    public void changeUserPassword(String userCode, String userPassword) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        user.setUserPin(passwordEncoder.encodePassword(userPassword, user.getUserCode()));
        userInfoDao.updateUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUserPassword(String userCode, String userPassword) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        return passwordEncoder.isPasswordValid(user.getUserPin(),
                    userPassword, user.getUserCode());
    }

    @Override
    @Cacheable(value = "UserInfo",key = "'userList'" )
    @Transactional(readOnly = true)
    public List<UserInfo> listAllUsers() {
        return userInfoDao.listObjects();
    }

    @Override
    @Cacheable(value="UnitInfo",key="'unitList'")
    @Transactional(readOnly = true)
    public List<UnitInfo> listAllUnits() {
        return unitInfoDao.listObjects();
    }

    @Override
    @Cacheable(value="AllUserUnits",key="'allUserUnits'")
    @Transactional(readOnly = true)
    public List<UserUnit> listAllUserUnits() {
        return userUnitDao.listObjectsAll();
    }

    private List<UserUnit> fetchUserUnitXzRank(List<UserUnit> userUnits){
        if(userUnits!=null){
            for (UserUnit uu : userUnits) {
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

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="UserUnits",key="#userCode")
    public List<UserUnit> listUserUnits(String userCode) {
        return fetchUserUnitXzRank(userUnitDao.listUserUnitsByUserCode(userCode));
    }

    @Override
    @Cacheable(value="UnitUsers",key="#unitCode")
    @Transactional(readOnly = true)
    public List<UserUnit> listUnitUsers(String unitCode) {
      return fetchUserUnitXzRank(userUnitDao.listUnitUsersByUnitCode(unitCode));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="RoleInfo",key="'roleCodeMap'")
    public Map<String, RoleInfo> getRoleRepo() {
        Map<String, RoleInfo> roleReop = new HashMap<>();
        List<RoleInfo> roleList = roleInfoDao.listObjectsAll();
        if(roleList!=null)
            for (Iterator<RoleInfo> it = roleList.iterator(); it.hasNext(); ) {
                RoleInfo roleinfo = it.next();
                roleReop.put(roleinfo.getRoleCode(), roleinfo);
            }
        return roleReop;
    }

    @Override
    @Cacheable(value="OptInfo",key="'optIdMap'")
    @Transactional(readOnly = true)
    public Map<String, OptInfo> getOptInfoRepo() {
        Map<String, OptInfo> optRepo = new HashMap<>();
        List<OptInfo> optList = optInfoDao.listObjectsAll();
        if (optList != null) {
            for (OptInfo optinfo : optList) {
                optRepo.put(optinfo.getOptId(), optinfo);
            }
        }

        return optRepo;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value="OptInfo",key="'optCodeMap'")
    public Map<String, OptMethod> getOptMethodRepo() {
        Map<String, OptMethod> powerRepo = new HashMap<>();

        List<OptMethod> optdefList = optMethodDao.listObjects();
        if(optdefList!=null){
            for (Iterator<OptMethod> it = optdefList.iterator(); it.hasNext(); ) {
                OptMethod optdef = it.next();
                powerRepo.put(optdef.getOptCode(), optdef);
            }
        }
        return powerRepo;
    }

    @Override
    @Cacheable(value = "DataDictionary",key="'CatalogCode'")
    @Transactional(readOnly = true)
    public List<DataCatalog> listAllDataCatalogs() {
        return dataCatalogDao.listObjects();
    }

    @Override
    @Cacheable(value = "DataDictionary",key="#catalogCode")
    @Transactional(readOnly = true)
    public List<DataDictionary> listDataDictionaries(String catalogCode) {
        return dataDictionaryDao.listDataDictionary(catalogCode);
    }

    @Override
    @Cacheable(value="UnitInfo",key="'unitCodeMap'")
    @Transactional(readOnly = true)
    public Map<String,UnitInfo> getUnitRepo() {
        Map<String, UnitInfo> unitRepo = new HashMap<>();
        List<UnitInfo> unitList = unitInfoDao.listObjects();
        if (unitList != null){
            for (UnitInfo unitinfo : unitList) {
                unitRepo.put(unitinfo.getUnitCode(), unitinfo);
            }
        }
        /**
         * 计算所有机构的子机构。只计算启动的机构
         */
        for (Map.Entry<String, UnitInfo> ent : unitRepo.entrySet()) {
            UnitInfo u = ent.getValue();
            String sParentUnit = u.getParentUnit();
            if ("T".equals(u.getIsValid())
                    && (sParentUnit != null && (!"".equals(sParentUnit)) && (!"0".equals(sParentUnit)))) {
                UnitInfo pU = unitRepo.get(sParentUnit);
                if (pU != null)
                    pU.getSubUnits().add(u);
            }
        }

        return unitRepo;
    }

    @Override
    @Cacheable(value = "UserInfo",key = "'userCodeMap'" )
    @Transactional(readOnly = true)
    public Map<String,UserInfo> getUserRepo() {
        Map<String, UserInfo> userInfoMap = new HashMap<>();
        List<UserInfo> users = userInfoDao.listObjects();
        if(users!=null){
            for (UserInfo userInfo : users) {
                userInfoMap.put(userInfo.getUserCode(), userInfo);
            }
        }
        return userInfoMap;
    }

    @Override
    @Cacheable(value = "UserInfo",key = "'loginNameMap'")
    @Transactional(readOnly = true)
    public Map<String, ? extends IUserInfo> getLoginNameRepo() {
        Map<String, UserInfo> userInfoMap = new HashMap<>();
        List<UserInfo> users = userInfoDao.listObjects();
        if(users!=null){
            for (UserInfo userInfo : users) {
                userInfoMap.put(userInfo.getLoginName(), userInfo);
            }
        }
        return userInfoMap;
    }

    @Override
    @Cacheable(value="UnitInfo",key="'depNoMap'")
    @Transactional(readOnly = true)
    public Map<String, ? extends IUnitInfo> getDepNoRepo() {
        Map<String, UnitInfo> depNo = new HashMap<>();
        List<UnitInfo> unitList = unitInfoDao.listObjects();
        if (unitList != null)
            for (UnitInfo unitinfo : unitList) {
                depNo.put(unitinfo.getDepNo(), unitinfo);
            }
        return depNo;
    }

    //@Transactional
    private CentitUserDetailsImpl fillUserDetailsField(UserInfo userinfo){
        List<UserUnit> usun = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
        userinfo.setUserUnits(usun);
        CentitUserDetailsImpl sysuser = new CentitUserDetailsImpl(userinfo);
        for(UserUnit uu :usun){
            if("T".equals(uu.getIsPrimary())){
                sysuser.setCurrentUserUnit(uu);
                break;
            }
        }
        //edit by zhuxw  代码从原框架迁移过来，可和其它地方合并
        List<RoleInfo> roles = new ArrayList<>();
        //所有的用户 都要添加这个角色
        roles.add(new RoleInfo("public", "general public","G",
                 "G","T", "general public"));
        List<FVUserRoles> userRolesList = userRoleDao.listUserRolesByUserCode(userinfo.getUserCode());
        if(userRolesList!=null) {
             for (FVUserRoles role : userRolesList) {
                 RoleInfo roleInfo = new RoleInfo();
                 //roleInfo.copy(role);
                 BeanUtils.copyProperties(role, roleInfo);
                 roles.add(roleInfo);
             }
         }
        //add  end
        //sysuser.setUserFuncs(functionDao.getMenuFuncByUserID(sysuser.getUserCode()));
        sysuser.setAuthoritiesByRoles(roles);
        List<FVUserOptList> uoptlist = userInfoDao.getAllOptMethodByUser(userinfo.getUserCode());
        Map<String, String> userOptList = new HashMap<String, String>();
        if (uoptlist != null) {
            for (FVUserOptList opt : uoptlist){
                if(!StringUtils.isBlank(opt.getOptMethod()))
                    userOptList.put(opt.getOptId() + "-" + opt.getOptMethod(), opt.getOptMethod());
            }
        }
        // ServletActionContext.getRequest().getSession().setAttribute("userOptList",
        // userOptList);
        sysuser.setUserOptList(userOptList);

        List<UserSetting> uss =userSettingDao.getUserSettingsByCode(userinfo.getUserCode());
        if(uss!=null){
            for(UserSetting us :uss)
                sysuser.putUserSettingsParams(us.getParamCode(), us.getParamValue());
        }
        return sysuser;
    }

    @Override
    @Transactional
    public CentitUserDetailsImpl loadUserDetailsByLoginName(String loginName) {
         UserInfo userinfo = userInfoDao.getUserByLoginName(loginName);
         if(userinfo==null)
             return null;
         return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public CentitUserDetailsImpl loadUserDetailsByUserCode(String userCode) {
         UserInfo userinfo = userInfoDao.getUserByCode(userCode);
         if(userinfo==null)
             return null;
         return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public CentitUserDetailsImpl loadUserDetailsByRegEmail(String regEmail) {
        UserInfo userinfo = userInfoDao.getUserByRegEmail(regEmail);
           if(userinfo==null)
                return null;
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public CentitUserDetailsImpl loadUserDetailsByRegCellPhone(String regCellPhone) {
        UserInfo userinfo = userInfoDao.getUserByRegCellPhone(regCellPhone);
           if(userinfo==null) {
             return null;
           }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public void updateUserInfo(IUserInfo userInfo) {
        UserInfo ui = userInfoDao.getUserByCode(userInfo.getUserCode());
        if(ui==null)
          return;
        ui.copyNotNullProperty(userInfo);
        userInfoDao.updateUser(ui);
    }

    private static List<OptInfo> getMenuFuncs(List<OptInfo> preOpts, List<FVUserOptMoudleList> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<OptInfo> opts = new ArrayList<>();

        for (FVUserOptMoudleList opm : ls) {
            OptInfo opt = new OptInfo();
            opt.setFormCode(opm.getFormcode());
            opt.setImgIndex(opm.getImgindex());
            opt.setIsInToolbar(opm.getIsintoolbar());
            opt.setMsgNo(opm.getMsgno());
            opt.setMsgPrm(opm.getMsgprm());
            opt.setOptId(opm.getOptid());
            opt.setOptType(opm.getOpttype());
            opt.setOptName(opm.getOptname());
            opt.setOptUrl(opm.getOpturl());
            opt.setPreOptId(opm.getPreoptid());
            opt.setTopOptId(opm.getTopoptid());
            opt.setPageType(opm.getPageType());
            opt.setOptRoute(opm.getOptRoute());

            opts.add(opt);
            for (int i = 0; i < preOpts.size(); i++) {
                if (opt.getPreOptId() != null && opt.getPreOptId().equals(preOpts.get(i).getOptId())) {
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
        /*
        ListOpt.sortAsTree(opts, new ListOpt.ParentChild<OptInfo>() {
            @Override
            public boolean parentAndChild(OptInfo p, OptInfo c) {
                return p.getOptId().equals(c.getPreOptId());
            }

        });*/
    }

    /**
     * 新增或修改菜单和操作
     * @param optInfos 菜单对象集合
     * @param optMethods 操作对象集合
     */
    @Override
    @Transactional
    public void insertOrUpdateMenu(List<? extends IOptInfo> optInfos, List<? extends IOptMethod> optMethods) {
        List<OptMethod> dbMethods = new ArrayList<>();
        for(OptInfo optInfo : (List<OptInfo>)optInfos){
            if(StringUtils.isEmpty(optInfo.getPreOptId())){
                optInfo.setPreOptId("0");
            }
            optInfo.setCreateDate(new Date());
            optInfo.setOptType("O");//普通业务
            optInfo.setCreator("import");//外部导入
            OptInfo dbOptInfo = optInfoDao.getObjectById(optInfo.getOptId());
            if(dbOptInfo == null) {
                optInfoDao.saveNewObject(optInfo);
            }else{
                dbOptInfo.copy(optInfo);
                optInfoDao.updateOptInfo(dbOptInfo);
            }
            dbMethods.addAll(optMethodDao.listOptMethodByOptID(optInfo.getOptId()));
        }
        Triple<List<OptMethod>, List<Pair<OptMethod, OptMethod>>, List<OptMethod>> triple = ListOpt.compareTwoList(
            dbMethods, (List<OptMethod>)optMethods,
            (o1, o2) -> StringUtils.compare(o1.getOptId()+o1.getOptMethod(), o2.getOptId()+o2.getOptMethod()));
        //新增
        if(triple.getLeft() != null && triple.getLeft().size()>0){
            for(OptMethod om : triple.getLeft()){
                if(StringUtils.isEmpty(om.getOptReq())){
                    om.setOptReq("CRUD");
                }
                om.setOptCode(optMethodDao.getNextOptCode());
                optMethodDao.saveNewObject(om);
            }
        }
        //更新
        if(triple.getMiddle() != null && triple.getMiddle().size()>0){
            for(Pair<OptMethod, OptMethod> p : triple.getMiddle()){
                OptMethod oldMethod = p.getLeft();
                OptMethod newMethod = p.getRight();
                oldMethod.copy(newMethod);
                optMethodDao.updateOptMethod(oldMethod);
            }
        }
        //删除
        if(triple.getRight() != null && triple.getRight().size()>0){
            for(OptMethod om : triple.getRight()){
                optMethodDao.deleteObject(om);
            }
        }
    }
}
