package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.*;
import com.centit.framework.model.security.CentitPasswordEncoder;
import com.centit.framework.model.security.CentitSecurityConfig;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.model.security.OptTreeNode;
import com.centit.framework.security.CentitSecurityMetadata;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.system.constant.TenantConstant;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.service.*;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("platformEnvironment")
public class DBPlatformEnvironment implements PlatformEnvironment {

    public static final Logger logger = LoggerFactory.getLogger(DBPlatformEnvironment.class);
    public static final String SYSTEM = "system";

    @Value("${userinfo.password.expired_days:183}")
    protected int passwordExpiredDays;

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

    @Autowired
    private OptInfoManager optInfoManager;

    @Autowired
    private WorkGroupManager workGroupManager;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantPowerManage tenantPowerManage;

    @Autowired
    private UserPlatService userPlatService;

    public DBPlatformEnvironment() {
    }

    @Override
    public List<UserSetting> listUserSettings(String userCode) {
        return userSettingDao.getUserSettingsByCode(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSetting getUserSetting(String userCode, String paramCode) {
        return userSettingDao.getObjectById(new UserSettingId(userCode, paramCode));
    }

    @Override
    @Transactional
    public void saveUserSetting(UserSetting userSetting) {
        // regCellPhone  idCardNo  regEmail
        if (StringUtils.equalsAny(userSetting.getParamCode(),
            "regCellPhone", "idCardNo", "regEmail")) {
            UserInfo ui = userInfoDao.getUserByCode(userSetting.getUserCode());
            if (ui == null) {
                return;
            }
            if ("regCellPhone".equals(userSetting.getParamCode())) {
                ui.setRegCellPhone(userSetting.getParamValue());
            } else if ("idCardNo".equals(userSetting.getParamCode())) {
                ui.setIdCardNo(userSetting.getParamValue());
            } else if ("regEmail".equals(userSetting.getParamCode())) {
                ui.setRegEmail(userSetting.getParamValue());
            }
            userInfoDao.updateUser(ui);
        } else {
            if (StringUtils.isBlank(userSetting.getParamValue())) {
                userSettingDao.deleteObjectById(
                    new UserSettingId(userSetting.getUserCode(), userSetting.getParamCode()));
            } else {
                UserSetting us = userSettingDao.getObjectById(
                    new UserSettingId(userSetting.getUserCode(), userSetting.getParamCode()));
                if (us == null) {
                    us = new UserSetting();
                    us.copyFromUserSetting(userSetting);
                    us.setCreateDate(DatetimeOpt.currentUtilDate());
                    userSettingDao.saveNewUserSetting(us);
                } else {
                    us.copyFromUserSetting(userSetting);
                    userSettingDao.updateUserSetting(us);
                }
            }
        }
    }

    /**
     * 将菜单列表组装为树状
     *
     * @param optInfos 菜单列表
     * @return 树状菜单列表
     */
    private List<OptInfo> formatMenuTree(List<OptInfo> optInfos) {
        optInfos.sort((a, b) -> a.getOrderInd() == null && b.getOrderInd() == null ? 0 : (
            a.getOrderInd() == null ? 1 : (b.getOrderInd() == null ? -1 : Long.compare(a.getOrderInd(), b.getOrderInd()
            ))
        ));

        List<OptInfo> parentMenu = new ArrayList<>();
        for (OptInfo optInfo : optInfos) {
            boolean getParent = false;
            for (OptInfo opt : optInfos) {
                if (opt.getOptId().equals(optInfo.getPreOptId())) {
                    opt.addChild(optInfo);
                    getParent = true;
                    break;
                }
            }
            if (!getParent) {
                parentMenu.add(optInfo);
            }
        }
        return parentMenu;
    }

    /**
     * 将菜单列表组装为树状
     *
     * @param optInfos   菜单列表
     * @param superOptId 顶级菜单ID 不为空时返回该菜单的下级菜单
     * @return 树状菜单列表
     */
    private List<OptInfo> formatMenuTree(List<OptInfo> optInfos, String superOptId) {
        if (StringUtils.isEmpty(superOptId)) {
            return Collections.emptyList();
        }
        optInfos.sort((a, b) -> a.getOrderInd() == null && b.getOrderInd() == null ? 0 : (
            a.getOrderInd() == null ? 1 : (b.getOrderInd() == null ? -1 : Long.compare(a.getOrderInd(), b.getOrderInd()
            ))
        ));
        Iterator<OptInfo> menus = optInfos.iterator();
        OptInfo parentOpt = null;

        while (menus.hasNext()) {
            OptInfo optInfo = menus.next();
            if (StringUtils.equals(superOptId, optInfo.getOptId())) {
                parentOpt = optInfo;
            }
            for (OptInfo opt : optInfos) {
                if (opt.getOptId().equals(optInfo.getPreOptId())) {
                    opt.addChild(optInfo);
                }
            }
        }
        if (parentOpt != null) {
            return parentOpt.getChildren();
        } else {
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
        OptInfo optInfo = optInfoDao.getObjectById(superOptId);
        String topOptId = "";
        if (optInfo != null) {
            topOptId = optInfo.getTopOptId();
        }
        List<OptInfo> preOpts;
        if (StringUtils.isNotBlank(topOptId)) {
            preOpts = optInfoDao.listParentMenuFunc(topOptId);
        } else {
            preOpts = optInfoDao.listParentMenuFunc();
        }
        String optType = asAdmin ? "S" : "O";
        List<OptInfo> ls = optInfoDao.getMenuFuncByUserID(userCode, optType, topOptId);
        List<OptInfo> menuFunsByUser = getMenuFuncs(preOpts, ls);
        return formatMenuTree(menuFunsByUser, superOptId);
    }

    @Override
    public List<OptInfo> listMenuOptInfosUnderOsId(String osId) {
        List<OptInfo> allOptsUnderOsId = optInfoDao.listAllOptInfoByTopOpt(osId);
        return formatMenuTree(allOptsUnderOsId, osId);
    }

    @Override
    public OptInfo addOptInfo(OptInfo optInfo) {
        optInfoManager.saveNewOptInfo(optInfo);
        return optInfo;
    }

    @Override
    public OptInfo updateOptInfo(OptInfo optInfo) {
        optInfoDao.updateOptInfo(optInfo);
        return optInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> listUserRoles(String topUnit, String userCode) {
        return FVUserRoles.mapToUserRoles(userRoleDao.listUserRolesByTopUnit(topUnit, userCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRole> listRoleUsers(String topUnit, String roleCode) {
        return FVUserRoles.mapToUserRoles(userRoleDao.listRoleUsersByTopUnit(topUnit, roleCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitRole> listUnitRoles(String unitCode) {
        return unitRoleDao.listUnitRolesByUnitCode(unitCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitRole> listRoleUnits(String roleCode) {
        return unitRoleDao.listUnitRolesByRoleCode(roleCode);
    }

    @Override
    @Transactional
    public void changeUserPassword(String userCode, String userPassword) {
        UserInfo user = userInfoDao.getUserByCode(userCode);
        user.setUserPin(passwordEncoder.encodePassword(userPassword, user.getUserCode()));
        // 设置密码有效期
        user.setPwdExpiredTime(DatetimeOpt.addDays(DatetimeOpt.currentUtilDate(), passwordExpiredDays));
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
        return userInfoDao.listObjects(CollectionsOpt.createHashMap("topUnit", topUnit));
    }

    /*@Override
    public List<UserInfo> listUsersByProperties(Map<String, Object> filters, String topUnit) {
        if(StringUtils.isBlank(topUnit)){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "topUnit不能为空");
        }
        filters.put("topUnit", topUnit);
        return userInfoDao.listObjectsByProperties(filters);
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<UnitInfo> listAllUnits(String topUnit) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("topUnit", topUnit);
        return unitInfoDao.listObjectsByProperties(filterMap);
    }

    /*@Override
    public List<UnitInfo> listUnitsByProperties(Map<String, Object> filters, String topUnit) {
        if(StringUtils.isBlank(topUnit)){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "topUnit不能为空");
        }
        filters.put("topUnit", topUnit);
        return unitInfoDao.listObjectsByProperties(filters);
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listAllUserUnits(String topUnit) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("topUnit", topUnit);
        return userUnitDao.listObjectsAll(filterMap);
    }

    /**
     * 根据用户代码获得 用户的所有租户，顶级机构
     *
     * @param userCode userCode
     * @return List 用户所有的机构信息
     */
    @Override
    public List<UnitInfo> listUserTopUnits(String userCode) {
        return unitInfoDao.listUserTopUnits(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUserUnits(String topUnit, String userCode) {
        return userUnitDao.listUserUnitsByUserCode(topUnit, userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUnitUsers(String unitCode) {
        return userUnitDao.listUnitUsersByUnitCode(unitCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleInfo> listAllRoleInfo(String topUnit) {
        return roleInfoDao.listAllRoleByUnit(topUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolePower> listAllRolePower(String topUnit) {
        return rolePowerDao.listAllRolePowerByUnit(topUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptInfo> listAllOptInfo(String topUnit) {
        return optInfoDao.listAllOptInfoByUnit(topUnit);
    }

    @Override
    public List<OptInfo> listOptInfoByRole(String roleCode) {
        return optInfoDao.listOptInfoByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptMethod> listAllOptMethod(String topUnit) {
        return optMethodDao.listAllOptMethodByUnit(topUnit);
    }

    @Override
    public List<OptMethod> listOptMethodByRoleCode(String roleCode) {
        return optMethodDao.listOptMethodByRoleCode(roleCode);
    }

    @Override
    public OptMethod addOptMethod(OptMethod optMethod) {
        optMethodDao.saveNewObject(optMethod);
        return optMethod;
    }


    @Override
    public OptMethod mergeOptMethod(OptMethod optMethod) {
        optMethodDao.mergeObject(optMethod);
//        List<RolePower> rolePowers = rolePowerDao.listObjectsByProperties(CollectionsOpt.createHashMap("optCode", optMethod.getOptCode()));
//        if(rolePowers==null || rolePowers.size()==0){
//            RolePower rolePower=new RolePower();
//            rolePower.setOptCode(optMethod.getOptCode());
//            rolePower.setRoleCode("public");
//            rolePower.setCreateDate(new Date());
//            rolePower.setUpdateDate(new Date());
//            rolePowerDao.saveNewRolePower(rolePower);
//        }
        return optMethod;
    }

    /**
     * @return 所有的数据范围定义表达式
     */
    @Override
    public List<OptDataScope> listAllOptDataScope(String topUnit) {
        return dataScopeDao.listAllDataScopeByUnit(topUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalog> listAllDataCatalogs(String topUnit) {
        return dataCatalogDao.listDataCatalogByUnit(topUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataDictionary> listDataDictionaries(String catalogCode) {
        return dataDictionaryDao.listDataDictionary(catalogCode);
    }

    @Override
    public void deleteDataDictionary(String catalogCode) {
        dataDictionaryDao.deleteDictionary(catalogCode);
        dataCatalogDao.deleteObjectById(catalogCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int[] updateOptIdByOptCodes(String optId, List<String> optCodes) {
        return optMethodDao.updateOptIdByOptCodes(optId, optCodes);
    }

    @Override
    public boolean deleteOptInfoByOptId(String optId) {
        optInfoManager.deleteOptInfoById(optId);
        OptInfo optInfo = optInfoDao.getObjectById(optId);
        if (optInfo != null) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOptMethod(String optCode) {
        if(StringUtils.isBlank(optCode)) return;
        optMethodDao.deleteOptMethodById(optCode);
        rolePowerDao.deleteRolePowersByOptCode(optCode);
    }

    //@Transactional
    private CentitUserDetails fillUserDetailsField(UserInfo userinfo) {
        List<UserUnit> userUnits = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
        String currentUnitCode = userinfo.getPrimaryUnit();
        CentitUserDetails userDetails = new CentitUserDetails();
        userDetails.setUserInfo(userinfo);
        //userDetails.getUserInfo().put("userPin", userinfo.getUserPin());
        userDetails.setUserUnits(userUnits);
        // 确保 currentUnitCode 是正确的
        if (StringUtils.isNotBlank(userinfo.getCurrentStationId())) {
            for (UserUnit uu : userUnits) {
                if(userinfo.getCurrentStationId().equals(uu.getUserUnitId())){
                    userDetails.setCurrentStationId(uu.getUserUnitId());
                    currentUnitCode = uu.getUnitCode();
                    break;
                }
            }
        }

        if (StringUtils.isBlank(userDetails.getCurrentStationId())) {
            for (UserUnit uu : userUnits) {
                if ("T".equals(uu.getRelType()) && StringUtils.equals(userinfo.getTopUnit(), uu.getTopUnit())) {
                    userDetails.setCurrentStationId(uu.getUserUnitId());
                    currentUnitCode = uu.getUnitCode();
                    break;
                }
            }
        }

        //edit by zhuxw  代码从原框架迁移过来，可和其它地方合并
        List<RoleInfo> roles = new ArrayList<>();
        //所有的用户 都要平台公共角色和所属租户公共角色
        roles.add(new RoleInfo("publicsystem", "general public", "G",
            "system", "T", "general public"));
        roles.add(new RoleInfo("public" + userinfo.getTopUnit(), "topunit public", "G",
            userinfo.getTopUnit(), "T", "topunit public"));
        List<FVUserRoles> userRolesList = userRoleDao.listUserRolesByTopUnit(userinfo.getTopUnit(), userinfo.getUserCode());
        if (userRolesList != null) {
            for (FVUserRoles role : userRolesList) {
                roles.add(role.toRoleInfo());
            }
        }
        //add  end
        //userDetails.setUserFuncs(functionDao.getMenuFuncByUserID(userDetails.getUserCode()));
        appendAdminRoles(userinfo, roles);
        userDetails.mapAuthoritiesByRoles(roles);
        List<OptMethod> publicOptMethod = optMethodDao.listPublicOptMethodByUnit(userinfo.getTopUnit());
        Map<String, String> userOptList = new HashMap<>();
        if (publicOptMethod != null) {
            for (OptMethod opt : publicOptMethod) {
                if (!StringUtils.isBlank(opt.getOptMethod())) {
                    userOptList.put(opt.getOptCode(), opt.getOptName());
                }
            }
        }
        if (userRolesList != null && userRolesList.size()>0) {
            String[] userRole = new String[userRolesList.size()];
            for (int i = 0; i < userRolesList.size(); i++) {
                userRole[i] = userRolesList.get(i).getRoleCode();
            }
            List<OptMethod> userOptMethod = optMethodDao.listUserOptMethodByRoleCode(userRole);
            if (userOptMethod != null) {
                for (OptMethod opt : userOptMethod) {
                    if (!StringUtils.isBlank(opt.getOptMethod())) {
                        userOptList.put(opt.getOptCode(), opt.getOptName());
                    }
                }
            }
        }
        // ServletActionContext.getRequest().getSession().setAttribute("userOptList",
        // userOptList);
        userDetails.setUserOptList(userOptList);

        List<UserSetting> uss = userSettingDao.getUserSettingsByCode(userinfo.getUserCode());
        if (uss != null) {
            for (UserSetting us : uss) {
                userDetails.putUserSettingsParams(us.getParamCode(), us.getParamValue());
            }
        }

        userDetails.setTopUnitCode(userinfo.getTopUnit());
        UnitInfo currentUnit = null;
        //当用户还未加入任何租户或者单位时，currentUnitCode为空
        if (StringUtils.isNotBlank(currentUnitCode)) {
            currentUnit = unitInfoDao.getObjectById(currentUnitCode);
            if (null != currentUnit) {
                userDetails.setCurrentUnitName(currentUnit.getUnitName());
                //.getUserInfo().put("primaryUnitName", currentUnit.getUnitName());
                userDetails.getUserInfo().setPrimaryUnit(currentUnit.getUnitCode());
            }
        }

        if (StringUtils.isBlank(userDetails.getTopUnitCode())) {
            //当用户还未加入任何租户或者单位时，currentUnitCode为空
            if (currentUnit != null) {
                userDetails.setTopUnitCode(currentUnit.getTopUnit());
            }
            if (StringUtils.isBlank(userDetails.getTopUnitCode())) {
                //userDetails.setTopUnitCode(GlobalConstValue.SYSTEM_TENANT_TOP_UNIT);
            } else {
                UnitInfo topUnit = unitInfoDao.getObjectById(userDetails.getTopUnitCode());
                if (null != topUnit) {
                    userDetails.setTopUnitName(topUnit.getUnitName());//.getUserInfo().put("topUnitName", topUnit.getUnitName());
                }
            }
        } else {
            UnitInfo ui = unitInfoDao.getObjectById(userDetails.getTopUnitCode());
            if (GlobalConstValue.NO_TENANT_TOP_UNIT.equals(userDetails.getTopUnitCode())) {
                ui = unitInfoDao.getObjectById(currentUnitCode);
            }
            if (null != ui) {
                userDetails.setTopUnitName(ui.getUnitName());
            }
        }
        return userDetails;
    }

    private void appendAdminRoles(UserInfo userInfo, List<RoleInfo> roles) {
        String topUnit = userInfo.getTopUnit();
        if (StringUtils.isBlank(topUnit)) {
            return;
        }
        if (tenantPowerManage.userIsTenantOwner(userInfo.getUserCode(), topUnit)) {
            if(SYSTEM.equals(topUnit)){
                roles.add(new RoleInfo(TenantConstant.PLAT_ADMIN, "平台管理员", "G", topUnit, "T", "平台管理员"));
            }
            roles.add(new RoleInfo(TenantConstant.TENANT_ADMIN, "租户管理员", "G", topUnit, "T", "租户管理员"));
            return;
        }

        //TenantConstant.TENANT_ADMIN_ROLE_CODE
        List<WorkGroup> adminRols = workGroupManager.listWorkGroup(topUnit, userInfo.getUserCode(), null);
        if(null == adminRols || adminRols.isEmpty()) return;
        int admin = 0;
        for (WorkGroup workGroup : adminRols){
            if (TenantConstant.TENANT_ADMIN_ROLE_CODE.equals(workGroup.getRoleCode())){
                admin = 2;
                break;
            } else if (TenantConstant.ORGANIZE_ADMIN.equals(workGroup.getRoleCode())){
                admin = 1;
            }
        }

        if (admin==2) {
            if(SYSTEM.equals(topUnit)){
                roles.add(new RoleInfo(TenantConstant.PLAT_ADMIN, "平台管理员", "G", topUnit, "T", "平台管理员"));
            }
            roles.add(new RoleInfo(TenantConstant.TENANT_ADMIN, "租户管理员", "G", topUnit, "T", "租户管理员"));
        } else  if (admin==1) {
            roles.add(new RoleInfo(TenantConstant.ORGANIZE_ADMIN, "组织管理员", "G", topUnit, "T", "组织管理员"));
        }
    }

    @Override
    @Transactional
    public CentitUserDetails loadUserDetailsByLoginName(String loginName) {
        UserInfo userinfo = userInfoDao.getUserByLoginName(loginName);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public CentitUserDetails loadUserDetailsByUserCode(String userCode) {
        UserInfo userinfo = userInfoDao.getUserByCode(userCode);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    public UnitInfo loadUnitInfo(String unitCode){
        return unitInfoDao.getObjectById(unitCode);
    }

    @Override
    public UserInfo getUserInfoByIdCardNo(String idCardNo) {
        return userInfoDao.getUserByIdCardNo(idCardNo);
    }

    @Override
    public UserInfo getUserInfoByUserWord(String userWord) {
        return userInfoDao.getUserByUserWord(userWord);
    }

    @Override
    @Transactional
    public CentitUserDetails loadUserDetailsByRegEmail(String regEmail) {
        UserInfo userinfo = userInfoDao.getUserByRegEmail(regEmail);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public CentitUserDetails loadUserDetailsByRegCellPhone(String regCellPhone) {
        UserInfo userinfo = userInfoDao.getUserByRegCellPhone(regCellPhone);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfo userInfo) {
        UserInfo ui = userInfoDao.getUserByCode(userInfo.getUserCode());
        if (ui == null) {
            return;
        }
        ui.copyFromUserInfo(userInfo);
        userInfoDao.updateUser(ui);
    }

    @Override
    @Transactional
    public void saveUserLoginInfo(UserInfo userInfo) {
        userInfoDao.saveUserLoginInfo(userInfo);
    }

    public static List<OptInfo> getMenuFuncs(List<OptInfo> preOpts, List<OptInfo> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<OptInfo> opts = new ArrayList<>();
        for (OptInfo opt : ls) {
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
            if (nestedMenu == 0) {
                break;
            }

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

    public static List<OptInfo> getFormatMenuTree(List<OptInfo> optInfos) {
        DBPlatformEnvironment dbPlatformEnvironment = new DBPlatformEnvironment();
        List<OptInfo> opts = dbPlatformEnvironment.formatMenuTree(optInfos);
        return opts;
    }

    @Override
    public List<OsInfo> listOsInfos(String topUnit) {
        return osInfoDao.listOsInfoByUnit(topUnit);
    }

    @Override
    public OsInfo getOsInfo(String osId) {
        return osInfoDao.getObjectById(osId);
    }

    @Override
    public OsInfo deleteOsInfo(String osId) {
        OsInfo osInfo = osInfoDao.getObjectById(osId);
        if (osInfo != null) {
            osInfoDao.deleteObjectById(osId);
        }
        return osInfo;
    }

    @Override
    public OsInfo updateOsInfo(OsInfo osInfo) {
        OsInfo osInfo1 = osInfoDao.getObjectById(osInfo.getOsId());
        osInfo.setTopUnit(osInfo1.getTopUnit());
        osInfoDao.updateObject((OsInfo) osInfo);
        return osInfo;
    }

    @Override
    public OsInfo addOsInfo(OsInfo osInfo) {
        osInfoDao.saveNewObject((OsInfo) osInfo);
        return osInfo;
    }

    @Override
    public List<WorkGroup> listWorkGroup(String groupId, String userCode, String roleCode) {
        return workGroupManager.listWorkGroup(groupId, userCode, roleCode);
    }

    @Override
    public void batchSaveWorkGroup(List<WorkGroup> workGroups) {
        workGroupManager.batchWorkGroup(workGroups);
    }


    @Override
    public List<ConfigAttribute> getRolesWithApiId(String apiId) {
        List<RolePower> rolePowers = rolePowerDao.listRolePowerUseApiId(apiId);
        List<ConfigAttribute> roles = new ArrayList<>();
        for (RolePower rp : rolePowers) {
            dealRolePower(roles, rp);
        }
        Collections.sort(roles,
            Comparator.comparing(ConfigAttribute::getAttribute));
        return roles;
    }

    @Override
    public OptTreeNode getSysOptTree() {
        Map<String, List<ConfigAttribute>> optMethodRoleMap = new HashMap<>(100);
        List<RolePower> rolePowers = rolePowerDao.listSysRolePower();
        if (rolePowers == null || rolePowers.size() == 0) {
            return null;
        }
        for (RolePower rp : rolePowers) {
            List<ConfigAttribute> roles = optMethodRoleMap.get(rp.getOptCode());
            if (roles == null) {
                roles = new ArrayList<>();
            }
            dealRolePower(roles, rp);
            optMethodRoleMap.put(rp.getOptCode(), roles);
        }
        OptTreeNode optTreeNode = new OptTreeNode();
        List<OptMethod> OptMethods = optMethodDao.listAllOptMethodByUnit(SYSTEM);
        for (OptMethod ou : OptMethods) {
            if (ou != null) {
                if (StringUtils.isNotBlank(ou.getOptUrl()) && StringUtils.isNotBlank(ou.getOptReq())) {
                    List<List<String>> sOpt = optTreeNode.parsePowerDefineUrl(
                        ou.getOptUrl(), ou.getOptReq());
                    for (List<String> surls : sOpt) {
                        OptTreeNode opt = optTreeNode;
                        for (String surl : surls) {
                            opt = opt.setChildPath(surl);
                        }
                        List<ConfigAttribute> roles = optMethodRoleMap.get(ou.getOptCode());
                        opt.addRoleList(roles);
                    }
                }
            }
        }
        return optTreeNode;
    }

    private void dealRolePower(List<ConfigAttribute> roles, RolePower rp) {
        if (SecurityContextUtils.PUBLIC_ROLE_CODE.equalsIgnoreCase(rp.getRoleCode())) {
            roles.add(new CentitSecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode()) + rp.getTopUnit()));
        } else if (SecurityContextUtils.ANONYMOUS_ROLE_CODE.equalsIgnoreCase(rp.getRoleCode())) {
            roles.add(new CentitSecurityConfig(SecurityContextUtils.SPRING_ANONYMOUS_ROLE_CODE));
        } else {
            roles.add(new CentitSecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode())));
        }
    }


    @Override
    public JSONObject getTenantInfoByTopUnit(String topUnit) {
        TenantInfo tenantInfo = tenantService.getTenantInfo(topUnit);
        if (null == tenantInfo) {
            return null;
        }
        //单独翻译租户所有者姓名
        String ownUserName = CodeRepositoryUtil.getUserName(topUnit, tenantInfo.getOwnUser());
        JSONObject jsonObject = JSONObject.from(tenantInfo);
        jsonObject.put("ownUserName", ownUserName);
        //添加实际资源占用量
        //int dataBaseCount = sourceInfoDao.countDataBase(CollectionsOpt.createHashMap("topUnit", topUnit));
        int unitCount =  unitInfoDao.countUnitByTopUnit(topUnit);
        int userCount = userUnitDao.countUserByTopUnit(topUnit);
        int osCount = osInfoDao.countOsInfoByTopUnit(topUnit);

        //jsonObject.put("databaseCount",dataBaseCount);
        jsonObject.put("unitCount",unitCount);
        jsonObject.put("userCount",userCount);
        jsonObject.put("osCount",osCount);

        return jsonObject;
    }

    @Override
    public JSONObject fetchUserTenantGroupInfo(String userCode, String topUnit) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("tenantRole", tenantPowerManage.userTenantRole(userCode, topUnit));
        JSONArray userTenants = tenantService.userTenants(userCode);
        jsonObj.put("userTenants", userTenants);
        //获取微信用户信息
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userCode", userCode);
        List<UserPlat> userPlats = userPlatService.listObjects(paramsMap, null);
        //第三方登录信息
        jsonObj.put("userPlats", (userPlats != null && !userPlats.isEmpty()) ? userPlats : new ArrayList<>());
        return jsonObj;
    }

}
