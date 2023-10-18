package com.centit.framework.system.service.impl;

import com.centit.framework.model.basedata.UserRole;
import com.centit.framework.model.basedata.WorkGroup;
import com.centit.framework.model.basedata.WorkGroupParames;
import com.centit.framework.model.basedata.WorkGroupParameter;
import com.centit.framework.system.dao.UserRoleDao;
import com.centit.framework.system.dao.WorkGroupDao;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作组
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkGroupManagerImpl implements WorkGroupManager {


    @Autowired
    @NotNull
    private WorkGroupDao workGroupDao;
    @Autowired
    private UserRoleDao userRoleDao;


    @Override
    public void updateWorkGroup(WorkGroup workGroup) {
        if (StringUtils.isNotBlank(workGroup.getRoleCode())) {
            //修改复合主键值的情况
            WorkGroup work = getWorkGroup(workGroup.getWorkGroupParameter().getGroupId(),
                workGroup.getWorkGroupParameter().getUserCode(),
                workGroup.getWorkGroupParameter().getRoleCode());
            if (work != null) {
                workGroupDao.deleteObject(work);
                workGroup.getWorkGroupParameter().setRoleCode(workGroup.getRoleCode());
                workGroupDao.saveNewObject(workGroup);
            }
        } else {
            //正常修改，不修改复合主键的情况下
            workGroupDao.updateObject(workGroup);
        }
    }

    @Override
    public void deleteWorkGroup(String groupId, String userCode, String roleCode) {
        workGroupDao.deleteObjectForceById(new WorkGroupParameter(groupId, userCode, roleCode));
        userRoleDao.deleteByRoleCodeAndUserCode(UserRole.OS_MEMBER,userCode);
    }

    @Override
    public WorkGroup getWorkGroup(String groupId, String userCode, String roleCode) {
        return workGroupDao.getObjectById(new WorkGroupParameter(groupId, userCode, roleCode));
    }

    @Override
    public void createWorkGroup(WorkGroup workGroup) {
        workGroupDao.saveNewObject(workGroup);
        UserRole userRole = getUserRole(workGroup);
        userRoleDao.mergeUserRole(userRole);
    }


    @Override
    public void batchWorkGroup(List<WorkGroup> workGroups) {
        for (WorkGroup workGroup : workGroups) {
            workGroupDao.saveNewObject(workGroup);
            UserRole userRole = getUserRole(workGroup);
            userRoleDao.mergeUserRole(userRole);
        }
    }

    @Override
    public List<WorkGroup> listWorkGroup(Map<String, Object> param, PageDesc pageDesc) {
        return workGroupDao.listObjectsByProperties(param, pageDesc);
    }

    @Override
    public int countWorkGroup(Map<String, Object> param) {

        return workGroupDao.countObjectByProperties(param);
    }

    @Override
    public boolean loginUserIsExistWorkGroup(String osId, String userCode) {
        if (StringUtils.isBlank(osId) || StringUtils.isBlank(userCode)) {
            return false;
        }
        String[] osids = osId.split(",");
        Map<String, Object> param = new HashMap<>();
        param.put("groupId_in", osids);
        List<WorkGroup> workGroups = workGroupDao.listObjectsByProperties(param);
        for (WorkGroup workGroup : workGroups) {
            if (workGroup.getWorkGroupParameter().getUserCode().equals(userCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void leaderHandOver(WorkGroupParames workGroupParames) {
        //组长更新为组员(只能有一个组长)
        String sql = "UPDATE  work_group  SET ROLE_CODE ='组员'  WHERE group_id=? and role_code=?  ";
        workGroupDao.getJdbcTemplate().update(sql,
            new Object[]{workGroupParames.getGroupId(), WorkGroup.WORKGROUP_ROLE_CODE_LEADER});

        //删除原始组员数据
        WorkGroup delWorkGroup = new WorkGroup();
        WorkGroupParameter delWorkGroupParameter = new WorkGroupParameter();
        delWorkGroupParameter.setGroupId(workGroupParames.getGroupId());
        delWorkGroupParameter.setRoleCode(WorkGroup.WORKGROUP_ROLE_CODE_MEMBER);
        delWorkGroupParameter.setUserCode(workGroupParames.getNewUserCode());
        delWorkGroup.setWorkGroupParameter(delWorkGroupParameter);
        workGroupDao.deleteObject(delWorkGroup);
        //新增组长信息
        WorkGroup workGroup = new WorkGroup();
        WorkGroupParameter workGroupParameter = new WorkGroupParameter();
        workGroupParameter.setGroupId(workGroupParames.getGroupId());
        workGroupParameter.setRoleCode(WorkGroup.WORKGROUP_ROLE_CODE_LEADER);
        workGroupParameter.setUserCode(workGroupParames.getNewUserCode());
        workGroup.setWorkGroupParameter(workGroupParameter);
        workGroupDao.saveNewObject(workGroup);
    }

    private UserRole getUserRole(WorkGroup workGroup) {
        UserRole userRole = new UserRole();
        userRole.setUserCode(workGroup.getWorkGroupParameter().getUserCode());
        userRole.setObtainDate(new Date());
        userRole.setChangeDesc("工作组自动分配");
        userRole.setRoleCode(UserRole.OS_MEMBER);
        return userRole;
    }
}

