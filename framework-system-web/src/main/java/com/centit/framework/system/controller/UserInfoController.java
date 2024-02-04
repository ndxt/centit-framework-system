package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserRole;
import com.centit.framework.model.basedata.UserUnit;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import com.centit.support.security.SecurityOptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @Autowired
    @NotNull
    private SysUserManager sysUserManager;

    @Autowired
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Autowired
    private WorkGroupManager workGroupManager;


    /*
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "USERMAG";//CodeRepositoryUtil.getCode("OPTID", "userInfo");
    public String getOptId() {
        return "USERMAG";
    }

    /*
     * 查询所有用户信息
     *
     * @param field    显示结果中只需要显示的字段
     * @param pageDesc PageDesc
     * @param _search  _search
     * @param request  HttpServletRequest
     * @return 分页查询结果
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
    ),@ApiImplicitParam(
        name = "all", value = "平台管理员根据具体的topUnit查询",
        paramType = "query", dataType = "Boolean"
    ), @ApiImplicitParam(
        name = "field", value = "过滤返回的字段信息",
        allowMultiple = true, paramType = "query", dataType = "String"
    )})
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<UserInfo> list(String[] field, PageDesc pageDesc, String _search, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        //特殊字符转义
        if (searchColumn.get("userName") != null) {
            searchColumn.put("likeUserOrLoginName", StringEscapeUtils.escapeHtml4(searchColumn.get("userName").toString()));
            searchColumn.remove("userName");
        }
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        List<UserInfo> listObjects;
        if (MapUtils.getBoolean(searchColumn,"all",false) && userIsSystemMember(request)){
            //平台租户查询所用用户
            listObjects = sysUserManager.listObjects(searchColumn);
            return PageQueryResult.createResultMapDict(listObjects, pageDesc, field);
        }

        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", topUnit);
        }
        //name = "_search", value = "强制关闭分页查询",
        if (BooleanBaseOpt.castObjectToBoolean(_search,false)) {
            listObjects = sysUserManager.listObjects(searchColumn);
            //pageDesc = null;
        } else {
            listObjects = sysUserManager.listObjects(searchColumn, pageDesc);
        }
        //脱敏操作, 添加归属部门信息
        if(listObjects!=null){
            for (UserInfo userInfo : listObjects) {
                UserUnit uu = CodeRepositoryUtil.getUserPrimaryUnit(topUnit, userInfo.getUserCode());
                if(uu!=null){
                    userInfo.setPrimaryUnit(uu.getUnitCode());
                }
                userInfo.setIdCardNo("");
                userInfo.setUserPwd("");
                userInfo.setUserPin("");
            }
        }
        return PageQueryResult.createResultMapDict(listObjects, pageDesc, field);
    }
    @ApiOperation(value = "用户信息按机构分页查询", notes = "用户信息按机构分页查询")
    @RequestMapping(value = "/querybyunit",method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listQueryByUnit(PageDesc pageDesc, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        JSONArray jsonArray = sysUserManager.listObjectsByUnit(searchColumn, pageDesc);
        return PageQueryResult.createJSONArrayResult(jsonArray,pageDesc, UserInfo.class);
    }
    /*
     * 新增用户
     *
     * @param userInfo UserInfo
     * @param userUnit 用户机构
     * @param request  HttpServletRequest
     * @return 结果
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增用户",
        tag = "{us.userCode}")
    @WrapUpResponseBody
    public ResponseData create(@ParamName("us") @Valid UserInfo userInfo, UserUnit userUnit, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
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
        userUnit.setCreator(WebOptUtils.getCurrentUserCode(request));
        userUnit.setUserCode(userInfo.getUserCode());
        userUnit.setUserOrder(userInfo.getUserOrder());
        sysUserManager.saveNewUserInfo(userInfo, userUnit);

        return ResponseData.makeResponseData(userInfo);
    }

    /*
     * 更新用户信息
     * @param userCode userCode
     * @param userInfo userInfo
     * @param userUnit userUnit
     * @param request  HttpServletRequest
     * @return 结果
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户信息",
        tag = "{userCode}")
    @WrapUpResponseBody
    public ResponseData updateUserInfo(@ParamName("userCode") @PathVariable String userCode, @Valid UserInfo userInfo, UserUnit userUnit,
                             HttpServletRequest request) {

        UserInfo dbUserInfo = sysUserManager.getObjectById(userCode);
        if (null == dbUserInfo) {
            return ResponseData.makeErrorMessage("当前用户不存在");
        }
        if(!StringUtils.equals(userInfo.getLoginName(), dbUserInfo.getLoginName())) {
            if(sysUserManager.isLoginNameExist(userInfo.getUserCode(), userInfo.getLoginName())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的登录名已存在，");
            }
        }
        if(StringUtils.isNotBlank(userInfo.getRegCellPhone()) &&
                !StringUtils.equals(userInfo.getRegCellPhone(), dbUserInfo.getRegCellPhone())) {
            if(sysUserManager.isCellPhoneExist(userInfo.getUserCode(), userInfo.getRegCellPhone())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的预留的电话号码已存在，");
            }
        }
        if(StringUtils.isNotBlank(userInfo.getRegEmail()) &&
            !StringUtils.equals(userInfo.getRegEmail(), dbUserInfo.getRegEmail())) {
            if(sysUserManager.isEmailExist(userInfo.getUserCode(), userInfo.getRegEmail())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的预留的email已存在，");
            }
        }

        if(!StringUtils.equals(userInfo.getLoginName(), dbUserInfo.getLoginName())) {
            if(sysUserManager.isLoginNameExist(userInfo.getUserCode(), userInfo.getLoginName())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的登录名已存在，");
            }
        }
        if(StringUtils.isNotBlank(userInfo.getRegCellPhone()) &&
            !StringUtils.equals(userInfo.getRegCellPhone(), dbUserInfo.getRegCellPhone())) {
            if(sysUserManager.isCellPhoneExist(userInfo.getUserCode(), userInfo.getRegCellPhone())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的预留的电话号码已存在，");
            }
        }
        if(StringUtils.isNotBlank(userInfo.getRegEmail()) &&
            !StringUtils.equals(userInfo.getRegEmail(), dbUserInfo.getRegEmail())) {
            if(sysUserManager.isEmailExist(userInfo.getUserCode(), userInfo.getRegEmail())){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "用户的预留的email已存在，");
            }
        }

        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        sysUserUnitManager.deletePrimaryUnitByUserCode(userCode,topUnit);
        userUnit.setTopUnit(topUnit);
        userUnit.setUserCode(userInfo.getUserCode());
        userUnit.setUnitCode(userInfo.getPrimaryUnit());
        userUnit.setRelType("T");
        userUnit.setCreator(WebOptUtils.getCurrentUserCode(request));
        userUnit.setUserOrder(userInfo.getUserOrder());
        sysUserUnitManager.saveNewUserUnit(userUnit);

        //if (StringUtils.isBlank(userInfo.getUserPin())) {
        userInfo.setUserPin(dbUserInfo.getUserPin());
        //}
        //由于userinfo已经在sysUserUnitManager.saveNewUserUnit(userUnit)中被修改
        //防止用户的当前登录租户信息被修改，在这里重新恢复userInfo中的topUnit和primaryUnit
        CodeRepositoryCache.evictCache("UnitUser", userInfo.getPrimaryUnit());
        userInfo.setPrimaryUnit(dbUserInfo.getPrimaryUnit());
        userInfo.setTopUnit(dbUserInfo.getTopUnit());
        userInfo.setUserType(userInfo.getUserType());
        sysUserManager.updateUserInfo(userInfo);
        CodeRepositoryCache.evictCache("UserInfo", topUnit);
        CodeRepositoryCache.userUnitsMap.getCachedValue(topUnit).evictIdentifiedCache(userCode);
        CodeRepositoryCache.evictCache("UnitUser", userInfo.getPrimaryUnit());
        return ResponseData.successResponse;

    }

    /*
     * 当前登录用户信息
     *
     * @param request HttpServletRequest
     * @return 结果
     */
    @ApiOperation(value = "当前登录用户信息", notes = "当前登录用户信息。")
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @WrapUpResponseBody
    public UserInfo getCurrentUserInfo(HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if(StringUtils.isBlank(userCode)){
            return null;
        }
        return sysUserManager.getObjectById(userCode);
    }

    /*
     * 获取单个用户信息
     *
     * @param userCode 用户代码
     * @return 结果
     */
    @ApiOperation(value = "获取单个用户信息", notes = "根据用户代码获取单个用户信息。")
    @ApiImplicitParam(
        name = "userCode", value = "用户代码",
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseMapData getUserInfo(@PathVariable String userCode, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        UserInfo userInfo = sysUserManager.getObjectById(userCode);
        String topUnit = "";
        if (WebOptUtils.isTenantTopUnit(request)) {
            topUnit = WebOptUtils.getCurrentTopUnit(request);
        }
        UserUnit userUnit = sysUserUnitManager.getPrimaryUnitByUserCode(userCode, topUnit);

        //针对输入框类型做的反转译
        userInfo.setUserCode(StringEscapeUtils.unescapeHtml4(userInfo.getUserCode()));
        userInfo.setLoginName(StringEscapeUtils.unescapeHtml4(userInfo.getLoginName()));
        userInfo.setUserWord(StringEscapeUtils.unescapeHtml4(userInfo.getUserWord()));
        userInfo.setEnglishName(StringEscapeUtils.unescapeHtml4(userInfo.getEnglishName()));
        userInfo.setUserName(StringEscapeUtils.unescapeHtml4(userInfo.getUserName()));
        userInfo.setUserDesc(StringEscapeUtils.unescapeHtml4(userInfo.getUserDesc()));
        //脱敏操作
        //UserInfo desensitizeUserInfo = Sensitive.desensitize(userInfo);

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

    /*
     * 当前登录名是否已存在
     *
     * @param request HttpServletRequest
     * @return 结果
     * */
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
        if (StringUtils.isAllBlank(userCode,loginName,regPhone,regEmail)){
            throw new ObjectException("参数不能为空!");
        }
        return sysUserManager.isAnyOneExist(userCode, loginName, regPhone, regEmail);
    }

    /*
     * 当前登录名是否已存在
     *
     * @param loginName 登录名
     * @return 结果
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

    /*
     * 强制更新用户密码
     *
     * @param userCode 用户代码
     * @param request  {@link HttpServletRequest}
     * @return 结果
     */
    @ApiOperation(value = "强制更新用户密码", notes = "强制更新用户密码。")
    @ApiImplicitParam(
        name = "userCode", value = "用户代码", required = true,
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/changePwd/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}强制更新用户密码",
        tag = "{userCode}")
    @WrapUpResponseBody
    public void forceChangePwd(@ParamName("userCode") @PathVariable String userCode,
                               @RequestBody String jsonBody,
                               HttpServletRequest request) {
        String currentUser = WebOptUtils.getCurrentUserCode(request);
        if(StringUtils.isBlank(currentUser)){
            throw new ObjectException(ResponseData.ERROR_SESSION_TIMEOUT,"您没有权限强制设置密码。");
        }
        JSONObject objBody = JSONObject.parseObject(jsonBody);
        //newPassword
        String newPassword = SecurityOptUtils.decodeSecurityString(objBody.getString("newPassword"));

        if (StringUtils.isBlank(newPassword)) {
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,"您没有设置新的密码。");
        } else {
            sysUserManager.forceSetPassword(userCode, newPassword);
        }
    }

    /*
     * 检查用户密码是否可以修改
     *
     * @param userCode    用户代码
     * @param oldPassword 旧密码
     * @return 结果
     * */
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
        oldPassword = SecurityOptUtils.decodeSecurityString(oldPassword);
        boolean bo = sysUserManager.checkUserPassword(userCode, oldPassword);
        return ResponseData.makeResponseData(bo);
    }

     /*
     * 删除用户
     *
     * @param userCodes 用户代码
     * @return 结果
     * */
    @ApiOperation(value = "批量删除用户", notes = "批量删除用户。")
    @ApiImplicitParam(
        name = "userCodes", value = "用户代码集合(数组)", allowMultiple = true,
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userCodes}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户",
        tag = "{userCodes}")
    @WrapUpResponseBody
    public ResponseData deleteUser(@ParamName("userCodes") @PathVariable String[] userCodes) {
        for (String userCode : userCodes) {
            UserInfo userInfo = sysUserManager.getObjectById(userCode);
            if (null != userInfo) {
                sysUserManager.deleteUserInfo(userCode);
            } else {
                return ResponseData.makeErrorMessage("该用户不存在");
            }
        }
        return ResponseData.successResponse;
    }

    /*
     * 获取单个用户指定的敏感信息
     *
     * @param userCode 用户代码
     * @return 结果
     */
    @ApiOperation(value = "获取单个用户指定的敏感信息", notes = "获取单个用户指定的敏感信息。")
    @ApiImplicitParam(
        name = "userCode", value = "用户代码",
        paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userCode}/{name}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUserInfo(@PathVariable String userCode, @PathVariable String name) {
        UserInfo userInfo = sysUserManager.getObjectById(userCode);
        Map<String, String> result = new HashMap<>();
        if ("idCardNo".equals(name)) {
            result.put(name, userInfo.getIdCardNo());
        } else if ("regCellPhone".equals(name)) {
            result.put(name, userInfo.getRegCellPhone());
        } else if ("regEmail".equals(name)) {
            result.put(name, userInfo.getRegEmail());
        }
        return ResponseData.makeResponseData(result);
    }

    /**
     * 当前人员是否是系统租户的中成员
     * @param request
     * @return
     */
    private boolean userIsSystemMember( HttpServletRequest request ){
        String currentUserCode = WebOptUtils.getCurrentUserCode(request);
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if ( !"system".equals(topUnit) || StringUtils.isBlank(currentUserCode) ){
            return false;
        }
        return workGroupManager.loginUserIsExistWorkGroup("system",currentUserCode);
    }
}
