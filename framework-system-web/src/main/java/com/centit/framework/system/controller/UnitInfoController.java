package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.*;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.service.*;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/*
 * Created with IntelliJ IDEA.
 * Date: 14-10-28
 * Time: 下午1:32
 * 机构管理Controller
 */
@Controller
@RequestMapping("/unitinfo")
@Api(value = "系统机构管理操作接口", tags = "系统机构管理操作接口")
public class UnitInfoController extends BaseController {

    @Autowired
    @NotNull
    private SysUnitManager sysUnitManager;

    @Autowired
    @NotNull
    private SysUserManager sysUserMag;

    @Autowired
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Autowired
    @NotNull
    private SysRoleManager sysRoleManager;

    @Autowired
    @NotNull
    private SysUnitRoleManager sysUnitRoleManager;

    /**
     * 系统日志中记录
     * @return 业务标识ID
     */
    public String getOptId() {
        return "UNITMAG";
    }

    /*
     * 查询所有机构信息
     *
     * @param struct  是否需要树形结构
     * @param id      父机构ID
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "查询所有机构信息", notes = "查询所有机构信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "struct", value = "指需要显示的属性名", paramType = "query", dataType = "Boolean"),
        @ApiImplicitParam(name = "id", value = "父机构ID", paramType = "query", dataType = "String")
    })
    @RequestMapping(method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listAsTree(boolean struct, String id, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        String unitName = (String) searchColumn.get("unitName");
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        if (StringUtils.isNotBlank(unitName) && StringUtils.isBlank(id)) {

            List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);
            //通过名字搜索，也添加状态字段
            sysUnitManager.checkState(listObjects);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            if (struct) {
                ja = CollectionsOpt.sortAsTreeAndToJSON(ja, (p, c) ->
                    StringUtils.equals(
                        ((JSONObject) p).getString("unitCode"),
                        ((JSONObject) c).getString("parentUnit")), "children");
            }
            return ResponseData.makeResponseData(ja);
        } else {
            if (StringUtils.isNotBlank(id)) {
                searchColumn.put("parentUnit", id);
            } else {
                searchColumn.put("NP_TOPUnit", "true");
            }
            List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);
            sysUnitManager.checkState(listObjects);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            return ResponseData.makeResponseData(ja);
        }
    }

    @ApiOperation(value = "分页查询机构信息", notes = "分页查询机构信息。")
    @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页信息",
            paramType = "body", dataTypeClass = PageDesc.class
        )
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<UnitInfo> list(PageDesc pageDesc, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn, pageDesc);
        return PageQueryResult.createResultMapDict(listObjects, pageDesc);
    }

    /*
     * 查询所有子机构信息
     *
     * @param id      String parentUnit 父类机构
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "查询所有子机构信息", notes = "查询所有子机构信息。")
    @ApiImplicitParam(
        name = "id", value = "父机构ID",
        paramType = "query", dataType = "String")
    @RequestMapping(value = "/subunits", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listSub(String id, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        String currentUnitCode = WebOptUtils.getCurrentUnitCode(request);
        searchColumn.put("parentUnit", StringUtils.isNotBlank(id) ? id : currentUnitCode);
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        String unitName = StringBaseOpt.castObjectToString(searchColumn.get("unitName"));
        if (StringUtils.isNotBlank(unitName)) {
            List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            return ResponseData.makeResponseData(ja);
        } else {
            List<UnitInfo> listObjects = sysUnitManager.listAllSubUnits((String) searchColumn.get("parentUnit"));
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            for (Object o : ja) {
                ((JSONObject) o).put("state", "open");
                ((JSONObject) o).put("id", ((JSONObject) o).getString("unitCode"));
                ((JSONObject) o).put("text", ((JSONObject) o).getString("unitName"));
            }
            ja = CollectionsOpt.sortAsTreeAndToJSON(ja, (p, c) ->
                StringUtils.equals(
                    ((JSONObject) p).getString("unitCode"),
                    ((JSONObject) c).getString("parentUnit")), "children");
            return ResponseData.makeResponseData(ja);
        }
    }

    /*
     * 查询 当前机构 子机构
     */
    @ApiOperation(value = "查询 当前机构 子机构", notes = "查询 当前机构 子机构。")
    @RequestMapping(value = "/validsubunits", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listValidSubUnit(HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        String unitName = (String) searchColumn.get("unitName");
        String currentUnitCode = WebOptUtils.getCurrentUnitCode(request);
        List<UnitInfo> listObjects = sysUnitManager.listValidSubUnits(currentUnitCode);

        JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
        for (Object o : ja) {
            ((JSONObject) o).put("state", "open");
            ((JSONObject) o).put("id", ((JSONObject) o).getString("unitCode"));
            ((JSONObject) o).put("text", ((JSONObject) o).getString("unitName"));
        }
        ja = CollectionsOpt.sortAsTreeAndToJSON(ja, (p, c) ->
                StringUtils.equals(
                    ((JSONObject) p).getString("unitCode"),
                    ((JSONObject) c).getString("parentUnit")),
            "children");
        return ResponseData.makeResponseData(ja);
    }

    /*
     * 查询单个机构信息
     *
     * @param unitCode 机构代码
     */
    @ApiOperation(value = "查询单个机构信息", notes = "根据机构ID查询单个机构信息。")
    @ApiImplicitParam(
        name = "unitCode", value = "机构ID",
        paramType = "query", dataType = "String")
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public UnitInfo getUnitInfo(@PathVariable String unitCode, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        return sysUnitManager.getObjectById(unitCode);
    }

    /*
     * 删除机构
     *
     * @param unitCode unitCode
     */
    @ApiOperation(value = "删除机构信息", notes = "根据机构ID删除机构信息。")
    @ApiImplicitParam(
        name = "unitCode", value = "机构ID",
        paramType = "query", dataType = "String")
    @RequestMapping(value = "/{unitCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除机构",
        tag="{unitCode}")
    @WrapUpResponseBody
    public ResponseData delete(@ParamName("unitCode")@PathVariable String unitCode) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);
        if (unitInfo == null) {
            return ResponseData.makeErrorMessage("The object not found!");
        }

