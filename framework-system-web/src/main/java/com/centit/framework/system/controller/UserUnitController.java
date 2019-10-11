package com.centit.framework.system.controller;

import com.centit.support.common.ObjectException;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.po.UserUnit;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-11-27
 * Time: 上午10:16
 * 用户机构关联操作，此操作是双向操作，用户可在用户管理中新增或更新自身所在机构，机构可在机构管理中新增或更新机构内用户。
 */

@Controller
@RequestMapping("/userunit")
@Api(value = "用户机构关联操作，此操作是双向操作，用户可在用户管理中新增或更新自身所在机构，机构可在机构管理中新增或更新机构内用户。", tags = "用户机构关联操作接口")
public class UserUnitController extends BaseController {
    @Resource
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Resource
    @NotNull
    private SysUserManager sysUserManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "USERUNIT";//CodeRepositoryUtil.getCode("OPTID", "userUnit");
    public String getOptId() {
        return "USERUNIT";
    }

    /**
     * 机构人员树形信息
     *
     * @param state A或空，返回所有机构人员信息。T，返回未禁用的机构人员信息
     */
    @ApiOperation(value = "机构人员树形信息", notes = "机构人员树形信息。")
    @ApiImplicitParam(
        name = "state", value = "A或空，返回所有机构人员信息。T，返回未禁用的机构人员信息",
        paramType = "path", dataType = "String")
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData list(String state) {
        List<Map<String, Object>> listObjects = new ArrayList<>();
        if (StringUtils.isBlank(state)) {
            state = "A";
        }
        List<IUserInfo> users = CodeRepositoryUtil.getAllUsers(state);
        List<IUnitInfo> units = CodeRepositoryUtil.getAllUnits(state);

        for (IUnitInfo unit : units) {
            Map<String, Object> object = new HashMap<>();
            object.put("id", unit.getUnitCode());
            object.put("name", unit.getUnitName());
            object.put("pId", unit.getParentUnit());

            listObjects.add(object);
        }

        for (IUserInfo user : users) {
            Map<String, Object> object = new HashMap<>();
            object.put("id", user.getUserCode());
            object.put("name", user.getUserName());
            object.put("pId", user.getPrimaryUnit());

            listObjects.add(object);
        }

        return ResponseData.makeResponseData(listObjects);
    }

    /**
     * 通过机构代码获取机构及其子机构下用户组
     *
     * @param unitCode 机构代码
     *                 参数 s_isPaimary 是否为主机构，是T F否，为空不限定
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "通过机构代码获取机构及其子机构下用户组", notes = "通过机构代码获取机构及其子机构下用户组。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/unitusers/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listUsersByUnit(@PathVariable String unitCode, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);

        List<UserUnit> listObjects = sysUserUnitManager.listSubUsersByUnitCode(unitCode, filterMap, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        return resData;
    }

    /**
     * 通过用户代码获取用户所在机构
     *
     * @param userCode 用户代码
     *                 参数 s_isPaimary 是否为主机构，是T F否，为空不限定
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "通过用户代码获取用户所在机构", notes = "通过用户代码获取用户所在机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/userunits/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listUnitsByUser(@PathVariable String userCode, PageDesc pageDesc, HttpServletRequest request) {

//        UserInfo user = sysUserManager.getObjectById(this.WebOptUtils.getCurrentUserCode(request));
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("userCode", userCode);
//        filterMap.put("unitCode", user.getPrimaryUnit());

        List<UserUnit> listObjects = sysUserUnitManager.listObjects(filterMap, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, DictionaryMapUtils.objectsToJSONArray(listObjects));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        return resData;
    }

    @ApiOperation(value = "获取用户所在机构列表（在当前用户可见范围内）")
    @ApiImplicitParam(name = "userCode", value = "用户代码")
    @GetMapping(value = "/usercurrentunits/{userCode}")
    @WrapUpResponseBody
    public PageQueryResult<UserUnit> listUserUnitsUnderUnitByUserCode(@PathVariable String userCode, PageDesc pageDesc, HttpServletRequest request){
        String currentUnitCode  = WebOptUtils.getCurrentUnitCode(request);
        if(StringUtils.isBlank(currentUnitCode )){
            throw new ObjectException("未登录");
        }

        List<UserUnit> userUnits = sysUserUnitManager.listUserUnitsUnderUnitByUserCode(userCode, currentUnitCode, pageDesc);
        return PageQueryResult.createResult(userUnits, pageDesc);
    }

    /**
     * 根据用户机构关联对象的ID获取一条用户机构关联信息
     *
     * @param userunitid userunitid
     */
    @ApiOperation(value = "根据用户机构关联对象的ID获取一条用户机构关联信息", notes = "根据用户机构关联对象的ID获取一条用户机构关联信息。")
    @ApiImplicitParam(
        name = "userunitid", value = "用户机构ID",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userunitid}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUserUnitById(@PathVariable String userunitid) {
        UserUnit userUnit = sysUserUnitManager.getObjectById(userunitid);

        if (null == userUnit) {
            return ResponseData.makeErrorMessage("当前机构中无此用户");
        }
        return ResponseData.makeResponseData(DictionaryMapUtils.objectToJSON(userUnit));
    }

