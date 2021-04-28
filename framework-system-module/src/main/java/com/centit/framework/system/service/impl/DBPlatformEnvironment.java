package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.GlobalConstValue;
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
import com.centit.support.algorithm.UuidOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

//@Service("platformEnvironment")
public class DBPlatformEnvironment implements PlatformEnvironment {

    public static final Logger logger = LoggerFactory.getLogger(DBPlatformEnvironment.class);

    @Autowired
    private CentitPasswordEncoder passwordEncoder;

    @Autowired
    protected OptDataScopeDao dataScopeDao;

    @Autowired
    private UserSettingDao userSettingDao;

    @Autowired
    private OsInfoDao osInfoDao;

    @Autowired
    private OptInfoDao optInfoDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Autowired
    private DataCatalogDao dataCatalogDao;

    @Autowired
    private UserUnitDao userUnitDao;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @Autowired
    private RoleInfoDao roleInfoDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UnitRoleDao unitRoleDao;

    @Autowired
    private OptMethodDao optMethodDao;

    @Autowired
    private RolePowerDao rolePowerDao;


    private boolean supportTenant;

    public DBPlatformEnvironment(){
        supportTenant = false;
    }

    public void setSupportTenant(boolean supportTenant) {
        this.supportTenant = supportTenant;
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
                    us.copyFromIUserSetting(userSetting);
                    us.setCreateDate(DatetimeOpt.currentUtilDate());
                    userSettingDao.saveNewUserSetting(us);
                }else {
                    us.copyFromIUserSetting(userSetting);
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
        optInfos.sort((a,b)-> a.getOrderInd()==null? 1 :(
                b.getOrderInd() == null ? -1 :Long.compare(a.getOrderInd(),b.getOrderInd() )
            ));

        List<OptInfo> parentMenu = new ArrayList<>();
        for (OptInfo optInfo :optInfos) {
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

    /*@Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfos(String userCode, boolean asAdmin) {

        List<OptInfo> preOpts = optInfoDao.listParentMenuFunc();
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls = optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser);
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listUserMenuOptInfosUnderSuperOptId(String userCode, String superOptId, boolean asAdmin) {
        List<OptInfo> preOpts=optInfoDao.listParentMenuFunc();
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls=optInfoDao.getMenuFuncByUserID(userCode, optType);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts,  ls);
        return formatMenuTree(menuFunsByUser,superOptId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUserRole> listUserRoles(String topUnit, String userCode) {
        return supportTenant ? userRoleDao.listUserRolesByTopUnit(topUnit, userCode)
            : userRoleDao.listUserRoles(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUserRole> listRoleUsers(String topUnit, String roleCode) {
        return supportTenant ? userRoleDao.listRoleUsersByTopUnit(topUnit, roleCode)
            : userRoleDao.listRoleUsers(roleCode);
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
    public List<UserInfo> listAllUsers(String topUnit) {
        if(supportTenant) {
            Map<String, Object> filterMap = new HashMap<>();
            filterMap.put("topUnit", topUnit);
            return userInfoDao.listObjects(filterMap);
        } else {
            return userInfoDao.listObjects();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitInfo> listAllUnits(String topUnit) {
        if(supportTenant) {
            Map<String, Object> filterMap = new HashMap<>();
            filterMap.put("topUnit", topUnit);
            return unitInfoDao.listObjects(filterMap);
        } else {
            return unitInfoDao.listObjects();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listAllUserUnits(String topUnit) {
        if(supportTenant) {
            Map<String, Object> filterMap = new HashMap<>();
            filterMap.put("topUnit", topUnit);
            return userUnitDao.listObjectsAll(filterMap);
        } else {
            return userUnitDao.listObjects();
        }
    }

    /**
     * 根据用户代码获得 用户的所有租户，顶级机构
     *
     * @param userCode userCode
     * @return List 用户所有的机构信息
     */
    @Override
    public List<? extends IUnitInfo> listUserTopUnits(String userCode) {
        return supportTenant ? unitInfoDao.listUserTopUnits(userCode)
            : CollectionsOpt.createList(
            new UnitInfo(GlobalConstValue.NO_TENANT_TOP_UNIT,
                "T", "不支持租户时的默认顶级机构"));
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
                      uu.setXzRank(IUserUnit.MAX_XZ_RANK);
                    }
                }
            }
        }
        return userUnits;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUserUnits(String topUnit, String userCode) {
        return this.supportTenant ? fetchUserUnitXzRank(userUnitDao.listUserUnitsByUserCode(topUnit, userCode))
            : fetchUserUnitXzRank(userUnitDao.listUserUnitsByUserCode(userCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUnitUsers(String unitCode) {
      return fetchUserUnitXzRank(userUnitDao.listUnitUsersByUnitCode(unitCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRoleInfo> listAllRoleInfo(String topUnit) {
        return this.supportTenant ? roleInfoDao.listAllRoleByUnit(topUnit)
            : roleInfoDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRolePower> listAllRolePower(String topUnit) {
        return this.supportTenant ? rolePowerDao.listAllRolePowerByUnit(topUnit)
            : rolePowerDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptInfo> listAllOptInfo(String topUnit) {
        return this.supportTenant ? optInfoDao.listAllOptInfoByUnit(topUnit)
            : optInfoDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptMethod> listAllOptMethod(String topUnit) {
        return this.supportTenant ? optMethodDao.listAllOptMethodByUnit(topUnit)
            : optMethodDao.listObjectsAll();
    }

    /**
     * @return 所有的数据范围定义表达式
     */
    @Override
    public List<? extends IOptDataScope> listAllOptDataScope(String topUnit) {
        return this.supportTenant ? dataScopeDao.listAllDataScopeByUnit(topUnit)
            : dataScopeDao.listAllDataScope();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalog> listAllDataCatalogs(String topUnit) {
        return  this.supportTenant ? dataCatalogDao.listDataCatalogByUnit(topUnit)
            : dataCatalogDao.listObjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataDictionary> listDataDictionaries(String catalogCode) {
        return dataDictionaryDao.listDataDictionary(catalogCode);
    }

    //@Transactional
    private JsonCentitUserDetails fillUserDetailsField(UserInfo userinfo){
        List<UserUnit> usun = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
        String currentUnitCode = userinfo.getPrimaryUnit();
        JsonCentitUserDetails sysuser = new JsonCentitUserDetails();
        sysuser.setUserInfo((JSONObject) JSON.toJSON(userinfo));
        sysuser.getUserInfo().put("userPin", userinfo.getUserPin());
        sysuser.setUserUnits((JSONArray) JSON.toJSON(usun));
        for(UserUnit uu :usun){
            if("T".equals(uu.getRelType()) || "I".equals(uu.getRelType())){
                sysuser.setCurrentStationId(uu.getUserUnitId());
                currentUnitCode = uu.getUnitCode();
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
        sysuser.setAuthoritiesByRoles((JSONArray)JSON.toJSON(roles));
        List<FVUserOptList> uoptlist = userInfoDao.listUserOptMethods(userinfo.getUserCode());
        Map<String, String> userOptList = new HashMap<>();
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
            for(UserSetting us :uss) {
                sysuser.putUserSettingsParams(us.getParamCode(), us.getParamValue());
            }
        }

        sysuser.setTopUnitCode(userinfo.getTopUnit());
        if(StringUtils.isBlank(sysuser.getTopUnitCode())) {
            UnitInfo ui = unitInfoDao.getObjectById(currentUnitCode);
            if (ui != null) {
                sysuser.setTopUnitCode(ui.getTopUnit());
            }
            if (StringUtils.isBlank(sysuser.getTopUnitCode())) {
                sysuser.setTopUnitCode(GlobalConstValue.SYSTEM_TENANT_TOP_UNIT);
            }
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
        if(ui==null) {
            return;
        }
        ui.copyFromIUserInfo(userInfo);
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
    //@Override
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
                om.setOptCode(UuidOpt.getUuidAsString22());
                optMethodDao.saveNewObject(om);
            }
        }
        //更新
        if(triple.getMiddle() != null && triple.getMiddle().size()>0){
            for(Pair<OptMethod, OptMethod> p : triple.getMiddle()){
                OptMethod oldMethod = p.getLeft();
                OptMethod newMethod = p.getRight();
                newMethod.setOptCode(oldMethod.getOptCode());
                optMethodDao.updateOptMethod(newMethod);
            }
        }
        //删除
        if(triple.getRight() != null && triple.getRight().size()>0){
            for(OptMethod om : triple.getRight()){
                optMethodDao.deleteObject(om);
            }
        }
    }

    @Override
    public List<? extends IOsInfo> listOsInfos(String topUnit) {
        return this.supportTenant ? osInfoDao.listOsInfoByUnit(topUnit)
            : osInfoDao.listObjects();
    }

}