        List<UserUnit> userUnits = sysUserUnitManager.listUnitUsersByUnitCode(unitCode);
        if (userUnits != null && userUnits.size() != 0) {
            return ResponseData.makeErrorMessage("该机构存在关联用户，不能删除！");
        }
        sysUnitManager.deleteUnitInfo(unitInfo);

        //删除unitrole关系表

        JSONArray unitRoles = sysUnitRoleManager.listUnitRoles(unitCode, PageDesc.createNotPaging());
        if (unitRoles != null && unitRoles.size() > 0) {
            for (Object obj : unitRoles) {
                sysUnitRoleManager.deleteUnitRole(unitCode,
                    JSON.to(UnitRole.class,obj).getRoleCode());
            }
        }

        return ResponseData.successResponse;
    }

    /*
     * 新建机构
     *
     * @param unitInfo UnitInfo
     */
    @ApiOperation(value = "新建机构", notes = "新建一个机构。")
    @ApiImplicitParam(
        name = "unitInfo", value = "json格式，机构信息对象",
        paramType = "body", dataTypeClass = UnitInfo.class)
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增机构",
        tag="{ui.unitCode}:{ui.unitName}")
    @WrapUpResponseBody
    public ResponseData create(@ParamName("ui")@Valid UnitInfo unitInfo) {

        if (sysUnitManager.hasSameName(unitInfo)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                "机构名" + unitInfo.getUnitName() + "已存在，请更换！");
        }
        HashMap<String, Object> map = new HashMap();
        map.put("unitWord", unitInfo.getUnitWord());
        List<UnitInfo> unitInfos = sysUnitManager.listObjects(map);
        if (unitInfos != null && unitInfos.size() > 0) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                "机构编码" + unitInfo.getUnitName() + "已存在，请更换！");
        }
        if(unitInfo.getUnitOrder()==null || unitInfo.getUnitOrder()==0) {
            while (!sysUnitManager.isUniqueOrder(unitInfo)) {
                unitInfo.setUnitOrder(unitInfo.getUnitOrder() + 1);
            }
        }
        sysUnitManager.saveNewUnitInfo(unitInfo);

        return ResponseData.makeResponseData(unitInfo);
    }

    /*
     * 新建部门，仅仅是为了区分权限
     *
     * @param unitInfo UnitInfo
     */
    @ApiOperation(value = "新建机构", notes = "新建一个机构。")
    @ApiImplicitParam(
        name = "unitInfo", value = "json格式，机构信息对象",
        paramType = "body", dataTypeClass = UnitInfo.class)
    @RequestMapping(value = "department", method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增机构",
        tag="{ui.unitCode}:{ui.unitName}")
    @WrapUpResponseBody
    public ResponseData createDepartment(@ParamName("ui")@Valid UnitInfo unitInfo, HttpServletRequest request) {
        UnitInfo parentUnit = sysUnitManager.getObjectById(unitInfo.getParentUnit());
        if(parentUnit == null ||
            ! StringUtils.contains(parentUnit.getUnitPath(),
                WebOptUtils.getCurrentUnitCode(request))){
            throw new ObjectException(unitInfo, ResponseData.ERROR_BAD_PROCESS_DATASCOPE,
                "用户只能添加其所在部门的下级部门。");
        }
        return create(unitInfo);
    }

    /*
     * 更新机构信息
     *
     * @param unitCode 机构代码
     * @param unitInfo UnitInfo
     */
    @ApiOperation(value = "更新机构信息", notes = "更新机构信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "unitInfo", value = "json格式，机构信息对象",
            paramType = "body", dataTypeClass = UnitInfo.class)
    })
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构",
        tag="{unitCode}")
    @WrapUpResponseBody
    public ResponseData edit(@ParamName("unitCode")@PathVariable String unitCode, @Valid UnitInfo unitInfo) {

        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            return ResponseData.makeErrorMessage("机构不存在");
        }
        if (!dbUnitInfo.getUnitName().equals(unitInfo.getUnitName())&&sysUnitManager.hasSameName(unitInfo)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                "机构名" + unitInfo.getUnitName() + "已存在，请更换！");
        }

        if (StringUtils.isNotBlank(unitInfo.getDepNo())){
            List<UnitInfo> unitInfos = sysUnitManager.listObjects(
                CollectionsOpt.createHashMap("topUnit",unitInfo.getTopUnit(),"depNo", unitInfo.getDepNo()));
            if (!CollectionUtils.sizeIsEmpty(unitInfos) && !unitCode.equals(unitInfos.get(0).getUnitCode())){
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                    "机构内部编码" + unitInfo.getUnitWord() + "已存在，请更换！");
            }
        }

        if ("F".equals(unitInfo.getIsValid())) {
            List<UnitInfo> units = sysUnitManager.listValidSubUnit(unitCode);
            if (units != null && units.size() != 0) {
                return ResponseData.makeErrorMessage("该机构包含下级机构，不能设为禁用！");
            }
            List<UserUnit> userUnits = sysUserUnitManager.listUnitUsersByUnitCode(unitCode);
            if (userUnits != null && userUnits.size() != 0) {
                return ResponseData.makeErrorMessage("该机构存在关联用户，不能设为禁用！");
            }
        }
        sysUnitManager.updateUnitInfo(unitInfo);

        return ResponseData.makeResponseData(unitInfo);
    }

    /*
     * 更新机构及子机构的状态
     *
     * @param unitCode    机构代码
     * @param statusValue 状态码 T 或 F
     */
    @ApiOperation(value = "更新机构及子机构的状态", notes = "更新机构及子机构的状态。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "statusValue", value = "状态码 T:可用 或 F:禁用",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/{unitCode}/status/{statusValue}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构状态",
        tag="{unitCode}")
    @WrapUpResponseBody
    public ResponseData changeStatus(@ParamName("unitCode")@PathVariable String unitCode, @PathVariable String statusValue) {
        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            return ResponseData.makeErrorMessage("机构不存在");
        }

        if (!"T".equals(statusValue) && !"F".equals(statusValue)) {
            return ResponseData.makeErrorMessage("机构状态不正确");
        }
        sysUnitManager.changeStatus(unitCode, statusValue);
        return ResponseData.successResponse;
    }

    /*
     * 获取单个机构下属子机构
     *
     * @param field    需要显示的字段
     * @param unitCode 机构代码
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "获取单个机构下属子机构", notes = "获取单个机构下属子机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "field", value = "需要显示的字段",
            allowMultiple = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/{unitCode}/children", method = RequestMethod.GET)
    @WrapUpResponseBody
    public JSONArray listChildren(@PathVariable String unitCode, String[] field, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        searchColumn.put("parentUnit", unitCode);
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);
        return DictionaryMapUtils.objectsToJSONArray(listObjects, field);
    }

    /*
     * 当前机构下所有用户
     *
     * @param pageDesc 分页信息
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "当前机构下所有用户", notes = "当前机构下所有用户。")
    @RequestMapping(value = "/currentunit/users", method = RequestMethod.GET)
    @ApiImplicitParam(
        name = "pageDesc", value = "json格式，分页对象信息",
        paramType = "body", dataTypeClass = PageDesc.class)
    @WrapUpResponseBody
    public PageQueryResult<Object> listUnitUsers(PageDesc pageDesc, HttpServletRequest request) {

        String currentUnitCode = WebOptUtils.getCurrentUnitCode(request);

        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        searchColumn.put("unitCode", currentUnitCode);
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        //特殊字符转义
        if (searchColumn.get("userName") != null) {
            searchColumn.put("likeUserOrLoginName", StringEscapeUtils.escapeHtml4(searchColumn.get("userName").toString()));
            searchColumn.remove("userName");
        }

        List<UserInfo> listObjects = sysUserMag.listObjects(searchColumn, pageDesc);
        JSONArray jsonArr = DictionaryMapUtils.objectsToJSONArray(listObjects);
        return PageQueryResult.createJSONArrayResult(jsonArr, pageDesc);
    }

    /*
     * 获取机构下所有可用的用户
     *
     * @param unitCode 机构代码
     */
    @ApiOperation(value = "获取机构下所有可用的用户", notes = "获取机构下所有可用的用户。")
    @ApiImplicitParam(
        name = "unitCode", value = "机构代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{unitCode}/validusers", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listUnitAllUsers(@PathVariable String unitCode, HttpServletRequest request) {

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("unitCode", unitCode);
        filterMap.put("isValid", "T");
        filterMap.put("topUnit", WebOptUtils.getCurrentTopUnit(request));

        List<UserInfo> listObjects = sysUserMag.listObjects(filterMap);
        return ResponseData.makeResponseData(listObjects);
    }

    /*
     * 获取当前用户所在机构下所有用户
     *
     * @param state   是否启用 T|F
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "获取机构下所有可用的用户", notes = "获取机构下所有可用的用户。")
    @ApiImplicitParam(
        name = "state", value = "是否启用 T:启用 | F:禁用",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/currentusers/{state}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listAllUsersByCurrentUser(@PathVariable String state, HttpServletRequest request) {
        String unitCode = WebOptUtils.getCurrentUnitCode(request);
        UnitInfo currentUnitInfo = sysUnitManager.getObjectById(unitCode);

        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("unitPath", currentUnitInfo.getUnitPath());

        filterMap.put("isValid", state);
        filterMap.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        List<UserInfo> listObjects = sysUserMag.listObjects(filterMap);

        return ResponseData.makeResponseData(listObjects);
    }

    /*
     * 当前机构下用户
     *
     * @param userunitid 用户机构代码
     */
    @ApiOperation(value = "当前机构下用户", notes = "当前机构下用户。")
    @ApiImplicitParam(
        name = "userUnitId", value = "用户机构代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/unitusers/{userUnitId}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUnitUser(@PathVariable String userUnitId, HttpServletRequest request)  {
        UserUnit userUnit = sysUserUnitManager.getObjectById(userUnitId);

        if (null == userUnit) {
            return ResponseData.makeErrorMessage(
                getI18nMessage("error.604.user_not_in_unit", request));
        }
        return ResponseData.makeResponseData(userUnit);
    }

    /*
     * 将权限赋给部门
     *
     * @param unitCode 机构代码
     * @param optCodes 操作权限代码 以，隔开
     */
    @ApiOperation(value = "将权限赋给部门", notes = "将权限赋给部门。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optCodes", value = "操作权限代码 以，隔开",
            required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/unit/saveopts/{unitCode}", method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构权限",
        tag="{unitCode}")
    @WrapUpResponseBody
    public ResponseData setUnitPowers(@ParamName("unitCode") @PathVariable String unitCode, String optCodes) {
        String[] optCodesArray = optCodes.split(",");
        RoleInfo roleInfo = sysRoleManager.getObjectById("G$" + unitCode);
        if (roleInfo == null) {
            roleInfo = new RoleInfo();
            roleInfo.setIsValid("T");
            roleInfo.setRoleCode("G$" + unitCode);
            roleInfo.setRoleName("赋给部门" + unitCode + "的权限");
            roleInfo.setRoleDesc(roleInfo.getRoleName());
            roleInfo.setRoleType("H");
            roleInfo.setUnitCode(unitCode);
            roleInfo.setCreateDate(new Date());
            sysRoleManager.saveNewRoleInfo(roleInfo);
            //刷新缓存
            //sysRoleManager.loadRoleSecurityMetadata();
        }

        List<RolePower> rolePowers = new ArrayList<>();
        //为空时更新RoleInfo中字段数据
        if (ArrayUtils.isNotEmpty(optCodesArray)) {
            for (String optCode : optCodesArray) {
                if (StringUtils.isNotBlank(optCode)) {
                    rolePowers.add(new RolePower(new RolePowerId(roleInfo.getRoleCode(), optCode)));
                }
            }
        }

        roleInfo.addAllRolePowers(rolePowers);
        sysRoleManager.updateRolePower(roleInfo);
        //sysRoleManager.loadRoleSecurityMetadata();
        return ResponseData.successResponse;
    }

    /*
     * 当前机构下所有可用的角色
     */
    @ApiOperation(value = "当前机构下所有可用的角色", notes = "当前机构下所有可用的角色。")
    @GetMapping(value = "/validroles")
    @WrapUpResponseBody
    public ResponseData listUnitAndPublicRole(HttpServletRequest request) {

        String currentUnitCode = WebOptUtils.getCurrentUnitCode(request);
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("publicUnitRole", currentUnitCode);
        filterMap.put("isValid", "T");
        filterMap.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap);
        return ResponseData.makeResponseData(roleInfos);
    }

    /*
     * 验证部门编码可用性
     *
     * @param depNo 部门编码
     * @return true-可用（唯一）；false-不可用（已存在同名）
     */
    @ApiOperation(value = "验证部门编码可用性-新增", notes = "true-可用（唯一）；false-不可用（已存在同名）")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "depNo", value = "部门编码", required = true),
    })
    @GetMapping(value = "/depNo_usability")
    @WrapUpResponseBody
    public boolean isDepNoUnique(String depNo) {
        return sysUnitManager.isDepNoUnique(depNo, null);
    }

    /*
     * 验证部门编码可用性
     *
     * @param unitCode 部门代码
     * @param depNo    部门编码
     * @return true-可用（唯一）；false-不可用（已存在同名）
     */
    @ApiOperation(value = "验证部门编码可用性-编辑", notes = "true-可用（唯一）；false-不可用（已存在同名）")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "depNo", value = "部门编码", required = true),
        @ApiImplicitParam(name = "unitCode", value = "部门Code(主键)", required = true),
    })
    @GetMapping(value = "/{unitCode}/depNo_usability")
    @WrapUpResponseBody
    public boolean isDepNoUnique(@PathVariable String unitCode, String depNo) {
        return sysUnitManager.isDepNoUnique(depNo, unitCode);
    }

    /*
     * 验证部门自定义编码可用性
     *
     * @param unitWord 部门自定义编码
     * @return true-可用（唯一）；false-不可用（已存在同名）
     */
    @ApiOperation(value = "验证部门自定义编码可用性-新增", notes = "true-可用（唯一）；false-不可用（已存在同名）")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "unitWord", value = "部门自定义编码", required = true),
    })
    @GetMapping(value = "/unitWord_usability")
    @WrapUpResponseBody
    public boolean isUnitWordUnique(String unitWord) {
        return sysUnitManager.isUnitWordUnique(unitWord, null);
    }

    /*
     * 验证部门自定义编码可用性
     *
     * @param unitCode 部门Code
     * @param unitWord 部门自定义编码
     * @return true-可用（唯一）；false-不可用（已存在同名）
     */
    @ApiOperation(value = "验证部门自定义编码可用性-编辑", notes = "true-可用（唯一）；false-不可用（已存在同名）")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "unitWord", value = "部门自定义编码", required = true),
        @ApiImplicitParam(name = "unitCode", value = "部门Code(主键)", required = true),
    })
    @GetMapping(value = "/{unitCode}/unitWord_usability")
    @WrapUpResponseBody
    public boolean isUnitWordUnique(@PathVariable String unitCode, String unitWord) {
        return sysUnitManager.isUnitWordUnique(unitWord, unitCode);
    }

    /*
     * 更新部门权限
     *
     * @param unitCode    机构代码
     * @param authorities 操作代码(json数组字符串，格式：[{'optCode':'xxx','optDataScopes':'xxx,xxx'}])
     */
    @ApiOperation(value = "更新部门权限", notes = "更新部门权限")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "unitCode", value = "机构代码", required = true, dataType = "String"),
        @ApiImplicitParam(name = "authorities",
            value = "操作代码(json数组字符串，格式：[{'optCode':'xxx','optDataScopes':'xxx,xxx'}])", required = true)
    })
    @PostMapping(value = "/{unitCode}/authorities")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新部门权限",
            tag="{unitCode}")
    @WrapUpResponseBody()
    public ResponseData updateAuthorities(@ParamName("unitCode") @PathVariable String unitCode, String authorities) {
        RoleInfo roleInfo = sysRoleManager.getObjectById("G$" + unitCode);
        if (roleInfo == null) {
            roleInfo = new RoleInfo();
            roleInfo.setIsValid("T");
            roleInfo.setRoleCode("G$" + unitCode);
            roleInfo.setRoleName("赋给部门" + unitCode + "的权限");
            roleInfo.setRoleDesc(roleInfo.getRoleName());
            roleInfo.setRoleType("H");
            roleInfo.setUnitCode(unitCode);
            roleInfo.setCreateDate(new Date());
            sysRoleManager.saveNewRoleInfo(roleInfo);
        }

        List<RolePower> rolePowers = new ArrayList<>();
        authorities = authorities.replaceAll("&#39;", "\'");//转义单引号
        authorities = authorities.replaceAll("&quot;", "\"");//转义双引号
        JSONArray authArr;
        try {
            authArr = JSON.parseArray(authorities);
        } catch (Exception e) {
            return ResponseData.makeErrorMessage("参数格式不正确");
        }
        for (Object auth : authArr) {
            String optCode = ((JSONObject) auth).getString("optCode");
            String scopes = ((JSONObject) auth).getString("optDataScopes");
            rolePowers.add(new RolePower(new RolePowerId(roleInfo.getRoleCode(), optCode), scopes));
        }

        roleInfo.addAllRolePowers(rolePowers);
        sysRoleManager.updateRolePower(roleInfo);
        return ResponseData.successResponse;
    }

    @ApiOperation(value = "根据用户代码获得用户的所有租户", notes = "根据用户代码获得用户的所有租户。")
    @ApiImplicitParam(name = "userCode", value = "用户代码", required = true, dataType = "String")
    @RequestMapping(value = "/topUnit/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public  List<UnitInfo> listUserTopUnits(@PathVariable String userCode, HttpServletRequest request) {
        return sysUnitManager.listUserTopUnits(userCode);
    }

    @ApiOperation(value = "获得所有租户", notes = "获得所有租户。")
    @RequestMapping(value = "/topUnit/all", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public  List<UnitInfo> listAllTopUnits(HttpServletRequest request) {
        return sysUnitManager.listAllTopUnits();
    }
}
