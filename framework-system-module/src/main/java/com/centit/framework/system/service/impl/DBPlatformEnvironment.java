package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.*;
import com.centit.framework.security.model.CentitPasswordEncoder;
import com.centit.framework.security.model.JsonCentitUserDetails;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.po.*;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringRegularOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    @Override
    public List<UserSetting> listUserSettings(String userCode){
        return userSettingDao.getUserSettingsByCode(userCode);
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
                    userSettingDao.updateUserSetting(us);
                }
            }
        }
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

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfos(String userCode, boolean asAdmin) {

        List<OptInfo> preOpts = optInfoDao.getMenuFuncByOptUrl();
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls = optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfosUnderSuperOptId(String userCode, String superOptId,boolean asAdmin) {
        List<OptInfo> preOpts=optInfoDao.getMenuFuncByOptUrl();
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls=optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser,superOptId);
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
    @Transactional(readOnly = true)
    public List<UserInfo> listAllUsers() {
        return userInfoDao.listObjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitInfo> listAllUnits() {
        return unitInfoDao.listObjects();
    }

    @Override
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
    public List<UserUnit> listUserUnits(String userCode) {
        return fetchUserUnitXzRank(userUnitDao.listUserUnitsByUserCode(userCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUnitUsers(String unitCode) {
      return fetchUserUnitXzRank(userUnitDao.listUnitUsersByUnitCode(unitCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRoleInfo> listAllRoleInfo() {
        return roleInfoDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRolePower> listAllRolePower() {
        return rolePowerDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptInfo> listAllOptInfo() {
        return optInfoDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptMethod> listAllOptMethod() {
        return optMethodDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalog> listAllDataCatalogs() {
        return dataCatalogDao.listObjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataDictionary> listDataDictionaries(String catalogCode) {
        return dataDictionaryDao.listDataDictionary(catalogCode);
    }

    //@Transactional
    private JsonCentitUserDetails fillUserDetailsField(UserInfo userinfo){
        List<UserUnit> usun = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
        JsonCentitUserDetails sysuser = new JsonCentitUserDetails();
        sysuser.setUserInfo((JSONObject) JSON.toJSON(userinfo));

        sysuser.setUserUnits((JSONArray) JSON.toJSON(usun));
        for(UserUnit uu :usun){
            if("T".equals(uu.getIsPrimary())){
                sysuser.setCurrentStationId(uu.getUserUnitId());
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
        sysuser.setAuthoritiesByRoles((JSONArray) JSON.toJSON(roles));
        List<FVUserOptList> uoptlist = userInfoDao.getAllOptMethodByUser(userinfo.getUserCode());
        Map<String, String> userOptList = new HashMap<String, String>();
        if (uoptlist != null) {
            for (FVUserOptList opt : uoptlist){
                if(!StringUtils.isBlank(opt.getOptMethod())) {
                    userOptList.put(opt.getOptId() + "-" + opt.getOptMethod(), opt.getOptMethod());
                }
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
    public JsonCentitUserDetails loadUserDetailsByLoginName(String loginName) {
         UserInfo userinfo = userInfoDao.getUserByLoginName(loginName);
         if(userinfo==null)
             return null;
         return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByUserCode(String userCode) {
         UserInfo userinfo = userInfoDao.getUserByCode(userCode);
         if(userinfo==null)
             return null;
         return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByRegEmail(String regEmail) {
        UserInfo userinfo = userInfoDao.getUserByRegEmail(regEmail);
           if(userinfo==null)
                return null;
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByRegCellPhone(String regCellPhone) {
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

    public static List<OptInfo> getMenuFuncs(List<OptInfo> preOpts, List<OptInfo> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<OptInfo> opts = new ArrayList<>();
        for(OptInfo opt : ls){
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
            for (int i = 0; i < preOpts.size(); i++) {
                isNeeds2[i] = false;
            }
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
        CollectionsOpt.sortAsTree(opts, new CollectionsOpt.ParentChild<OptInfo>() {
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
            optInfo.setOptType("O");//普通业务
            optInfo.setCreator("import");//外部导入
            OptInfo dbOptInfo = optInfoDao.getObjectById(optInfo.getOptId());
            if(dbOptInfo == null) {
                optInfo.setCreateDate(new Date());
                optInfoDao.saveNewObject(optInfo);
            }else{
                dbOptInfo.copy(optInfo);
                dbOptInfo.setUpdateDate(new Date());
                optInfoDao.updateOptInfo(dbOptInfo);
            }
            dbMethods.addAll(optMethodDao.listOptMethodByOptID(optInfo.getOptId()));
        }
        Triple<List<OptMethod>, List<Pair<OptMethod, OptMethod>>, List<OptMethod>> triple = CollectionsOpt.compareTwoList(
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
                newMethod.setOptCode(oldMethod.getOptCode());
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
