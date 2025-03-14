package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.TenantInfo;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserUnit;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.TenantBusinessLog;
import com.centit.framework.system.po.TenantMemberApply;
import com.centit.framework.system.service.TenantPowerManage;
import com.centit.framework.system.service.TenantService;
import com.centit.framework.system.vo.PageListTenantInfoQo;
import com.centit.framework.system.vo.TenantMemberApplyVo;
import com.centit.framework.system.vo.TenantMemberQo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tenant")
@Api(
    tags = {"租户管理接口"},
    value = "租户管理接口"
)
public class TenantController extends BaseController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantPowerManage tenantPowerManage;

    @Autowired
    protected PlatformEnvironment platformEnvironment;

    public String getOptId() {
        return "TENANMAG";
    }
    @ApiOperation(
        value = "注册用户账号",
        notes = "注册用户账号,请求体(用户基本信息)"
    )
    @RequestMapping(value = "/registerUserAccount", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData registerUserAccount(@RequestBody @Validated UserInfo userInfo,
            HttpServletRequest request) {
        ResponseData responseData = tenantService.registerUserAccount(userInfo);
        return ResponseData.makeErrorMessage(responseData.getCode(),
            getI18nMessage(responseData.getMessage(), request));
    }

    @ApiOperation(
        value = "用户申请新建租户,目前租户申请后不需要管理员再次审核",
        notes = "用户申请新建租户,请求体(租户基本信息)"
    )
    @RequestMapping(value = "/applyAddTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData applyAddTenant(@RequestBody @Validated TenantInfo tenantInfo, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)) { //"您未登录!"
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage("error.302.user_not_login", request));
        }
        tenantInfo.setCreator(userCode);
        if (tenantPowerManage.userIsSystemMember(userCode)){
            if (StringUtils.isBlank(tenantInfo.getOwnUser())){
                throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    getI18nMessage("error.701.field_is_blank", request,"ownUser"));
            }
        }else {
            tenantInfo.setOwnUser(userCode);
        }
        ResponseData responseData = tenantService.applyAddTenant(userCode, tenantInfo);
        return ResponseData.makeErrorMessage(responseData.getCode(),
            getI18nMessage(responseData.getMessage(), request, (Object[]) responseData.getData()));
    }

    @ApiOperation(
        value = "申请加入租户",
        notes = "可以是用户主动申请，也可以是管理员邀请,请求体(租户成员申请信息)，***废弃接口"
    )
    @RequestMapping(value = "/applyJoinTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData applyJoinTenant(@RequestBody @Validated TenantMemberApply tenantMemberApply,
                        HttpServletRequest request) {
        UserInfo userInfo = WebOptUtils.assertUserLogin(request);
        tenantMemberApply.setInviterUserCode(userInfo.getUserCode());
        ResponseData responseData = tenantService.applyJoinTenant(tenantMemberApply);
        return ResponseData.makeErrorMessage(responseData.getCode(),
            getI18nMessage(responseData.getMessage(), request, (Object[]) responseData.getData()));
    }

    @ApiOperation(
        value = "用户申请加入租户",
        notes = "用户申请加入租户"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "topUnit",
        value = "租户id",
        paramType = "String",
        dataTypeClass = String.class)}
    )
    @RequestMapping(value = "/userApplyJoinTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData userApplyJoinTenant(@RequestBody TenantMemberApply tenantMemberApply,HttpServletRequest request) {
        UserInfo userInfo = WebOptUtils.assertUserLogin(request);
        if (StringUtils.isBlank(tenantMemberApply.getTopUnit())){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage("error.701.field_is_blank", request, "topUnit"));
        }
        tenantMemberApply.setUserCode(userInfo.getUserCode());
        tenantMemberApply.setInviterUserCode(userInfo.getUserCode());

        ResponseData responseData = tenantService.userApplyJoinTenant(tenantMemberApply);
        return ResponseData.makeErrorMessage(responseData.getCode(),
            getI18nMessage(responseData.getMessage(), request, (Object[]) responseData.getData()));
    }

    @ApiOperation(
        value = "管理员邀请用户加入租户",
        notes = "管理员邀请用户加入租户"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "unitCode",
        value = "部门code，非必填，传入之后，邀请人会被分配到改部门，不传则会分配到顶级机构",
        paramType = "String",
        dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "userCode",
            value = "用户code",
            paramType = "String",
            dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/adminApplyUserJoinTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData adminApplyUserJoinTenant(@RequestBody TenantMemberApply tenantMemberApply,HttpServletRequest request) {

        if (StringUtils.isBlank(tenantMemberApply.getUserCode())){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage("error.701.field_is_blank",request, "userCode"));
        }
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(currentUserCode)){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        tenantMemberApply.setTopUnit(WebOptUtils.getCurrentTopUnit(request));
        tenantMemberApply.setInviterUserCode(currentUserCode);

        ResponseData responseData = tenantService.adminApplyUserJoinTenant(tenantMemberApply);
        return ResponseData.makeErrorMessage(responseData.getCode(),
            getI18nMessage(responseData.getMessage(), request, (Object[]) responseData.getData()));
    }

    @ApiOperation(
        value = "列出申请信息",
        notes = "可以是管理员邀请的信息，也可以是用户主动申请的信息。" +
            "租户主动邀请用户加入，租户查看未审批的用户列表 applyType:2,topUnit= topUnit,applyState_in=[1,2]"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "applyType",
        value = "1:用户主动申请2:租户主动邀请",
        paramType = "String",
        dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "userCode/topUnit",
            value = "用户代码或机构代码[userCode=][topUnit=][unitCode=](topUnit和unitCode传入任意一个即可)",
            paramType = "String",
            dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "applyState",
            value = "审批类型 未审批：applyState_in=1,2,已审批：applyState_in=3,4,审批通过：applyState=3,不同意：applyState=4",
            paramType = "String",
            dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "pageDesc",
            value = "分页对象",
            paramType = "body",
            dataTypeClass = PageDesc.class
        )})
    @RequestMapping(value = "/listApplyInfo", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult listApplyInfo(HttpServletRequest request,
                                         PageDesc pageDesc) {
        return tenantService.listApplyInfo(collectRequestParameters(request), pageDesc);
    }

    @ApiOperation(
        value = "撤销申请",
        notes = "用户或者租户撤销申请或邀请 ***废弃接口"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "topUnit",
        value = "租户id",
        paramType = "String",
        dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "userCode",
            value = "用户code",
            paramType = "String",
            dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/cancelApply", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData cancelApply(HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        if (StringUtils.isAnyBlank(MapUtils.getString(parameters, "topUnit"),
            MapUtils.getString(parameters, "userCode"))) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage("error.701.field_is_blank", request,"topUnit,userCode"));
        }
        return tenantService.cancelApply(parameters);
    }

    @ApiOperation(value = "用户撤销申请",notes = "用户撤销申请")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "topUnit",
        value = "租户id",
        paramType = "String",
        dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/userCancelApply", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData userCancelApply(@RequestBody TenantMemberApply tenantMemberApply,HttpServletRequest request) {
        String topUnit = tenantMemberApply.getTopUnit();
        String topUnit2 = WebOptUtils.getCurrentTopUnit(request);
        if (StringUtils.isBlank(topUnit) || !StringUtils.equals(topUnit, topUnit2)){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(currentUserCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        Map<String, Object> parameters = CollectionsOpt.createHashMap("userCode", currentUserCode, "topUnit", topUnit);
        return tenantService.cancelApply(parameters);
    }

    @ApiOperation(
        value = "租户撤销邀请",
        notes = "租户撤销邀请"
    )
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode",
            value = "用户code",
            paramType = "String",
            dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/adminCancelApply", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData adminCancelApply(@RequestBody TenantMemberApply tenantMemberApply, HttpServletRequest request) {
        String userCode = tenantMemberApply.getUserCode();
        if (StringUtils.isBlank(userCode)){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage("error.701.field_is_blank", request,"userCode"));
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if (!tenantPowerManage.userIsTenantAdmin(WebOptUtils.getCurrentUserCode(request), topUnit)){
            throw new ObjectException(ResponseData.HTTP_UNAUTHORIZED,
                getI18nMessage("error.401.unauthorized", request));
        }
        Map<String, Object> parameters = CollectionsOpt.createHashMap("userCode",userCode, "topUnit",  topUnit);
        return tenantService.cancelApply(parameters);
    }

    @ApiOperation(
        value = "注销租户",
        notes = "注销租户，只有租户所有者才可以操作"
    )
    @RequestMapping(value = "/deleteTenant", method = RequestMethod.PUT)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}注销租户",tag = "{userCodes}")
    public ResponseData deleteTenant(HttpServletRequest request) {
        Map<String, Object> parameters = collectRequestParameters(request);
        if (StringUtils.isBlank(MapUtils.getString(parameters, "topUnit"))) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        parameters.put("userCode",userCode);
        logger.info("用户:{}注销租户{}信息",userCode,MapUtils.getString(parameters, "topUnit"));
        return tenantService.deleteTenant(userCode, parameters);
    }

    @ApiOperation(
        value = "用户同意加入租户",
        notes = "用户同意加入租户"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "topUnit",
        value = "租户id",
        paramType = "String",
        dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "applyState",
            value = "申请状态 3：同意加入 4：不同意加入",
            paramType = "String",
            dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/userAgreeJoin", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData userAgreeJoin(@RequestBody TenantMemberApplyVo tenantMemberApply, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        if (StringUtils.isBlank(tenantMemberApply.getTopUnit())){
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        tenantMemberApply.setUserCode(userCode);
        return tenantService.agreeJoin(tenantMemberApply);
    }

    @ApiOperation(
        value = "管理员同意用户加入租户",
        notes = "管理员同意用户加入租户"
    )
    @ApiImplicitParams({@ApiImplicitParam(
        name = "applyState",
        value = "是否通过 3：通过 4：不通过",
        paramType = "String",
        dataTypeClass = String.class),
        @ApiImplicitParam(
            name = "userCode",
            value = "用户code",
            paramType = "String",
            dataTypeClass = String.class)
    }
    )
    @RequestMapping(value = "/adminAgreeJoin", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData adminAgreeJoin(@RequestBody TenantMemberApplyVo tenantMemberApply, HttpServletRequest request) {
        if (StringUtils.isBlank(WebOptUtils.getCurrentUserCode(request))){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(topUnit) || !tenantPowerManage.userIsTenantAdmin(currentUserCode, topUnit)){
            return ResponseData.makeErrorMessage(ResponseData.HTTP_UNAUTHORIZED,
                getI18nMessage( "error.401.unauthorized", request));
        }
        tenantMemberApply.setTopUnit(topUnit);
        return tenantService.agreeJoin(tenantMemberApply);
    }

    @ApiOperation(
        value = "平台管理员审核租户",
        notes = "平台管理员审核租户，请求体(租户信息)---暂时没用到"
    )
    @RequestMapping(value = "/adminCheckTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData adminCheckTenant(@RequestBody TenantInfo tenantInfo,HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,ResponseData.ERROR_NOT_LOGIN_MSG);
        }
        tenantInfo.setUpdator(userCode);
        return tenantService.adminCheckTenant(userCode, tenantInfo);
    }

    @ApiOperation(
        value = "更新用户当前机构信息",
        notes = "更新用户当前机构信息，请求体(用户信息)"
    )
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.PUT)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户信息",tag = "{userCodes}")
    public ResponseData updateUserCurrentUnit(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if(!StringUtils.equals(userCode, userInfo.getUserCode())){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR,
                "只有用户自己才能修改自己的信息！");
        }
        try {
            return tenantService.updateUserCurrentUnit(userInfo);
        } catch (Exception e) {
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,
                "更新人员信息失败。失败原因：" + e.getMessage() );
        }
    }

    @ApiOperation(
        value = "退出租户",
        notes = "退出租户，请求示例：{\"topUnit\":\"f0c0368da826434bbb158ed2ef0b1726\"}"
    )
    @RequestMapping(value = "/quitTenant", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData quitTenant(@RequestBody Map<String, Object> paraMaps,HttpServletRequest request) {
        //paraMaps 也可以通过 collectRequestParameters(request)获取
        String topUnit = MapUtils.getString(paraMaps, "topUnit");
        if (StringUtils.isBlank(topUnit)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_USER_NOT_LOGIN,
                getI18nMessage( "error.302.user_not_login", request));
        }
        try {
            return tenantService.quitTenant(topUnit, userCode);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("退出租户失败。失败原因：{},入参：userCode={},topUnit={}", e, userCode, topUnit);
        }
        return ResponseData.errorResponse;
    }

    @ApiOperation(
        value = "把成员移除租户",
        notes = "把成员移除租户，请求示例：{\"userCode\":\"U6n6uge0\",\"topUnit\":\"f0c0368da826434bbb158ed2ef0b1726\"}"
    )
    @RequestMapping(value = "/removeTenantMember", method = RequestMethod.PUT)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}把成员移除租户",tag = "{userCodes}")
    public ResponseData removeTenantMember(@RequestBody Map<String, Object> paraMaps, HttpServletRequest request) {
        String topUnit = MapUtils.getString(paraMaps, "topUnit");
        String userCode = MapUtils.getString(paraMaps, "userCode");
        if (StringUtils.isAnyBlank(topUnit, userCode)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit,userCode"));
        }
        try {
            return tenantService.removeTenantMember(topUnit, userCode);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("移除租户失败。失败原因：{},入参：userCode={},topUnit={}", e, userCode, topUnit);
        }
        return ResponseData.errorResponse;
    }

    @ApiOperation(
        value = "租户转让申请",
        notes = "租户转让申请，请求体(租户转让记录信息)"
    )
    @RequestMapping(value = "/businessTenant", method = RequestMethod.POST)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}租户转让申请",tag = "{userCodes}")
    public ResponseData businessTenant(@RequestBody @Validated TenantBusinessLog tenantBusinessLog,
                                       HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return tenantService.businessTenant(userCode, tenantBusinessLog);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("租户转让申请失败。失败原因：{},入参：tenantBusinessLog={}", e, tenantBusinessLog.toString());
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_PROCESS_FAILED,
           getI18nMessage("error.704.process_failed", request)); //
    }

    @ApiOperation(
        value = "分页展示租户列表",
        notes = "分页展示租户列表"
    )
    @RequestMapping(value = "/pageListTenantApply", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult pageListTenantApply(PageListTenantInfoQo tenantInfo, PageDesc pageDesc,
                                               HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        return tenantService.pageListTenantApply(userCode, tenantInfo, pageDesc);
    }

    @ApiOperation(
        value = "分页展示租户成员列表",
        notes = "分页展示租户成员列表"
    )
    @RequestMapping(value = "/pageListTenantMember", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult pageListTenantMember(HttpServletRequest request, PageDesc pageDesc) {
        Map<String, Object> parameters = collectRequestParameters(request);
        return tenantService.pageListTenantMember(parameters, pageDesc);
    }

    @ApiOperation(
        value = "设置租户成员角色",
        notes = "设置租户成员角色"
    )
    @RequestMapping(value = "/assignTenantRole", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData assignTenantRole(@RequestBody TenantMemberQo tenantMemberQo, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return tenantService.assignTenantRole(userCode, tenantMemberQo);
        } catch (ObjectException obe) {
            return ResponseData.makeErrorMessage(obe.getExceptionCode(), obe.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("设置租户成员角色出错，错误原因:{},入参:{}", e, tenantMemberQo.toString());
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_PROCESS_FAILED,
            getI18nMessage("error.704.process_failed", request)); //"设置租户成员角色出错");

    }

    @ApiOperation(
        value = "移除租户成员角色",
        notes = "移除租户成员角色"
    )
    @RequestMapping(value = "/deleteTenantRole", method = RequestMethod.DELETE)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}移除租户成员角色",tag = "{userCodes}")
    public ResponseData deleteTenantRole(TenantMemberQo tenantMemberQo, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return tenantService.deleteTenantRole(userCode, tenantMemberQo);
        } catch (ObjectException obe) {
            return ResponseData.makeErrorMessage(obe.getExceptionCode(), obe.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("删除租户成员角色出错，错误原因:{},入参:{}", e, tenantMemberQo.toString());
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_PROCESS_FAILED,
            getI18nMessage("error.704.process_failed", request)); //"删除租户成员角色出错");

    }

    @ApiOperation(
        value = "获取用户所在租户",
        notes = "获取用户所在租户"
    )
    @RequestMapping(value = "/userTenants", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userTenants(HttpServletRequest request) {

        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(userCode)) {
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录!");
        }
        try {
            JSONArray userTenants = tenantService.userTenants(userCode);
            return ResponseData.makeResponseData(userTenants);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户所在租户出错，错误原因:{},入参:{}", e, userCode);
        }
        return ResponseData.makeErrorMessage(ResponseData.ERROR_PROCESS_FAILED,
            getI18nMessage("error.704.process_failed", request)); //"获取用户所在租户出错");

    }

    @ApiOperation(
        value = "查询租户信息",
        notes = "根据unitName精确查询租户信息"
    )
    @RequestMapping(value = "/pageListTenants", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult pageListTenants( PageDesc pageDesc,HttpServletRequest request) {
        Map<String, Object> map = collectRequestParameters(request);
        if (StringUtils.isBlank(MapUtils.getString(map,"unitName"))) {
            //租户名称为空时，直接返回空值
            JSONArray jsonArray = new JSONArray();
            return PageQueryResult.createResult(jsonArray, pageDesc);
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map,"otherTenant"))){
            map.put("userCode", WebOptUtils.getCurrentUserCode(request));
        }
        return tenantService.pageListTenants(map, pageDesc);

    }

    @ApiOperation(
        value = "查询用户信息",
        notes = "查询租户信息，只能根据userCode，userName，regCellPhone精确查找，unitCode:必传 当前用户所在租户topUnit"
    )
    @RequestMapping(value = "/findUsers", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData findUsers(HttpServletRequest request) {
        Map<String, Object> paramMap = collectRequestParameters(request);
        return tenantService.findUsers(paramMap);
    }

    @ApiOperation(
        value = "查询用户信息",
        notes = "查询租户信息，只能根据userCode，userName，regCellPhone精确查找"
    )
    @RequestMapping(value = "/searchUsers", method = RequestMethod.GET)
    @WrapUpResponseBody
    public List<UserInfo> searchUsers(HttpServletRequest request) {
        Map<String, Object> paramMap = collectRequestParameters(request);
        return tenantService.searchUsers(paramMap);
    }

    @ApiOperation(
        value = "修改租户信息",
        notes = "修改租户信息"
    )
    @RequestMapping(value = "/updateTenant", method = RequestMethod.PUT)
    @WrapUpResponseBody
    //@RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}修改租户信息",tag = "{userCodes}")
    public ResponseData updateTenant(@RequestBody TenantInfo tenantInfo, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        return tenantService.updateTenant(userCode, tenantInfo);

    }

    @ApiOperation(value = "新建机构", notes = "新建一个机构。")
    @ApiImplicitParam(
        name = "unitInfo", value = "json格式，机构信息对象",
        paramType = "body", dataTypeClass = UnitInfo.class)
    @RequestMapping(value = {"/addTenantUnit"},method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增机构",
        tag="{ui.unitCode}:{ui.unitName}")
    @WrapUpResponseBody
    public ResponseData addTenantUnit(@ParamName("ui")@Valid UnitInfo unitInfo,HttpServletRequest request) {
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if (StringUtils.isBlank(topUnit)){
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        unitInfo.setTopUnit(topUnit);
        return tenantService.addTenantUnit(unitInfo);

    }

    @ApiOperation(value = "新增用户", notes = "新增用户。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userInfo", value = "json格式，用户对象信息",
            paramType = "body", dataTypeClass = UserInfo.class),
        @ApiImplicitParam(
            name = "userUnit", value = "json格式，用户机构对象信息",
            paramType = "body", dataTypeClass = UserUnit.class)
    })
    @RequestMapping(value = {"/addTenantUser"},method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增用户",
        tag = "{us.userCode}")
    @WrapUpResponseBody
    public ResponseData addTenantUser(@ParamName("us") @Valid UserInfo userInfo, UserUnit userUnit, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if (null == userUnit || StringUtils.isBlank(userUnit.getTopUnit())){
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                getI18nMessage( "error.701.field_is_blank", request, "topUnit"));
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if(!StringUtils.equals(userUnit.getTopUnit(), topUnit)){
            return ResponseData.makeErrorMessage(403, "只能添加本租户下的用户!");
        }

        userUnit.setCreator(userCode);
        return tenantService.addTenantUser(userInfo, userUnit);

    }
}
