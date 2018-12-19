package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserUnit;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "框架中用户管理接口，包括用户的增删改查", tags = "用户管理操作接口")
@Controller
@RequestMapping("/userinfo")
public class UserInfoController extends BaseController {
    @Resource
    @NotNull
    private SysUserManager sysUserManager;

    @Resource
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Resource
    @NotNull
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "USERMAG";//CodeRepositoryUtil.getCode("OPTID", "userInfo");
    public String getOptId() {
        return "USERMAG";
    }

    /**
     * 查询所有用户信息
     *
     * @param field    显示结果中只需要显示的字段
     * @param pageDesc PageDesc
     * @param _search  _search
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "用户信息分页查询", notes = "查询用户信息")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "filterMap", value = "过滤条件",
        paramType = "query", dataType = "Map"
    ), @ApiImplicitParam(
        name = "pageDesc", value = "json格式的分页信息",
        paramType = "body", dataTypeClass = PageDesc.class
    ), @ApiImplicitParam(
        name = "_search", value = "强制关闭分页查询",
        paramType = "query", dataType = "Boolean"
    ), @ApiImplicitParam(
        name = "field", value = "过滤返回的字段信息",
        allowMultiple = true, paramType = "query", dataType = "String"
    )})
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData list(String[] field, PageDesc pageDesc, String _search, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        //特殊字符转义
        if (searchColumn.get("userName") != null) {
            searchColumn.put("likeUserOrLoginName", StringEscapeUtils.escapeHtml4(searchColumn.get("userName").toString()));
            searchColumn.remove("userName");
        }
        List<UserInfo> listObjects = null;
        if (Boolean.parseBoolean(_search)) {
            listObjects = sysUserManager.listObjects(searchColumn);
            pageDesc = null;
        } else {
            listObjects = sysUserManager.listObjects(searchColumn, pageDesc);
        }
        JSONArray jsonArr = DictionaryMapUtils.objectsToJSONArray(listObjects);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(UserInfo.class, field);
        }
        ResponseMapData resData = new ResponseMapData();
        if (null == pageDesc) {
            resData.addResponseData(BaseController.OBJLIST, listObjects);
            resData.toJSONString(simplePropertyPreFilter);
            return ResponseData.makeResponseData(resData.getResponseData(BaseController.OBJLIST));
        }

        resData.addResponseData(BaseController.OBJLIST, jsonArr);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        resData.toJSONString(simplePropertyPreFilter);

        return resData;
    }

    /**
     * 新增用户
     *
     * @param userInfo UserInfo
     * @param userUnit 用户机构
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "新增用户", notes = "新增用户。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userInfo", value = "json格式，用户对象信息",
            paramType = "body", dataTypeClass = UserInfo.class),
        @ApiImplicitParam(
            name = "userUnit", value = "json格式，用户机构对象信息",
            paramType = "body", dataTypeClass = UserUnit.class)
    })
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增用户")
    @WrapUpResponseBody
    public ResponseData create(@Valid UserInfo userInfo, UserUnit userUnit, HttpServletRequest request) {

        UserInfo dbuserinfo = sysUserManager.loadUserByLoginname(userInfo.getLoginName());
        if (null != dbuserinfo) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                "登录名" + userInfo.getLoginName() + "已存在，请更换！");
        }

        if (null != userInfo.getUserRoles()) {
            for (UserRole ur : userInfo.getUserRoles()) {
                ur.setUserCode(userInfo.getUserCode());
            }
        }
        userUnit.setCreator(getLoginUserCode(request));
        userUnit.setUserCode(userInfo.getUserCode());
        sysUserManager.saveNewUserInfo(userInfo, userUnit);

        return ResponseData.makeResponseData(userInfo);

        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId,userInfo.getUserCode(),
//                OperationLog.P_OPT_LOG_METHOD_C,  "新增用户", userInfo);
    }

    /**
     * 更新用户信息
     *
     * @param userCode userCode
     * @param userInfo userInfo
     * @param userUnit userUnit
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码", required = true,
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "userInfo", value = "json格式，用户对象信息",
            paramType = "body", dataTypeClass = UserInfo.class),
        @ApiImplicitParam(
            name = "userUnit", value = "json格式，用户机构对象信息",
            paramType = "body", dataTypeClass = UserUnit.class)
    })
    @RequestMapping(value = "/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户信息")
    @WrapUpResponseBody
    public ResponseData edit(@PathVariable String userCode, @Valid UserInfo userInfo, UserUnit userUnit,
                             HttpServletRequest request) {

        UserInfo dbUserInfo = sysUserManager.getObjectById(userCode);
        if (null == dbUserInfo) {
            return ResponseData.makeErrorMessage("当前用户不存在");
        }

        sysUserUnitManager.deletePrimaryUnitByUserCode(userCode);
        userUnit.setUserCode(userInfo.getUserCode());
        userUnit.setUnitCode(userInfo.getPrimaryUnit());
        userUnit.setIsPrimary("T");
        userUnit.setCreator(getLoginUserCode(request));
        sysUserUnitManager.saveNewUserUnit(userUnit);

        if (StringUtils.isBlank(userInfo.getUserPin())) {
            userInfo.setUserPin(dbUserInfo.getUserPin());
        }

        sysUserManager.updateUserInfo(userInfo);
        return ResponseData.makeSuccessResponse();

    }

    /**
     * 当前登录用户信息
     *
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "当前登录用户信息", notes = "当前登录用户信息。")
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getCurrentUserInfo(HttpServletRequest request) {
        CentitUserDetails userDetails = super.getLoginUser(request);
        UserInfo userinfo = sysUserManager.getObjectById(userDetails.getUserInfo().getUserCode());
        return ResponseData.makeResponseData(userinfo);
    }

    /**
     * 获取单个用户信息
     *
     * @param userCode 用户代码
     */
    @ApiOperation(value = "获取单个用户信息", notes = "根据用户代码获取单个用户信息。")
    @ApiImplicitParam(
        name = "userCode", value = "用户代码",
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseMapData getUserInfo(@PathVariable String userCode) {
        UserInfo userInfo = sysUserManager.getObjectById(userCode);
        UserUnit userUnit = sysUserUnitManager.getPrimaryUnitByUserCode(userCode);
        //针对输入框类型做的反转译
        userInfo.setUserCode(StringEscapeUtils.unescapeHtml4(userInfo.getUserCode()));
        userInfo.setLoginName(StringEscapeUtils.unescapeHtml4(userInfo.getLoginName()));
        userInfo.setUserWord(StringEscapeUtils.unescapeHtml4(userInfo.getUserWord()));
        userInfo.setEnglishName(StringEscapeUtils.unescapeHtml4(userInfo.getEnglishName()));
        userInfo.setUserName(StringEscapeUtils.unescapeHtml4(userInfo.getUserName()));
        userInfo.setUserDesc(StringEscapeUtils.unescapeHtml4(userInfo.getUserDesc()));

        Object userJson = DictionaryMapUtils.objectToJSON(userInfo);
        Object userUnitJson = DictionaryMapUtils.objectToJSON(userUnit);
        ResponseMapData responseData = new ResponseMapData();
        responseData.addResponseData("userInfo", userJson);
        responseData.addResponseData("userUnit", userUnitJson);

        Map<Class<?>, String[]> excludes = new HashMap<>();
        excludes.put(UserUnit.class, new String[]{"userInfo"});
        excludes.put(UserRole.class, new String[]{"userInfo"});

        responseData.toJSONString(JsonPropertyUtils.getExcludePropPreFilter(excludes));
        return responseData;
    }


    /**
     * 当前登录名是否已存在
     *
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "当前登录名是否已存在", notes = "当前登录名是否已存在。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码",
            paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "loginName", value = "登录名",
            paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "regPhone", value = "手机号",
            paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "regEmail", value = "邮箱",
            paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isAnyExist(HttpServletRequest request) {
        String userCode = request.getParameter("userCode");
        String loginName = request.getParameter("loginName");
        String regPhone = request.getParameter("regPhone");
        String regEmail = request.getParameter("regEmail");
        return sysUserManager.isAnyOneExist(userCode, loginName, regPhone, regEmail);
    }

    /**
     * 当前登录名是否已存在
     *
     * @param loginName 登录名
     */
    @ApiOperation(value = "当前登录名是否已存在", notes = "当前登录名是否已存在。")
    @ApiImplicitParam(
        name = "loginName", value = "登录名", required = true,
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/exists/{loginName}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isExists(@PathVariable String loginName) throws IOException {
        UserInfo userInfo = sysUserManager.loadUserByLoginname(loginName);
        return null != userInfo;
    }

    /**
     * 更新用户密码
     *
     * @param userCode    用户代码
     * @param password    旧密码
     * @param newPassword 新密码
     */
    @ApiOperation(value = "更新用户密码", notes = "更新用户密码。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码", required = true,
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "password", value = "旧密码", required = true,
            paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "newPassword", value = "新密码", required = true,
            paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/change/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户密码")
    @WrapUpResponseBody
    public ResponseData changePwd(@PathVariable String userCode, String password, String newPassword) {

        sysUserManager.setNewPassword(userCode, password, newPassword);

        return ResponseData.makeSuccessResponse();

        /*********log*********/
        //OperationLogCenter.log(request,optId,userCode, "changePassword", "更新用户密码,用户代码:" + userCode);
        /*********log*********/
    }

    /**
     * 强制更新用户密码
     *
     * @param userCode 用户代码
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "强制更新用户密码", notes = "强制更新用户密码。")
    @ApiImplicitParam(
        name = "userCode", value = "用户代码", required = true,
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/changePwd/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}强制更新用户密码")
    @WrapUpResponseBody
    public ResponseData forceChangePwd(@PathVariable String userCode, HttpServletRequest request) {
        String newPassword = request.getParameter("newPassword");
        if (StringUtils.isBlank(newPassword)) {
            sysUserManager.resetPwd(userCode);
        } else {
            sysUserManager.forceSetPassword(userCode, newPassword);
        }

        return ResponseData.makeSuccessResponse();
        /*********log*********/
        //OperationLogCenter.log(request,optId,userCode, "forceChangePwd", "更新用户密码,用户代码:" + userCode);
        /*********log*********/
    }

    /**
     * 检查用户密码是否可以修改
     *
     * @param userCode    用户代码
     * @param oldPassword 旧密码
     */
    @ApiOperation(value = "检查用户密码是否可以修改", notes = "检查用户密码是否可以修改。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码", required = true,
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "oldPassword", value = "旧密码", required = true,
            paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/canchange/{userCode}/{oldPassword}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData canChangePwd(@PathVariable String userCode, @PathVariable String oldPassword) {
        boolean bo = true;
        bo = sysUserManager.checkUserPassword(userCode, oldPassword);
        return ResponseData.makeResponseData(bo);
    }

    /**
     * 批量重置密码
     *
     * @param userCodes 用户代码集合
     */
    @ApiOperation(value = "批量重置密码", notes = "批量重置密码。")
    @ApiImplicitParam(
        name = "userCodes", value = "用户代码集合(数组)", allowMultiple = true,
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}重置用户密码")
    @WrapUpResponseBody
    public ResponseData resetBatchPwd(String[] userCodes) {
        if (ArrayUtils.isEmpty(userCodes)) {
            return ResponseData.makeErrorMessage("用户代码集合为空");
        }
        sysUserManager.resetPwd(userCodes);
        return ResponseData.makeSuccessResponse();

        /*********log*********/
        //OperationLogCenter.logNewObject(request,optId,null, "resetPassword",  "批量重置密码",userCodes);
    }

    /**
     * 删除用户
     *
     * @param userCodes 用户代码
     */
    @ApiOperation(value = "批量删除用户", notes = "批量删除用户。")
    @ApiImplicitParam(
        name = "userCodes", value = "用户代码集合(数组)", allowMultiple = true,
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userCodes}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户")
    @WrapUpResponseBody
    public ResponseData deleteUser(@PathVariable String[] userCodes) {
        for (String userCode : userCodes) {
            UserInfo userInfo = sysUserManager.getObjectById(userCode);
            if (null != userInfo) {
                sysUserManager.deleteUserInfo(userCode);
            } else {
                return ResponseData.makeErrorMessage("该用户不存在");
            }

            /*********log*********/
//            OperationLogCenter.logDeleteObject(request, optId, userCode, OperationLog.P_OPT_LOG_METHOD_D,
//                    "删除用户"+userInfo.getUserName(), userInfo);
            /*********log*********/
        }
        return ResponseData.makeSuccessResponse();
    }

}
