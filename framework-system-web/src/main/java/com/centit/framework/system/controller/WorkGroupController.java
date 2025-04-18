package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.WorkGroup;
import com.centit.framework.model.basedata.WorkGroupParameter;
import com.centit.framework.system.constant.TenantConstant;
import com.centit.framework.system.dao.OsInfoDao;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.framework.system.vo.WorkGroupParames;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileLibraryAccess  Controller.
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * 项目库授权信息
 */

@Controller
@RequestMapping("/workGroup")
@Api(value = "APPLICATION_TEAM_USER", tags = "工作组管理接口")
public class WorkGroupController extends BaseController {

    @Autowired
    WorkGroupManager workGroupManager;
    @Autowired
    OsInfoDao osInfoDao;
    @Value("${tio.url:}")
    private String tioServer;

    /*
     * 查询所有   项目库授权信息  列表
     *
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "查询全部工作组")
    @WrapUpResponseBody
    public PageQueryResult<Object> list(HttpServletRequest request, PageDesc pageDesc) {
        List<WorkGroup> list = workGroupManager.listWorkGroup(BaseController.collectRequestParameters(request), pageDesc);
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        JSONArray jsonArray = new JSONArray();
        for (WorkGroup workGroup : list) {
            JSONObject jsonObject = JSONObject.from(workGroup);
            String userName = CodeRepositoryUtil.getUserName(topUnit, workGroup.getWorkGroupParameter().getUserCode());
            jsonObject.put("userName", userName);
            jsonArray.add(jsonObject);
        }
        return PageQueryResult.createResult(jsonArray, pageDesc);
    }

    /*
     * 查询查询租户中管理员列表
     *
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tenantAdminList")
    @ApiOperation(value = "查询租户中管理员")
    @WrapUpResponseBody
    public PageQueryResult tenantAdminList(HttpServletRequest request, PageDesc pageDesc) {
        if (StringUtils.isBlank(WebOptUtils.getCurrentUserCode(request))) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if (StringUtils.isBlank(topUnit)) {
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR,
                getI18nMessage( "error.403.user_not_in_tenant", request));
        }
        Map<String, Object> parameters = BaseController.collectRequestParameters(request);
        parameters.put("groupId", topUnit);
        // parameters.put("roleCode", TenantConstant.TENANT_ADMIN_ROLE_CODE);
        List<WorkGroup> list = workGroupManager.listWorkGroup(parameters, pageDesc);
        if (CollectionUtils.sizeIsEmpty(list)) {
            return PageQueryResult.createResult(list, pageDesc);
        }
        JSONArray jsonArray = new JSONArray();
        for (WorkGroup workGroup : list) {
            //补充用户信息 groupId对应租户topUnit
            HashMap<String, Object> map = JSONObject.from(workGroup);
            WorkGroupParameter workGroupParameter = workGroup.getWorkGroupParameter();
            UserInfo ui = CodeRepositoryUtil.getUserInfoByCode(topUnit, workGroupParameter.getUserCode());

            if (null != ui) {
                map.put("userName", ui.getUserName());
                map.put("loginName", ui.getLoginName());
                map.put("userWord", ui.getUserWord());
                map.put("regCellPhone" , ui.getRegCellPhone());
                map.put("regEmail" , ui.getRegEmail());
            }

            if(StringUtils.isNotBlank(workGroup.getRunToken())){
                map.put("unitCode", workGroup.getRunToken());
                map.put("unitName", CodeRepositoryUtil.getValue(
                    CodeRepositoryUtil.UNIT_CODE, workGroup.getRunToken(), topUnit, "zh-CN") );
            }
            if (TenantConstant.TENANT_ADMIN_ROLE_CODE.equals(workGroupParameter.getRoleCode())) {
                map.put("roleName", "管理员");
            } else {
                map.put("roleName", "组织管理员" );// TenantConstant.WORKGROUP_ROLE_CODE_MEMBER);
            }
            jsonArray.add(map);
        }
        return PageQueryResult.createResult(jsonArray, pageDesc);
    }

    @RequestMapping(value = "/{groupId}/{userCode}/{roleCode}", method = {RequestMethod.GET})
    @ApiOperation(value = "查询单个工作组成员")
    @WrapUpResponseBody
    public WorkGroup getWorkGroup(@PathVariable String groupId, @PathVariable String userCode, @PathVariable String roleCode) {
        return workGroupManager.getWorkGroup(groupId, userCode, roleCode);
    }


    /*
     * 新增 项目组成员
     *
     * @param workGroup {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.POST})
    @ApiOperation(value = "新增单个工作组成员")
    @WrapUpResponseBody
    public void createTeamUser(@RequestBody WorkGroup workGroup, HttpServletRequest request, HttpServletResponse response) {
        loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId(), request);
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)) {
            workGroup.setCreator(currentUserCode);//创建人  当前登录人
        }
        workGroup.getWorkGroupParameter().setRoleCode(TenantConstant.WORKGROUP_ROLE_CODE_MEMBER);
        workGroupManager.createWorkGroup(workGroup);
        JsonResultUtils.writeSingleDataJson(workGroup, response);
    }

    /*
     * 新增 项目组成员
     *
     * @param workGroups {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.POST}, value = "/batchAdd")
    @ApiOperation(value = "批量新增工作组成员")
    @WrapUpResponseBody
    public void batchCreateTeamUser(@RequestBody List<WorkGroup> workGroups, HttpServletRequest request, HttpServletResponse response) {
        if (workGroups == null || workGroups.size() == 0) {
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR,
                getI18nMessage("error.701.field_is_blank", request,"workGroup"));
        }
        for (WorkGroup workGroup : workGroups) {
            loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId(), request);
        }
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        for (WorkGroup workGroup : workGroups) {
            workGroup.getWorkGroupParameter().setRoleCode(TenantConstant.WORKGROUP_ROLE_CODE_MEMBER);
            workGroup.setCreator(currentUserCode);
        }
        workGroupManager.batchWorkGroup(workGroups);
        //对接谭聊
        if (StringUtils.isNotBlank(tioServer)) {
            try (CloseableHttpClient httpClient = HttpExecutor.createHttpClient()) {
                OsInfo osInfo = osInfoDao.getObjectById(workGroups.get(0).getGroupId());
                Map<String, Object> requestParams = new HashMap<>(10);
                if (osInfo != null) {
                    if (osInfo.getGroupId() == null) {
                        PageDesc pageDesc = new PageDesc();
                        pageDesc.setPageSize(-1);
                        List<WorkGroup> allWorkGroup = workGroupManager.listWorkGroup(CollectionsOpt.createHashMap(
                            "groupId", workGroups.get(0).getGroupId()), pageDesc);
                        StringBuilder users = new StringBuilder();
                        StringBuilder allUsers = new StringBuilder();
                        for (WorkGroup workGroup : allWorkGroup) {
                            if (workGroup.getRoleCode().equals(TenantConstant.WORKGROUP_ROLE_CODE_LEADER)) {
                                if (allUsers.length() > 0) {
                                    allUsers.append(",");
                                }
                                allUsers.append(workGroup.getUserCode());
                            } else {
                                if (users.length() > 0) {
                                    users.append(",");
                                }
                                users.append(workGroup.getUserCode());
                            }
                        }
                        allUsers.append(",").append(users);
                        requestParams.put("uidList", allUsers.toString());
                        requestParams.put("name", osInfo.getOsName());
                        HttpExecutorContext httpExecutorContext = HttpExecutorContext.create(httpClient);
                        HttpReceiveJSON valueOfJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext,
                            tioServer + "/chat/createGroup.tio_x", requestParams));
                        logger.info(valueOfJson.getDataAsString());
                        if (valueOfJson.getJSONObject("data") != null && valueOfJson.getJSONObject("data").get("id") != null) {
                            osInfo.setGroupId(valueOfJson.getJSONObject("data").getLong("id"));
                            osInfoDao.updateObject(new String[]{"groupId"}, osInfo);
                        }
                    } else {
                        StringBuilder users = new StringBuilder();
                        for (WorkGroup workGroup : workGroups) {
                            if (users.length() > 0) {
                                users.append(",");
                            }
                            users.append(workGroup.getUserCode());
                        }
                        requestParams.put("uids", users.toString());
                        requestParams.put("groupid", osInfo.getGroupId());
                        List<WorkGroup> leaderWorkGroup = workGroupManager.listWorkGroup(CollectionsOpt.createHashMap(
                            "groupId", workGroups.get(0).getGroupId(), "roleCode", TenantConstant.WORKGROUP_ROLE_CODE_LEADER), null);
                        if (leaderWorkGroup != null) {
                            requestParams.put("applyuid", leaderWorkGroup.get(0).getUserCode());
                        }
                        HttpReceiveJSON valueOfJson = HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(HttpExecutorContext.create(httpClient), tioServer + "/chat/joinGroup.tio_x", requestParams));
                        logger.info(valueOfJson.getDataAsString());
                    }
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        JsonResultUtils.writeSingleDataJson(workGroups, response);
    }

    /*
     * 删除单个  项目库授权信息
     */
    @RequestMapping(value = "/{groupId}/{userCode}", method = {RequestMethod.DELETE})
    @ApiOperation(value = "删除单个工作组成员")
    @WrapUpResponseBody
    public void deleteTeamUser(@PathVariable String groupId, @PathVariable String userCode, HttpServletRequest request) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY,
                getI18nMessage( "error.302.user_not_login", request));
        }
        WorkGroup workGroup = workGroupManager.getWorkGroup(groupId, loginUser, TenantConstant.WORKGROUP_ROLE_CODE_LEADER);
        if (workGroup == null || !TenantConstant.WORKGROUP_ROLE_CODE_LEADER.equals(workGroup.getWorkGroupParameter().getRoleCode())) {
            throw new ObjectException(ResponseData.ERROR_FORBIDDEN,
                getI18nMessage( "error.403.access_forbidden", request)); //"你非组长不能删除成员！");
        }
        if (loginUser.equals(userCode)) {
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                getI18nMessage( "error.702.operate_conflict", request)); //组长不能删除组长！");
        }
        workGroupManager.deleteWorkGroup(groupId, userCode, TenantConstant.WORKGROUP_ROLE_CODE_MEMBER);
    }

    /*
     * 更新 项目库授权信息
     *
     * @param workGroup {@link WorkGroup}
     */
    @RequestMapping(method = {RequestMethod.PUT})
    @ApiOperation(value = "更新单个工作组成员")
    @WrapUpResponseBody
    public void updateTeamUser(@RequestBody WorkGroup workGroup, HttpServletRequest request) {
        loginUserPermissionCheck(workGroup.getWorkGroupParameter().getGroupId(), request);
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isNotBlank(currentUserCode)) {
            workGroup.setUpdator(currentUserCode);//更新人  当前登录人
        }
        workGroup.getWorkGroupParameter().setRoleCode(TenantConstant.WORKGROUP_ROLE_CODE_MEMBER);
        workGroupManager.updateWorkGroup(workGroup);
    }

    /*
     * 组长移交
     */
    @RequestMapping(value = "hand-over", method = {RequestMethod.PUT})
    @ApiOperation(value = "移交组长")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void leaderHandOver(@RequestBody WorkGroupParames workGroupParames, HttpServletRequest request) {
        leaderHandOverPermissionCheck(workGroupParames.getGroupId(), request);
        workGroupManager.leaderHandOver(workGroupParames);
    }

    private void leaderHandOverPermissionCheck(String workGroupId, HttpServletRequest request) {
        if (StringUtils.isBlank(workGroupId)) {
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "groupId"));
        }
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(request, "userCode");
        }
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY,
                getI18nMessage( "error.302.user_not_login", request));
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if (StringUtils.isBlank(topUnit)) {
            throw new ObjectException(ResponseData.HTTP_UNAUTHORIZED,
                getI18nMessage( "error.403.access_forbidden", request));
        }
        Map<String, Object> filterMap = CollectionsOpt.createHashMap("groupId_in",
            new Object[]{workGroupId, topUnit}, "userCode", loginUser,
            "roleCode_in", new Object[]{"ZHGLY", "组长"});
        if (workGroupManager.countWorkGroup(filterMap) < 1) {
            throw new ObjectException(ResponseData.HTTP_UNAUTHORIZED,
                getI18nMessage( "error.403.access_forbidden", request));
        }
    }

    private void loginUserPermissionCheck(String osId, HttpServletRequest request) {
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        if (StringUtils.isBlank(loginUser)) {
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY,
                getI18nMessage( "error.302.user_not_login", request));
        }
        if (!workGroupManager.loginUserIsExistWorkGroup(osId, loginUser)) {
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION,
                getI18nMessage( "error.403.access_forbidden", request));
        }
    }
}
