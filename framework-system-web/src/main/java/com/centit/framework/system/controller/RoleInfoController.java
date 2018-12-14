package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.OptMethodManager;
import com.centit.framework.system.service.SysRoleManager;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.framework.system.service.SysUserRoleManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.*;

@Controller
@RequestMapping("/roleinfo")
@Api(value = "系统角色操作接口", tags = "系统角色操作接口")
public class RoleInfoController extends BaseController {
    @Resource
    @NotNull
    private SysRoleManager sysRoleManager;

    @Resource
    protected PlatformEnvironment platformEnvironment;

    @Resource
    @NotNull
    private OptMethodManager optMethodManager;

    @Resource
    private SysUserRoleManager sysUserRoleManager;

    @Resource
    private SysUnitRoleManager sysUnitRoleManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "ROLEMAG";//CodeRepositoryUtil.getCode("OPTID", "roleInfo");
    public String getOptId() {
        return "ROLEMAG";
    }

    private ResponseMapData writeRoleListToResponse(List<RoleInfo> roleInfos, String[] field, PageDesc pageDesc) {

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        if (ArrayUtils.isNotEmpty(field)) {
            respData.toJSONString(JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class, field));
        } else {
            respData.toJSONString(JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers", "userRoles"));
        }
        return respData;
    }

    /**
     * 查询所有系统角色
     *
     * @param field    field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询所有系统角色", notes = "查询所有系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "指需要显示的属性名",
            allowMultiple = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listAllRole(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("NP_ALL", "true");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        return writeRoleListToResponse(roleInfos, field, pageDesc);
    }

    /**
     * 查询所有可用的系统角色
     *
     * @param field    field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询所有可用的系统角色", notes = "查询所有可用的系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "指需要显示的属性名",
            allowMultiple = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "分页对象",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/global", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listGlobalAndPublicRole(String[] field, PageDesc pageDesc, HttpServletRequest request) {

        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("NP_GLOBAL", "true");
        filterMap.put("isValid", "T");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        return writeRoleListToResponse(roleInfos, field, pageDesc);
    }

    /**
     * 查询所有 当前部门角色
     *
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询所有 当前部门角色", notes = "查询所有 当前部门角色。")
    @ApiImplicitParam(
        name = "pageDesc", value = "分页对象",
        paramType = "body", dataTypeClass = PageDesc.class)
    @GetMapping(value = "/currentunit")
    @WrapUpResponseBody
    public ResponseData listUnitAndPublicRole(PageDesc pageDesc, HttpServletRequest request) {

        String currentUnit = WebOptUtils.getLoginUser().getCurrentUnitCode();
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("publicUnitRole", currentUnit);
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        respData.toJSONString(JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers", "userRoles"));
        return respData;
    }

    /**
     * 查询所有某部门的部门角色
     *
     * @param field    field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "查询所有某部门的部门角色", notes = "查询所有某部门的部门角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "field", value = "指需要显示的属性名",
            allowMultiple = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "分页对象",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listItemRole(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("ROLETYPE", "I");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        if (ArrayUtils.isNotEmpty(field)) {
            respData.toJSONString(JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class, field));
        } else {
            respData.toJSONString(JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers", "userRoles"));
        }
        return respData;
    }

    /**
     * 根据角色代码获取角色操作定义信息
     *
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "根据角色代码获取角色操作定义信息", notes = "根据角色代码获取角色操作定义信息。")
    @ApiImplicitParam(
        name = "roleCode", value = "角色代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/power/role/{roleCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getRolePowerByRoleCode(@PathVariable String roleCode) {
        List<RolePower> rolePowers = sysRoleManager.getRolePowers(roleCode);

        return ResponseData.makeResponseData(rolePowers);
    }

    /**
     * 根据操作定义代码获取角色信息
     *
     * @param defCode 操作定义代码
     */
    @ApiOperation(value = "根据操作定义代码获取角色信息", notes = "根据操作定义代码获取角色信息。")
    @ApiImplicitParam(
        name = "defCode", value = "操作定义代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/power/defCode/{defCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getRolePowerByOptCode(@PathVariable String defCode) {

        List<RolePower> rolePowers = sysRoleManager.getRolePowersByDefCode(defCode);

        return ResponseData.makeResponseData(rolePowers);
    }

