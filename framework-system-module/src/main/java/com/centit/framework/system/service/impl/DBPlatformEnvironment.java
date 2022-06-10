package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.GlobalConstValue;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.*;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.security.model.*;
import com.centit.framework.system.dao.*;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("dbPlatformEnvironment")
public class DBPlatformEnvironment implements PlatformEnvironment {

    public static final Logger logger = LoggerFactory.getLogger(DBPlatformEnvironment.class);
    public static final String SYSTEM = "system";

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

    private boolean supportTenant;

    public DBPlatformEnvironment() {
        supportTenant = false;
    }

    public void setSupportTenant(boolean supportTenant) {
        this.supportTenant = supportTenant;
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
    public void saveUserSetting(IUserSetting userSetting) {
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
                    us.copyFromIUserSetting(userSetting);
                    us.setCreateDate(DatetimeOpt.currentUtilDate());
                    userSettingDao.saveNewUserSetting(us);
                } else {
                    us.copyFromIUserSetting(userSetting);
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
    public IOptInfo addOptInfo(IOptInfo optInfo) {
        optInfoManager.saveNewOptInfo((OptInfo) optInfo);
        return optInfo;
    }

    @Override
    public IOptInfo updateOptInfo(IOptInfo optInfo) {
        optInfoDao.updateOptInfo((OptInfo) optInfo);
        return optInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUserRole> listUserRoles(String topUnit, String userCode) {
        return supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? userRoleDao.listUserRolesByTopUnit(topUnit, userCode)
            : userRoleDao.listUserRoles(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IUserRole> listRoleUsers(String topUnit, String roleCode) {
        return supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? userRoleDao.listRoleUsersByTopUnit(topUnit, roleCode)
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
        if (supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)) {
            return userInfoDao.listObjects(CollectionsOpt.createHashMap("topUnit", topUnit));
        } else {
            return userInfoDao.listObjects();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitInfo> listAllUnits(String topUnit) {
        if (supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)) {
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
        if (supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)) {
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

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUserUnits(String topUnit, String userCode) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? userUnitDao.listUserUnitsByUserCode(topUnit, userCode)
            : userUnitDao.listUserUnitsByUserCode(userCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserUnit> listUnitUsers(String unitCode) {
        return userUnitDao.listUnitUsersByUnitCode(unitCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRoleInfo> listAllRoleInfo(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? roleInfoDao.listAllRoleByUnit(topUnit)
            : roleInfoDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IRolePower> listAllRolePower(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit) ?
            rolePowerDao.listAllRolePowerByUnit(topUnit)
            : rolePowerDao.listObjectsAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptInfo> listAllOptInfo(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? optInfoDao.listAllOptInfoByUnit(topUnit)
            : optInfoDao.listObjectsAll();
    }

    @Override
    public List<? extends IOptInfo> listOptInfoByRole(String roleCode) {
        return optInfoDao.listOptInfoByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends IOptMethod> listAllOptMethod(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit) ?
            optMethodDao.listAllOptMethodByUnit(topUnit)
            : optMethodDao.listObjectsAll();
    }

    @Override
    public List<? extends IOptMethod> listOptMethodByRoleCode(String roleCode) {
        return optMethodDao.listOptMethodByRoleCode(roleCode);
    }

    @Override
    public IOptMethod addOptMethod(IOptMethod optMethod) {
        optMethodDao.saveNewObject((OptMethod) optMethod);
        return optMethod;
    }

    @Override
    public IOptMethod mergeOptMethod(IOptMethod optMethod) {
        optMethodDao.mergeObject((OptMethod)optMethod);
        return optMethod;
    }

    @Override
    public void deleteOptMethod(String optMethod) {
        optMethodDao.deleteObjectById(optMethod);
    }



    /**
     * @return 所有的数据范围定义表达式
     */
    @Override
    public List<? extends IOptDataScope> listAllOptDataScope(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? dataScopeDao.listAllDataScopeByUnit(topUnit)
            : dataScopeDao.listAllDataScope();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataCatalog> listAllDataCatalogs(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? dataCatalogDao.listDataCatalogByUnit(topUnit)
            : dataCatalogDao.listObjects();
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
    public boolean deleteOptDefAndRolepowerByOptCode(String optCode) {
        optMethodDao.deleteObjectById(optCode);
        String sql = "DELETE FROM f_rolepower WHERE OPT_CODE = ?";
        DatabaseOptUtils.doExecuteSql(optInfoDao, sql, new Object[]{optCode});
        OptMethod objectById = optMethodDao.getObjectById(optCode);
        if (objectById != null) {
            return false;
        }
        return true;
    }

    //@Transactional
    private JsonCentitUserDetails fillUserDetailsField(UserInfo userinfo) {
        List<UserUnit> userUnits = userUnitDao.listUserUnitsByUserCode(userinfo.getUserCode());
        String currentUnitCode = userinfo.getPrimaryUnit();
        JsonCentitUserDetails userDetails = new JsonCentitUserDetails();
        userDetails.setUserInfo((JSONObject) JSON.toJSON(userinfo));
        userDetails.getUserInfo().put("userPin", userinfo.getUserPin());
        userDetails.setUserUnits((JSONArray) JSON.toJSON(userUnits));
        if (StringUtils.isEmpty(userinfo.getCurrentStationId())) {
            for (UserUnit uu : userUnits) {
                if ("T".equals(uu.getRelType())) {
                    if (!supportTenant) {
                        userDetails.setCurrentStationId(uu.getUserUnitId());
                        currentUnitCode = uu.getUnitCode();
                        break;
                    } else if (StringUtils.isNotBlank(userinfo.getTopUnit()) && StringUtils.isNotBlank(uu.getTopUnit())
                        && userinfo.getTopUnit().equals(uu.getTopUnit())) {
                        userDetails.setCurrentStationId(uu.getUserUnitId());
                        currentUnitCode = uu.getUnitCode();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isEmpty(userDetails.getCurrentStationId())) {
            for (UserUnit uu : userUnits) {
                if (StringUtils.isNotBlank(userinfo.getTopUnit()) && StringUtils.isNotBlank(uu.getTopUnit())
                    && userinfo.getTopUnit().equals(uu.getTopUnit())) {
                    userDetails.setCurrentStationId(uu.getUserUnitId());
                    currentUnitCode = uu.getUnitCode();
                    break;
                }
            }
        }
        //edit by zhuxw  代码从原框架迁移过来，可和其它地方合并
        List<RoleInfo> roles = new ArrayList<>();
        //所有的用户 都要添加这个角色
        roles.add(new RoleInfo("public" + userinfo.getTopUnit(), "general public", "G",
            "G", "T", "general public"));
        List<FVUserRoles> userRolesList = userRoleDao.listUserRolesByUserCode(userinfo.getUserCode());
        if (userRolesList != null) {
            for (FVUserRoles role : userRolesList) {
                RoleInfo roleInfo = new RoleInfo();
                //roleInfo.copy(role);
                BeanUtils.copyProperties(role, roleInfo);
                roles.add(roleInfo);
            }
        }
        //add  end
        //userDetails.setUserFuncs(functionDao.getMenuFuncByUserID(userDetails.getUserCode()));
        appendAdminRoles(userinfo, roles);
        userDetails.setAuthoritiesByRoles((JSONArray) JSON.toJSON(roles));
        List<FVUserOptList> uoptlist = userInfoDao.listUserOptMethods(userinfo.getUserCode());
        Map<String, String> userOptList = new HashMap<>();
        if (uoptlist != null) {
            for (FVUserOptList opt : uoptlist) {
                if (!StringUtils.isBlank(opt.getOptMethod())) {
                    userOptList.put(opt.getOptId() + "-" + opt.getOptMethod(), opt.getOptMethod());
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
        //当用户还未加入任何租户或者单位时，currentUnitCode为空
        if (StringUtils.isNotBlank(currentUnitCode)) {
            UnitInfo currentUnit = unitInfoDao.getObjectById(currentUnitCode);
            if (null != currentUnit) {
                userDetails.getUserInfo().put("primaryUnitName", currentUnit.getUnitName());
                userDetails.getUserInfo().put("primaryUnit", currentUnit.getUnitCode());
            }
        }

        if (StringUtils.isBlank(userDetails.getTopUnitCode())) {
            //当用户还未加入任何租户或者单位时，currentUnitCode为空
            UnitInfo ui = null;
            if (StringUtils.isNotBlank(currentUnitCode)) {
                ui = unitInfoDao.getObjectById(currentUnitCode);
            }
            if (ui != null) {
                userDetails.setTopUnitCode(ui.getTopUnit());
            }
            if (StringUtils.isBlank(userDetails.getTopUnitCode())) {
                //userDetails.setTopUnitCode(GlobalConstValue.SYSTEM_TENANT_TOP_UNIT);
            } else {
                UnitInfo topUnit = unitInfoDao.getObjectById(userDetails.getTopUnitCode());
                if (null != topUnit) {
                    userDetails.getUserInfo().put("topUnitName", topUnit.getUnitName());
                }
            }
        } else {
            UnitInfo ui = unitInfoDao.getObjectById(userDetails.getTopUnitCode());
            if (GlobalConstValue.NO_TENANT_TOP_UNIT.equals(userDetails.getTopUnitCode())
                || GlobalConstValue.SYSTEM_TENANT_TOP_UNIT.equals(userDetails.getTopUnitCode())) {
                ui = unitInfoDao.getObjectById(currentUnitCode);
            }
            if (null != ui) {
                userDetails.getUserInfo().put("topUnitName", ui.getUnitName());
            }
        }
        return userDetails;
    }

    private void appendAdminRoles(UserInfo userInfo, List<RoleInfo> roles) {
        String topUnit = userInfo.getTopUnit();
        if (StringUtils.isBlank(topUnit)) {
            return;
        }
        WorkGroup admin = workGroupManager.getWorkGroup(topUnit, userInfo.getUserCode(), WorkGroup.WORKGROUP_ROLE_CODE_ADMIN);
        if (null != admin) {
            String roleCode = SYSTEM.equals(topUnit) ? "platadmin" : "tenantadmin";
            String roleName = SYSTEM.equals(topUnit) ? "平台管理员" : "租户管理员";
            roles.add(new RoleInfo(roleCode, roleName, "G", topUnit, "T", roleName));
        }
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByLoginName(String loginName) {
        UserInfo userinfo = userInfoDao.getUserByLoginName(loginName);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByUserCode(String userCode) {
        UserInfo userinfo = userInfoDao.getUserByCode(userCode);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByRegEmail(String regEmail) {
        UserInfo userinfo = userInfoDao.getUserByRegEmail(regEmail);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public JsonCentitUserDetails loadUserDetailsByRegCellPhone(String regCellPhone) {
        UserInfo userinfo = userInfoDao.getUserByRegCellPhone(regCellPhone);
        if (userinfo == null) {
            return null;
        }
        return fillUserDetailsField(userinfo);
    }

    @Override
    @Transactional
    public void updateUserInfo(IUserInfo userInfo) {
        UserInfo ui = userInfoDao.getUserByCode(userInfo.getUserCode());
        if (ui == null) {
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

    /**
     * 新增或修改菜单和操作
     *
     * @param optInfos   菜单对象集合
     * @param optMethods 操作对象集合
     */
    //@Override
    @Transactional
    public void insertOrUpdateMenu(List<? extends IOptInfo> optInfos, List<? extends IOptMethod> optMethods) {
        List<OptMethod> dbMethods = new ArrayList<>();
        for (OptInfo optInfo : (List<OptInfo>) optInfos) {
            if (StringUtils.isEmpty(optInfo.getPreOptId())) {
                optInfo.setPreOptId("0");
            }
            optInfo.setOptType("O");//普通业务
            optInfo.setCreator("import");//外部导入
            OptInfo dbOptInfo = optInfoDao.getObjectById(optInfo.getOptId());
            if (dbOptInfo == null) {
                optInfo.setCreateDate(new Date());
                optInfoDao.saveNewObject(optInfo);
            } else {
                dbOptInfo.copy(optInfo);
                dbOptInfo.setUpdateDate(new Date());
                optInfoDao.updateOptInfo(dbOptInfo);
            }
            dbMethods.addAll(optMethodDao.listOptMethodByOptID(optInfo.getOptId()));
        }
        Triple<List<OptMethod>, List<Pair<OptMethod, OptMethod>>, List<OptMethod>> triple = CollectionsOpt.compareTwoList(
            dbMethods, (List<OptMethod>) optMethods,
            (o1, o2) -> StringUtils.compare(o1.getOptId() + o1.getOptMethod(), o2.getOptId() + o2.getOptMethod()));
        //新增
        if (triple.getLeft() != null && triple.getLeft().size() > 0) {
            for (OptMethod om : triple.getLeft()) {
                if (StringUtils.isEmpty(om.getOptReq())) {
                    om.setOptReq("CRUD");
                }
                om.setOptCode(UuidOpt.getUuidAsString22());
                optMethodDao.saveNewObject(om);
            }
        }
        //更新
        if (triple.getMiddle() != null && triple.getMiddle().size() > 0) {
            for (Pair<OptMethod, OptMethod> p : triple.getMiddle()) {
                OptMethod oldMethod = p.getLeft();
                OptMethod newMethod = p.getRight();
                newMethod.setOptCode(oldMethod.getOptCode());
                optMethodDao.updateOptMethod(newMethod);
            }
        }
        //删除
        if (triple.getRight() != null && triple.getRight().size() > 0) {
            for (OptMethod om : triple.getRight()) {
                optMethodDao.deleteObject(om);
            }
        }
    }

    @Override
    public List<? extends IOsInfo> listOsInfos(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? osInfoDao.listOsInfoByUnit(topUnit)
            : osInfoDao.listObjects();
    }

    @Override
    public IOsInfo getOsInfo(String osId) {
        return osInfoDao.getObjectById(osId);
    }

    @Override
    public IOsInfo deleteOsInfo(String osId) {
        IOsInfo osInfo = osInfoDao.getObjectById(osId);
        if (osInfo != null) {
            osInfoDao.deleteObjectById(osId);
        }
        return osInfo;
    }

    @Override
    public IOsInfo updateOsInfo(IOsInfo osInfo) {
        OsInfo osInfo1 = osInfoDao.getObjectById(osInfo.getOsId());
        osInfo.setTopUnit(osInfo1.getTopUnit());
        osInfoDao.updateObject((OsInfo) osInfo);
        return osInfo;
    }

    @Override
    public IOsInfo addOsInfo(IOsInfo osInfo) {
        osInfoDao.saveNewObject((OsInfo) osInfo);
        return osInfo;
    }


    @Override
    public int countUserByTopUnit(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? userUnitDao.countUserByTopUnit(topUnit) : userUnitDao.countUserByTopUnit(null);
    }

    @Override
    public int countUnitByTopUnit(String topUnit) {
        return this.supportTenant && !GlobalConstValue.NO_TENANT_TOP_UNIT.equals(topUnit)
            ? unitInfoDao.countUnitByTopUnit(topUnit) : unitInfoDao.countUnitByTopUnit(null);
    }

    @Override
    public List<? extends IWorkGroup> listWorkGroup(Map<String, Object> filterMap, PageDesc pageDesc) {
        return workGroupManager.listWorkGroup(filterMap, pageDesc);
    }

    @Override
    public void batchWorkGroup(List<IWorkGroup> workGroups) {
        ArrayList<WorkGroup> workGroups1 = new ArrayList<>();
        for (IWorkGroup workGroup : workGroups) {
            if (workGroup instanceof WorkGroup) {
                workGroups1.add((WorkGroup) workGroup);
            }
        }
        workGroupManager.batchWorkGroup(workGroups1);
    }

    @Override
    public boolean loginUserIsExistWorkGroup(String osId, String userCode) {
        return workGroupManager.loginUserIsExistWorkGroup(osId, userCode);
    }

    @Override
    public List<ConfigAttribute> getRolesWithApiId(String apiId) {
        List<? extends IRolePower> rolePowers = rolePowerDao.listRolePowerWithTopUnit(apiId);
        List<ConfigAttribute> roles = new ArrayList<>();
        for (IRolePower rp : rolePowers) {
            dealRolePower(roles, rp);
        }
        return roles;
    }

    @Override
    public OptTreeNode getSysOptTree() {
        Map<String, List<ConfigAttribute>> optMethodRoleMap = new HashMap<>(100);
        List<? extends IRolePower> rolePowers = rolePowerDao.listSysRolePower();
        if (rolePowers == null || rolePowers.size() == 0) {
            return null;
        }
        for (IRolePower rp : rolePowers) {
            List<ConfigAttribute> roles = optMethodRoleMap.get(rp.getOptCode());
            if (roles == null) {
                roles = new ArrayList<>();
            }
            dealRolePower(roles, rp);
            optMethodRoleMap.put(rp.getOptCode(), roles);
        }
        OptTreeNode optTreeNode = new OptTreeNode();
        List<? extends IOptMethod> iOptMethods = optMethodDao.listAllOptMethodByUnit(SYSTEM);
        for (IOptMethod ou : iOptMethods) {
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

    private void dealRolePower(List<ConfigAttribute> roles, IRolePower rp) {
        if (SecurityContextUtils.PUBLIC_ROLE_CODE.equalsIgnoreCase(rp.getRoleCode())) {
            roles.add(new CentitSecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode()) + rp.getTopUnit()));
        } else if (SecurityContextUtils.ANONYMOUS_ROLE_CODE.equalsIgnoreCase(rp.getRoleCode())) {
            roles.add(new CentitSecurityConfig(SecurityContextUtils.SPRING_ANONYMOUS_ROLE_CODE));
        } else {
            roles.add(new CentitSecurityConfig(CentitSecurityMetadata.ROLE_PREFIX + StringUtils.trim(rp.getRoleCode())));
        }
    }
}
