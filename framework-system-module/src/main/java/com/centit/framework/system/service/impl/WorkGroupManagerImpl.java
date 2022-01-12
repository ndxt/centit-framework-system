package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.WorkGroupDao;
import com.centit.framework.system.po.WorkGroup;
import com.centit.framework.system.po.WorkGroupParameter;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
    }

    @Override
    public WorkGroup getWorkGroup(String groupId, String userCode, String roleCode) {
        return workGroupDao.getObjectById(new WorkGroupParameter(groupId, userCode, roleCode));
    }

    @Override
    public void createWorkGroup(WorkGroup workGroup) {
        workGroupDao.saveNewObject(workGroup);
    }

    @Override
    public void batchWorkGroup(List<WorkGroup> workGroups) {
        for (WorkGroup workGroup : workGroups) {
            workGroupDao.saveNewObject(workGroup);
        }
    }

    @Override
    public List<WorkGroup> listWorkGroup(Map<String, Object> param, PageDesc pageDesc) {
        return workGroupDao.listObjects(param, pageDesc);
    }

    @Override
    public boolean loginUserIsExistWorkGroup(String osId,String userCode) {
        if (StringUtils.isBlank(osId) || StringUtils.isBlank(userCode)){
            return false;
        }
        String[] osids = osId.split(",");
        Map<String, Object> param = new HashMap<>();
        param.put("groupId_in",osids);
        List<WorkGroup> workGroups = workGroupDao.listObjects(param, null);
        for (WorkGroup workGroup : workGroups) {
            if (workGroup.getWorkGroupParameter().getUserCode().equals(userCode)) {
                return true;
            }
        }
        return false;
    }
}