    /**
     * 根据业务代码获取角色信息
     *
     * @param optId 业务菜单代码
     */
    @ApiOperation(value = "根据操作定义代码获取角色信息", notes = "根据操作定义代码获取角色信息。")
    @ApiImplicitParam(
        name = "optId", value = "业务菜单代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/power/optCode/{optId}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getRolePowerByOptId(@PathVariable String optId) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<OptMethod> optDefs = optMethodManager.listOptMethodByOptID(optId);


        for (OptMethod def : optDefs) {
            Map<String, Object> temp = new HashMap<>();

            List<RolePower> rolePowers = sysRoleManager.getRolePowersByDefCode(def.getOptCode());

            temp.put("optDef", def);
            temp.put("rolePowers", rolePowers);
            result.add(temp);
        }

        return ResponseData.makeResponseData(result);
    }

    /**
     * 新增系统角色
     *
     * @param roleInfo RoleInfo
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "根据操作定义代码获取角色信息", notes = "根据操作定义代码获取角色信息。")
    @ApiImplicitParam(
        name = "roleInfo", value = "json格式，系统角色对象",
        required = true, paramType = "body", dataTypeClass = RoleInfo.class)
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增角色")
    @WrapUpResponseBody
    public ResponseData createGlobalRole(@Valid RoleInfo roleInfo, HttpServletRequest request) {
        String roleType = roleInfo.getRoleType();
        if (StringUtils.isBlank(roleType)) {
            return ResponseData.makeErrorMessage("新建角色必须指定角色类别。");
        }
        if ("D".equals(roleType)) {
            if (StringUtils.isBlank(roleInfo.getUnitCode())) {
                //JsonResultUtils.writeErrorMessageJson("机构角色必须指定所属机构。",response);
                //return;
                roleInfo.setUnitCode(super.getLoginUser(request).getCurrentUnitCode());
            }
        }
        //roleInfo.setUnitCode("G");
        roleInfo.setCreator(WebOptUtils.getLoginUserName(request));
        roleInfo.setCreateDate(new Date());
        sysRoleManager.saveNewRoleInfo(roleInfo);
        //刷新缓存
        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId,roleInfo.getRoleCode(),
//                OperationLog.P_OPT_LOG_METHOD_C, "新增角色" ,roleInfo);
        /*********log*********/
    }

    /**
     * 从操作定义反向添加角色代码
     *
     * @param roleCode 角色代码
     * @param optCode  操作定义
     */
    @ApiOperation(value = "从操作定义反向添加角色代码", notes = "从操作定义反向添加角色代码。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optCode", value = "操作定义",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/addopt/{roleCode}/{optCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}给角色添加权限")
    @WrapUpResponseBody
    public ResponseData addOptToRole(@PathVariable String roleCode, @PathVariable String optCode) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);

        if (null == dbRoleInfo) {
            return ResponseData.makeErrorMessage("角色信息不存在");
        }

        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));

        if (dbRoleInfo.getRolePowers().contains(rolePower)) {
            return ResponseData.makeSuccessResponse();
        }

        dbRoleInfo.getRolePowers().add(rolePower);

        sysRoleManager.updateRoleInfo(dbRoleInfo);

        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId, rolePower.getOptCode(),
//                OperationLog.P_OPT_LOG_METHOD_C,  "角色"+dbRoleInfo.getRoleName()+"添加权限:" , rolePower);
        /*********log*********/
    }

    /**
     * 从操作定义反向删除角色代码
     *
     * @param roleCode 角色代码
     * @param optCode  操作定义
     */
    @ApiOperation(value = "从操作定义反向删除角色代码", notes = "从操作定义反向删除角色代码。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optCode", value = "操作定义",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/delopt/{roleCode}/{optCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除角色权限")
    @WrapUpResponseBody
    public ResponseData deleteOptFormRole(@PathVariable String roleCode, @PathVariable String optCode) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);

        if (null == dbRoleInfo) {
            return ResponseData.makeErrorMessage("角色信息不存在");
        }

        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));
        if (!dbRoleInfo.getRolePowers().contains(rolePower)) {
            return ResponseData.makeSuccessResponse();
        }

        dbRoleInfo.getRolePowers().remove(rolePower);
        sysRoleManager.updateRoleInfo(dbRoleInfo);
        //刷新缓存
        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logDeleteObject(request,optId,rolePower.getOptCode(),
//                OperationLog.P_OPT_LOG_METHOD_D, "删除角色"+dbRoleInfo.getRoleName()+"的权限" , rolePower);
        /*********log*********/
    }

    /**
     * 更新系统角色
     *
     * @param roleCode 角色代码
     * @param roleInfo RoleInfo
     */
    @ApiOperation(value = "更新系统角色", notes = "更新系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleInfo", value = "json格式，角色修改的对象信息",
            required = true, paramType = "path", dataTypeClass = RoleInfo.class)
    })
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色")
    @WrapUpResponseBody
    public ResponseData edit(@PathVariable String roleCode, @Valid RoleInfo roleInfo) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            return ResponseData.makeErrorMessage("角色信息不存在");
        }

        RoleInfo oldValue = new RoleInfo();
        oldValue.copy(dbRoleInfo);

        sysRoleManager.updateRoleInfo(roleInfo);

        return ResponseData.makeSuccessResponse();

        /*********log*********/
