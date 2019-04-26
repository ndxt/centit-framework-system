package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户设置
 *
 * @author sx
 * 2014年10月14日
 */
@Controller
@RequestMapping("/usersetting")
@Api(value = "用户设置操作维护接口。", tags = "用户设置操作接口")
public class UserSettingController extends BaseController {
    @Resource
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    public String getOptId() {
        return "userSetting";
    }

    /**
     * 查询当前用户所有的用户参数设置信息
     *
     * @param pageDesc pageDesc
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "查询当前用户所有的用户参数设置信息", notes = "查询当前用户所有的用户参数设置信息。")
    @ApiImplicitParam(
        name = "pageDesc", value = "json格式的分页对象信息",
        paramType = "body", dataTypeClass = PageDesc.class)
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<Object> list(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        searchColumn.put(CodeRepositoryUtil.USER_CODE, WebOptUtils.getCurrentUserCode(request));
        JSONArray listObjects = userSettingManager.listObjects(searchColumn, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc);
    }

    /**
     * 查询用户个人设置列表
     *
     * @param userCode 用户代码
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "查询用户个人设置列表", notes = "查询用户个人设置列表")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "userCode", value = "用户代码",
        required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
        name = "pageDesc", value = "json格式的分页对象信息",
        paramType = "body", dataTypeClass = PageDesc.class
    )})
    @GetMapping(value = "/list/{userCode}")
    @WrapUpResponseBody
    public ResponseData listUserSetting(@PathVariable String userCode, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userCode);

        JSONArray listObjects = userSettingManager.listObjects(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        return resData;
    }

    /**
     * 查询用户个人默认设置列表
     *
     * @param pageDesc 分页信息
     * @param request  {@link HttpServletRequest}
     * @return ResponseData ajax 请求
     */
    @ApiOperation(value = "查询用户个人默认设置列表", notes = "查询用户个人默认设置列表。")
    @ApiImplicitParam(
        name = "pageDesc", value = "json格式的分页对象信息",
        paramType = "body", dataTypeClass = PageDesc.class)
    @GetMapping(value = "/listdefault")
    @ResponseBody
    public ResponseData listUserDefaultSetting(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        List<UserSetting> listObjects = userSettingManager.listDefaultSettings(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        return resData;
    }

    /**
     * 查询当前用户所有个人设置 不分页
     *
     * @param field   需要返回的字段
     * @param request {@link HttpServletRequest}
     */
    @ApiOperation(value = "查询当前用户所有个人设置 不分页", notes = "查询当前用户所有个人设置 不分页。")
    @ApiImplicitParam(
        name = "field", value = "需要返回的字段",
        allowMultiple = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "/listall", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listAll(String[] field, HttpServletRequest request) {
        Map<String, Object> searchColumn = new HashMap<>();
        String userCode = WebOptUtils.getCurrentUserCode(request);
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userCode);

        List<UserSetting> listObjects = userSettingManager.listObjects(searchColumn);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(UserSetting.class, field);
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.toJSONString(simplePropertyPreFilter);
        return resData;
    }

    /**
     * 获取当前用户设置的参数
     *
     * @param paramCode paramCode
     * @param request   {@link HttpServletRequest}
     */
    @ApiOperation(value = "获取当前用户设置的参数", notes = "获取当前用户设置的参数。")
    @ApiImplicitParam(
        name = "paramCode", value = "参数代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{paramCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUserSetting(@PathVariable String paramCode, HttpServletRequest request) {
        UserSettingId id = new UserSettingId(WebOptUtils.getCurrentUserCode(request), paramCode);

        UserSetting userSetting = userSettingManager.getObjectById(id);
        if (null != userSetting) {
            userSetting.setParamValue(HtmlUtils.htmlUnescape(userSetting.getParamValue()));
        }

        return ResponseData.makeResponseData(userSetting);
    }

    /**
     * 更新用户设置参数
     *
     * @param userSetting UserSetting
     */
    @ApiOperation(value = "更新用户设置参数", notes = "更新用户设置参数。")
    @ApiImplicitParam(
        name = "userSetting", value = "json格式的用户设置参数对象信息",
        paramType = "body", dataTypeClass = UserSetting.class)
    @RequestMapping(method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户设置参数")
    @WrapUpResponseBody
    public ResponseData editUserSetting(@Valid UserSetting userSetting) {

        boolean isDefaultValue = userSetting.isDefaultValue();
        if (isDefaultValue) {
//            userSetting.setUserCode(WebOptUtils.getCurrentUserCode(request));
            userSettingManager.saveNewUserSetting(userSetting);
        } else {
            userSettingManager.updateUserSetting(userSetting);
        }
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 更新用户默认设置参数
     *
     * @param userSetting UserSetting
     */
    @ApiOperation(value = "更新用户默认设置参数", notes = "更新用户默认设置参数。")
    @ApiImplicitParam(
        name = "userSetting", value = "json格式的用户设置参数对象信息",
        paramType = "body", dataTypeClass = UserSetting.class)
    @RequestMapping(value = "updatedefault", method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新当前用户设置参数")
    @WrapUpResponseBody
    public ResponseData editDefaultSetting(@Valid UserSetting userSetting) {

        UserSetting dbSetting = userSettingManager.getUserSetting(userSetting.getUserCode(), userSetting.getParamCode());
        if (dbSetting == null) {
            userSetting.setUserCode("default");
            userSettingManager.saveNewUserSetting(userSetting);
        } else {
            userSettingManager.updateUserSetting(userSetting);
        }
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 删除当前用户设置参数
     *
     * @param paramCode paramCode
     * @param request   {@link HttpServletRequest}
     */
    @ApiOperation(value = "删除当前用户设置参数", notes = "删除当前用户设置参数。")
    @ApiImplicitParam(
        name = "paramCode", value = "用户设置参数代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户设置参数")
    @WrapUpResponseBody
    public ResponseData deleteUserSetting(@PathVariable String paramCode, HttpServletRequest request) {
        UserSetting dbUserSetting = userSettingManager.getObjectById(
            new UserSettingId(WebOptUtils.getCurrentUserCode(request), paramCode));
        userSettingManager.deleteObject(dbUserSetting);
        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logDeleteObject(request,optId,dbUserSetting.getUserCode(),
//                OperationLog.P_OPT_LOG_METHOD_D,  "已删除",dbUserSetting);
        /*********log*********/
    }

    /**
     * 删除用户设置参数
     *
     * @param userCode  用户代码
     * @param paramCode 设置编码
     */
    @ApiOperation(value = "删除用户设置参数", notes = "根据用户代码和参数代码删除用户设置参数。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "paramCode", value = "设置编码",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/{userCode}/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户设置参数")
    @WrapUpResponseBody
    public ResponseData delete(@PathVariable String userCode, @PathVariable String paramCode) {
        UserSetting userSetting = userSettingManager.getUserSetting(userCode, paramCode);
        if (userSetting != null) {
            if ("default".equals(userSetting.getUserCode())) {
                return ResponseData.makeErrorMessage("默认设置不能删除！");
            }
            userSettingManager.deleteObject(userSetting);
        }
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 删除用户默认设置
     *
     * @param paramCode 设置编码
     */
    @ApiOperation(value = "删除用户默认设置", notes = "删除用户默认设置。")
    @ApiImplicitParam(
        name = "paramCode", value = "用户设置参数代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/deletedefault/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户设置参数")
    @WrapUpResponseBody
    public ResponseData deleteDefault(@PathVariable String paramCode) {

        UserSetting userSetting = userSettingManager.getUserSetting("default", paramCode);
        if (userSetting != null) {
            userSettingManager.deleteObject(userSetting);
        } else {
            return ResponseData.makeErrorMessage("值已为null！");
        }
        return ResponseData.makeSuccessResponse();
    }
}