    /**
     * 根据用户代码和机构代码获取一组用户机构关联信息
     *
     * @param unitCode 机构代码
     * @param userCode 用户代码
     */
    @ApiOperation(value = "根据用户代码和机构代码获取一组用户机构关联信息", notes = "根据用户代码和机构代码获取一组用户机构关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "userCode", value = "用户代码",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/{unitCode}/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUserUnit(@PathVariable String unitCode, @PathVariable String userCode) {
        List<UserUnit> userUnits = sysUserUnitManager.listObjectByUserUnit(userCode, unitCode);

        if (null == userUnits || userUnits.size() == 0) {
            return ResponseData.makeErrorMessage("当前机构中无此用户");
        }
        return ResponseData.makeResponseData(DictionaryMapUtils.objectsToJSONArray(userUnits));
    }

    /**
     * 创建用户机构关联信息
     *
     * @param userUnit UserUnit
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "创建用户机构关联信息", notes = "创建用户机构关联信息。")
    @ApiImplicitParam(
        name = "userUnit", value = "json格式的用户机构关联对象信息", required = true,
        paramType = "body", dataTypeClass = UserUnit.class)
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增用户机构关联信息")
    @WrapUpResponseBody
    public ResponseData create(@Valid UserUnit userUnit, HttpServletRequest request) {

        HashMap<String, Object> map = new HashMap();
        map.put("unitCode", userUnit.getUnitCode());
        map.put("userRank", userUnit.getUserRank());
        map.put("userStation", userUnit.getUserStation());
        map.put("userCode", userUnit.getUserCode());
        List<UserUnit> list = sysUserUnitManager.listObjects(map, new PageDesc());
        if (list != null && list.size() > 0) {
            return ResponseData.makeErrorMessage("该用户已存在");
        }

        userUnit.setCreator(WebOptUtils.getCurrentUserCode(request));
        sysUserUnitManager.saveNewUserUnit(userUnit);

        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId, OperationLog.P_OPT_LOG_METHOD_C, OperationLog.P_OPT_LOG_METHOD_C, "新增用户机构关联信息" , userUnit);
        /*********log*********/
    }

    /**
     * 更新机构用户信息
     *
     * @param userunitid userunitid
     * @param userUnit   UserUnit
     * @param request    {@link HttpServletRequest}
     */
    @ApiOperation(value = "更新机构用户信息", notes = "根据用户机构代码更新机构用户信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userunitid", value = "用户机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "userUnit", value = "json格式的用户机构关联对象信息", required = true,
            paramType = "body", dataTypeClass = UserUnit.class)
    })
    @RequestMapping(value = "/{userunitid}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户机构关联信息")
    @WrapUpResponseBody
    public ResponseData edit(@PathVariable String userunitid, @Valid UserUnit userUnit, HttpServletRequest request) {

        userUnit.setUpdator(WebOptUtils.getCurrentUserCode(request));
        UserUnit dbUserUnit = sysUserUnitManager.getObjectById(userunitid);
        if (null == dbUserUnit) {
            return ResponseData.makeErrorMessage("当前机构中无此用户");
        }

        UserUnit oldValue = new UserUnit();
        oldValue.copy(dbUserUnit);

        dbUserUnit.copy(userUnit);

        sysUserUnitManager.updateUserUnit(dbUserUnit);

        return ResponseData.makeResponseData(userUnit);

        /*********log*********/
//        OperationLogCenter.logUpdateObject(request,optId,oldValue.getUserUnitId(), OperationLog.P_OPT_LOG_METHOD_U,
//                "更新用户机构关联信息", dbUserUnit, oldValue);
        /*********log*********/
    }

    /**
     * 删除用户机构关联信息
     *
     * @param userunitid userunitid
     */
    @ApiOperation(value = "删除用户机构关联信息", notes = "根据用户机构代码 删除用户机构关联信息。")
    @ApiImplicitParam(
        name = "userunitid", value = "用户机构代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{userunitid}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户机构关联信息")
    @WrapUpResponseBody
    public ResponseData delete(@PathVariable String userunitid) {
        UserUnit dbUserUnit = sysUserUnitManager.getObjectById(userunitid);
        if ("T".equals(dbUserUnit.getIsPrimary())) {
            return ResponseData.makeErrorMessage("主机构组织信息不能删除！");
        }

        sysUserUnitManager.deleteObject(dbUserUnit);

        return ResponseData.makeSuccessResponse();

        /*********log*********/
//        OperationLogCenter.logDeleteObject(request,optId,dbUserUnit.getUserUnitId(),
//                OperationLog.P_OPT_LOG_METHOD_D,  "删除用户机构关联信息",dbUserUnit);
        /*********log*********/
    }

}
