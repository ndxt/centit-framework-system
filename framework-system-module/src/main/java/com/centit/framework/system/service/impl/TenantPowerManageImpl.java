package com.centit.framework.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.TenantInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.WorkGroup;
import com.centit.framework.system.constant.TenantConstant;
import com.centit.framework.system.dao.TenantInfoDao;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.dao.UserUnitDao;
import com.centit.framework.system.dao.WorkGroupDao;
import com.centit.framework.system.service.TenantPowerManage;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TenantPowerManageImpl implements TenantPowerManage {

    protected Logger logger = LoggerFactory.getLogger(TenantPowerManage.class);
    @Autowired
    private TenantInfoDao tenantInfoDao;

    @Autowired
    private WorkGroupDao workGroupDao;

    @Autowired
    private UserUnitDao userUnitDao;

    @Autowired
    private UnitInfoDao unitInfoDao;


    @Override
    public boolean userIsTenantOwner(String userCode, String topUnit) {
        return tenantInfoDao.userIsOwner(topUnit, userCode);
    }

    @Override
    public boolean userIsTenantAdmin(String userCode, String topUnit) {
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("groupId", topUnit, "userCode", userCode,
            "roleCode", TenantConstant.TENANT_ADMIN_ROLE_CODE);
        return workGroupDao.countObjectByProperties(filterMap) > 0;
    }

    @Override
    public String userTenantRole(String userCode, String topUnit) {
        if (StringUtils.isBlank(topUnit)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "topUnit不能为空");
        }
        if (this.userIsTenantOwner(userCode, topUnit)) {
            return TenantConstant.TENANT_OWNE_ROLE_CODE;
        }

        WorkGroup workGroup = workGroupDao.getObjectByProperties(
            CollectionsOpt.createHashMap("groupId", topUnit,  "userCode", userCode));
        if(workGroup == null) return "";
        if (TenantConstant.ORGANIZE_ADMIN.equals(workGroup.getRoleCode())) {
            return TenantConstant.ORGANIZE_ADMIN+":"+workGroup.getRunToken();
        }
        return workGroup.getRoleCode();
    }

    @Override
    public JSONArray listTenantAdmin(String topUnit) {
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("groupId", topUnit,
            "roleCode", TenantConstant.TENANT_ADMIN_ROLE_CODE);
        List<WorkGroup> works = workGroupDao.listObjectsByProperties(filterMap);
        if(works==null|| works.isEmpty())
            return null;
        JSONArray users = new JSONArray();
        for(WorkGroup work:works){
            JSONObject user = new JSONObject();
            user.put("userCode", work.getUserCode());
            UserInfo ui = CodeRepositoryUtil.getUserInfoByCode(topUnit, work.getUserCode());
            if(ui!=null) {
                user.put("userName", ui.getUserName());
                user.put("loginName", ui.getLoginName());
            }
            users.add(user);
        }
        return users;
    }

    @Override
    public boolean userIsTenantMember(String userCode, String topUnit) {
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("userCode", userCode, "topUnit", topUnit);
        return userUnitDao.countObjectByProperties(filterMap)>0;
    }

    @Override
    public boolean userIsApplicationAdmin(String userCode, String osId) {
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("groupId", osId, "userCode", userCode, "roleCode",
            TenantConstant.APPLICATION_ADMIN_ROLE_CODE);
        return workGroupDao.countObjectByProperties(filterMap) > 0;
    }

    @Override
    public boolean userIsApplicationMember(String userCode, String osId) throws ObjectException {
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("groupId", osId, "userCode", userCode);
        return workGroupDao.countObjectByProperties(filterMap) > 0;
    }

    @Override
    public boolean userIsSystemMember(String userCode) {
        return this.userIsTenantMember(userCode, TenantConstant.SYSTEM_TENANT_TOP_UNIT_CODE);
    }

    @Override
    public boolean userIsSystemAdmin(String userCode) {
        return !CollectionUtils.sizeIsEmpty(userUnitDao.listUserUnitsByUserCode(TenantConstant.SYSTEM_TENANT_TOP_UNIT_CODE, userCode));
    }


    @Override
    public boolean userNumberLimitIsOver(String topUnit) {
        TenantInfo tenantInfo = tenantInfoDao.getObjectById(topUnit);
        if (null == tenantInfo || null == tenantInfo.getUserNumberLimit()) {
            return true;
        }
        return userUnitDao.countUserByTopUnit(topUnit) >= tenantInfo.getUserNumberLimit();
    }

    @Override
    public boolean unitNumberLimitIsOver(String topUnit) {
        TenantInfo tenantInfo = tenantInfoDao.getObjectById(topUnit);
        if (null == tenantInfo || null == tenantInfo.getUnitNumberLimit()) {
            return true;
        }
        return unitInfoDao.countUnitByTopUnit(topUnit) >= tenantInfo.getUnitNumberLimit();
    }

}