//        OperationLogCenter.logUpdateObject(request,optId, roleCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新角色信息",roleInfo, oldValue);
        /*********log*********/
    }

    /**
     * 更新系统角色权限
     *
     * @param roleCode 角色代码
     * @param roleInfo rolePower roleCode dataScopes
     */
    @ApiOperation(value = "更新系统角色权限", notes = "更新系统角色权限。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleInfo", value = "json格式，含有系统角色权限的角色对象信息",
            required = true, paramType = "path", dataTypeClass = RoleInfo.class)
    })
    @RequestMapping(value = "/power/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色权限")
    @WrapUpResponseBody
    public ResponseData updateRolePower(@PathVariable String roleCode, RoleInfo roleInfo) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            return ResponseData.makeErrorMessage("角色信息不存在");
        }
//        String  userCode = getLoginUserCode(request);


//        List<RolePower> oldPowers = dbRoleInfo.getRolePowers();

        // List<RolePower> rolePowers = new ArrayList<>();
        /*for( Map.Entry<String, Object> ent : rolePower.entrySet()){
            RolePower po = new RolePower(
                    new RolePowerId( roleCode,ent.getKey()),
                    StringBaseOpt.objectToString(ent.getValue()) );
            po.setCreator(userCode);
            po.setUpdator(userCode);
            rolePowers.add(po);
        }*/
        //为空时更新RoleInfo中字段数据
        RoleInfo oldRoleInfo = new RoleInfo();
        oldRoleInfo.copy(dbRoleInfo);
        dbRoleInfo.setRolePowers(roleInfo.getRolePowers());
        List<RolePower> oldRolePowers = sysRoleManager.updateRolePower(dbRoleInfo);
        oldRoleInfo.setRolePowers(oldRolePowers);
        return ResponseData.makeSuccessResponse();
        /*********log*********/
//        OperationLogCenter.logUpdateObject(request,optId, roleCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新角色"+dbRoleInfo.getRoleName()+"权限",dbRoleInfo,oldRoleInfo);
        /*********log*********/
    }

    /**
     * 新增系统角色 判断名称是否存在
     *
     * @param roleName 角色名称
     */
    @ApiOperation(value = "新增判断角色名称是否存在", notes = "新增系统角色 判断名称是否存在。")
    @ApiImplicitParam(
        name = "roleName", value = "角色名称",
        required = true, paramType = "path", dataType = "String")
    @GetMapping(value = "/issysroleunique/{roleName}")
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isSysRoleNotExist(@PathVariable String roleName) {
        return sysRoleManager.judgeSysRoleNameExist(roleName, null, null);
    }

    /**
     * 更新系统角色 判断名称是否存在
     *
     * @param roleName 角色名称
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "更新判断角色名称是否存在", notes = "更新系统角色 判断名称是否存在。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleName", value = "角色名称",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String")
    })
    @GetMapping(value = "/issysroleunique/{roleName}/{roleCode}")
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isSysRoleUnique(@PathVariable String roleName, @PathVariable String roleCode) {
        return sysRoleManager.judgeSysRoleNameExist(roleName, roleCode, null);
    }

    /**
     * 新增部门角色 判断名称是否存在
     *
     * @param unitCode 部门代码
     * @param roleName 角色名称
     */
    @ApiOperation(value = "新增部门角色 判断名称是否存在", notes = "新增部门角色 判断名称是否存在。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleName", value = "角色名称",
            required = true, paramType = "path", dataType = "String")
    })
    @GetMapping(value = "/isunitroleunique/{unitCode}/{roleName}")
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isUnitRoleNotExist(@PathVariable String unitCode,
                                      @PathVariable String roleName) {
        return sysRoleManager.judgeSysRoleNameExist(roleName, null, unitCode);
    }

    /**
     * 更新部门角色 判断名称是否存在
     *
     * @param unitCode 部门代码
     * @param roleName 角色名称
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "更新部门角色 判断名称是否存在", notes = "更新部门角色 判断名称是否存在。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleName", value = "角色名称",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String")
    })
    @GetMapping(value = "/isunitroleunique/{unitCode}/{roleName}/{roleCode}")
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isUnitRoleUnique(@PathVariable String unitCode, @PathVariable String roleName, @PathVariable String roleCode) {
        return sysRoleManager.judgeSysRoleNameExist(roleName, roleCode, unitCode);
    }

    /**
     * 从操作定义反向删除角色代码
     *
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "从操作定义反向删除角色代码", notes = "从操作定义反向删除角色代码。")
    @ApiImplicitParam(
        name = "roleCode", value = "角色代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除角色")
    @WrapUpResponseBody
    public ResponseData deleteRole(@PathVariable String roleCode) {
        if (StringUtils.equalsAny(roleCode, "public", "anonymous", "forbidden")) {
            return ResponseData.makeErrorMessage("系统内置角色不能删除。");
        }
//        int n = sysRoleManager.countRoleUserSum(roleCode);
        List<UserInfo> users = sysUserRoleManager.listUsersByRole(roleCode);
        boolean isValid = true;
        for (UserInfo u : users) {
            if ("T".equals(u.getIsValid())) {
                return ResponseData.makeErrorMessage("有用户引用这个角色，不能删除。");
            }
            isValid = false;
        }
        if (!isValid) {
            return ResponseData.makeErrorMessage("有禁用用户引用这个角色，不能删除。");
        }
        JSONArray roleUnts = sysUnitRoleManager.listRoleUnits(roleCode, new PageDesc(1, 2));
        if (roleUnts != null && roleUnts.size() > 0) {
            return ResponseData.makeErrorMessage("有机构引用这个角色，不能删除。");
        }
        //RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        sysRoleManager.deleteRoleInfo(roleCode);
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 单个角色信息
     *
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "获取单个角色信息", notes = "根据角色代码获取单个角色信息。")
    @ApiImplicitParam(
        name = "roleCode", value = "角色代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData findRoleInfo(@PathVariable String roleCode) {
        RoleInfo roleInfo = sysRoleManager.getRoleInfo(roleCode);
        if (null == roleInfo) {
            return ResponseData.makeErrorMessage("角色信息不存在");
        }

        return ResponseData.makeResponseData(roleInfo);
    }

    /**
     * 机构权限
     *
     * @param unitCode 机构代码
     */
    @ApiOperation(value = "获取机构权限", notes = "根据机构代码获取机构权限。")
    @ApiImplicitParam(
        name = "unitCode", value = "机构代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/power/unit/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getUnitInfoPower(@PathVariable String unitCode) {

        List<RolePower> rolePowers = sysRoleManager.getRolePowers("G$" + unitCode);

        return ResponseData.makeResponseData(rolePowers);
    }

    /**
     * 对角色信息进行模糊搜索，适用于带搜索条件的下拉框。
     *
     * @param type    搜索条件
     * @param field   需要搜索的字段，如为空，默认，roleCode,roleName
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "对角色信息进行模糊搜索", notes = "对角色信息进行模糊搜索，适用于带搜索条件的下拉框。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "type", value = "搜索条件",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "field", value = "需要搜索的字段，如为空，默认，roleCode,roleName",
            allowMultiple = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/listRoles/{type}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listRoles(@PathVariable String type, String[] field, HttpServletRequest request) {
        if (ArrayUtils.isEmpty(field)) {
            field = new String[]{"roleCode", "roleName"};
        }
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
//        filterMap.put("roleType", type);
        filterMap.put("isValid", "T");
        if ("S".equals(type)) {
            filterMap.put("NP_unitCode", true);

        } else if ("D".equals(type)) {
            IUserUnit unit = CodeRepositoryUtil.getUserPrimaryUnit(super.getLoginUserCode(request));
            if (unit != null) {
                filterMap.put("publicUnitRole", unit.getUnitCode());
            } else {
                return null;
            }
        }
        List<RoleInfo> listObjects = sysRoleManager.listObjects(filterMap);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.toJSONString(JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class, field));
        return ResponseData.makeResponseData(resData.getResponseData(BaseController.OBJLIST));
    }

}
