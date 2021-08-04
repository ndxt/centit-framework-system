package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.OptMethodManager;
import com.centit.framework.system.service.SysRoleManager;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.framework.system.service.SysUserRoleManager;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller
@RequestMapping("/roleinfo")
@Api(value = "系统角色操作接口", tags = "系统角色操作接口")
public class RoleInfoController extends BaseController {
    @Autowired
    @NotNull
    private SysRoleManager sysRoleManager;

    @Autowired
    @NotNull
    private OptMethodManager optMethodManager;

    @Autowired
    private SysUserRoleManager sysUserRoleManager;

    @Autowired
    private SysUnitRoleManager sysUnitRoleManager;

    /*
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "ROLEMAG";//CodeRepositoryUtil.getCode("OPTID", "roleInfo");
    public String getOptId() {
        return "ROLEMAG";
    }

    /*
     * 查询所有系统角色
     *
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
    @WrapUpResponseBody()
    public PageQueryResult<RoleInfo> listAllRole(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("NP_ALL", "true");
        List<RoleInfo> list = sysRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createResultMapDict(list, pageDesc);
    }

    @ApiOperation(value = "查询子系统角色", notes = "查询子系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "topOptId", value = "子系统代码",
            allowMultiple = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/subSysRole/{topOptId}", method = RequestMethod.GET)
    @WrapUpResponseBody()
    public PageQueryResult<RoleInfo> listSubSystemRole(@PathVariable String topOptId,PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("subSystemROLE", topOptId);
        List<RoleInfo> list = sysRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createResultMapDict(list, pageDesc);
    }

    /*
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
    public PageQueryResult<RoleInfo> listGlobalAndPublicRole(String[] field, PageDesc pageDesc, HttpServletRequest request) {

        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("NP_GLOBAL", "true");
        filterMap.put("isValid", "T");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createResultMapDict(roleInfos, pageDesc, field);
    }

    /*
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
    @WrapUpResponseBody()
    public PageQueryResult<RoleInfo> listUnitAndPublicRole(PageDesc pageDesc, HttpServletRequest request) {

        String currentUnit = WebOptUtils.getCurrentUnitCode(request);
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("publicUnitRole", currentUnit);
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createResultMapDict(roleInfos, pageDesc);
    }

    /*
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
    public PageQueryResult<RoleInfo> listItemRole(String[] field, PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("ROLETYPE", "I");
        if (null == filterMap.get("topUnit") && WebOptUtils.isTenantTopUnit(request)) {
            filterMap.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);

        return PageQueryResult.createResultMapDict(roleInfos, pageDesc, field);
    }

    /*
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
    public List<RolePower> getRolePowerByRoleCode(@PathVariable String roleCode) {
        return sysRoleManager.getRolePowers(roleCode);
    }

    /*
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
    public List<RolePower> getRolePowerByOptCode(@PathVariable String defCode) {
        return sysRoleManager.getRolePowersByDefCode(defCode);
    }

    /*
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
    public List<Map<String, Object>> getRolePowerByOptId(@PathVariable String optId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<OptMethod> optDefs = optMethodManager.listOptMethodByOptID(optId);

        for (OptMethod def : optDefs) {
            Map<String, Object> temp = new HashMap<>();

            List<RolePower> rolePowers = sysRoleManager.getRolePowersByDefCode(def.getOptCode());

            temp.put("optDef", def);
            temp.put("rolePowers", rolePowers);
            result.add(temp);
        }
        return result;
    }

    /*
     * 新增系统角色
     *
     * @param roleInfo RoleInfo
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "创建系统角色", notes = "创建系统角色。")
    @ApiImplicitParam(
        name = "roleInfo", value = "json格式，系统角色对象",
        required = true, paramType = "body", dataTypeClass = RoleInfo.class)
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增角色",
        tag="{ri.roleCode}")
    @WrapUpResponseBody
    public void createRole(@ParamName("ri") @Valid RoleInfo roleInfo, HttpServletRequest request) {
        String roleType = roleInfo.getRoleType();
        if (StringUtils.isBlank(roleType)) {
            throw new ObjectException(roleInfo, "新建角色必须指定角色类别。");
        }

        if ("D".equals(roleType) || "S".equals(roleType)) {
            throw new ObjectException(roleInfo, "不能用这个接口创建 子系统角色或者机构角色");
        }

        roleInfo.setCreator(WebOptUtils.getCurrentUserCode(request));
        roleInfo.setCreateDate(new Date());
        sysRoleManager.saveNewRoleInfo(roleInfo);
    }

    /*
     * 新增系统角色
     *
     * @param roleInfo RoleInfo
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "创建部门角色", notes = "创建部门角色。")
    @ApiImplicitParam(
        name = "roleInfo", value = "json格式，系统角色对象",
        required = true, paramType = "body", dataTypeClass = RoleInfo.class)

    @RequestMapping(value = "/departmentRole", method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增角色",
        tag="{ri.roleCode}")
    @WrapUpResponseBody
    public void createDepartmentRole(@ParamName("ri") @Valid RoleInfo roleInfo, HttpServletRequest request) {
        roleInfo.setRoleType("D");
        if (WebOptUtils.isTenantTopUnit(request)) {
            roleInfo.setRoleOwner(WebOptUtils.getCurrentTopUnit(request));
        } else {
            if (StringUtils.isBlank(roleInfo.getUnitCode())) {
                roleInfo.setRoleOwner(WebOptUtils.getCurrentUnitCode(request));
            }
        }
        roleInfo.setCreator(WebOptUtils.getCurrentUserCode(request));
        roleInfo.setCreateDate(new Date());
        sysRoleManager.saveNewRoleInfo(roleInfo);
    }

    /*
     * 新增系统角色
     *
     * @param roleInfo RoleInfo
     * @param request  HttpServletRequest
     */
    @ApiOperation(value = "创建子系统角色", notes = "创建子系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleInfo", value = "json格式，系统角色对象",
            required = true, paramType = "body", dataTypeClass = RoleInfo.class),
        @ApiImplicitParam(
            name = "topOptId", value = "子系统代码，为系统顶级菜单编码",
            required = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/subSysRole/{topOptId}", method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增角色",
        tag="{topOptId}:{ri.roleCode}")
    @WrapUpResponseBody
    public void createSubSystemRole(@ParamName("topOptId") @PathVariable String topOptId,
                                    @ParamName("ri") @Valid RoleInfo roleInfo, HttpServletRequest request) {
        roleInfo.setRoleType("S");
        roleInfo.setRoleOwner(topOptId);

        roleInfo.setCreator(WebOptUtils.getCurrentUserCode(request));
        roleInfo.setCreateDate(new Date());
        sysRoleManager.saveNewRoleInfo(roleInfo);
    }


    /*
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}给角色添加权限",
        tag="{roleCode}:{optCode}")
    @WrapUpResponseBody
    public void addOptToRole(@ParamName("roleCode")@PathVariable String roleCode,
                             @ParamName("optCode")@PathVariable String optCode) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            throw new ObjectException(roleCode+":"+optCode, "角色信息不存在");
        }
        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));

        if (dbRoleInfo.getRolePowers().contains(rolePower)) {
            return ;
        }
        dbRoleInfo.getRolePowers().add(rolePower);
        sysRoleManager.updateRoleInfo(dbRoleInfo);
    }

    /*
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除角色权限",
        tag="{roleCode}:{optCode}")
    @WrapUpResponseBody
    public void deleteOptFormRole(@ParamName("roleCode")@PathVariable String roleCode,
                                  @ParamName("optCode")@PathVariable String optCode) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);

        if (null == dbRoleInfo) {
            throw new ObjectException(roleCode+":"+optCode, "角色信息不存在");
        }

        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));
        if (!dbRoleInfo.getRolePowers().contains(rolePower)) {
            return;
        }

        dbRoleInfo.getRolePowers().remove(rolePower);
        sysRoleManager.updateRoleInfo(dbRoleInfo);
    }

    /*
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色",
        tag="{roleCode}")
    @WrapUpResponseBody
    public void updateRole(@ParamName("roleCode")@PathVariable String roleCode, @Valid RoleInfo roleInfo) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            throw new ObjectException(roleInfo, "角色信息不存在");
        }
        roleInfo.setRoleCode(roleCode);
        sysRoleManager.updateRoleInfo(roleInfo);
    }


    @ApiOperation(value = "更新机构角色", notes = "更新机构角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleInfo", value = "json格式，角色修改的对象信息",
            required = true, paramType = "path", dataTypeClass = RoleInfo.class)
    })
    @RequestMapping(value = "/departmentRole/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色",
        tag="{roleCode}")
    @WrapUpResponseBody
    public void updateDepartmentRole(@ParamName("roleCode")@PathVariable String roleCode,
                                             @Valid RoleInfo roleInfo,
                                            HttpServletRequest request) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            throw new ObjectException(roleInfo, "角色信息不存在");
        }
        roleInfo.setRoleType("D");
        if (WebOptUtils.isTenantTopUnit(request)) {
            roleInfo.setRoleOwner(WebOptUtils.getCurrentTopUnit(request));
        } else {
            roleInfo.setRoleOwner(WebOptUtils.getCurrentUnitCode(request));
        }
        if(!StringUtils.equals(dbRoleInfo.getRoleOwner(), roleInfo.getRoleOwner())){
            throw new ObjectException(roleInfo, "不能修改部门角色的所属机构");
        }
        roleInfo.setRoleCode(roleCode);
        sysRoleManager.updateRoleInfo(roleInfo);
    }

    @ApiOperation(value = "更新系统角色", notes = "更新系统角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "topOptId", value = "子系统代码，为系统顶级菜单编码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "roleInfo", value = "json格式，角色修改的对象信息",
            required = true, paramType = "path", dataTypeClass = RoleInfo.class)
    })
    @RequestMapping(value = "/subSysRole/{topOptId}/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色",
        tag="{topOptId}:{roleCode}")
    @WrapUpResponseBody
    public void updateSubSystemRole(@ParamName("topOptId")@PathVariable String topOptId,
                                    @ParamName("roleCode")@PathVariable String roleCode, @Valid RoleInfo roleInfo) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            throw new ObjectException(roleInfo, "角色信息不存在");
        }
        roleInfo.setRoleType("S");
        roleInfo.setRoleOwner(topOptId);
        if(!StringUtils.equals(dbRoleInfo.getRoleOwner(),topOptId)){
            throw new ObjectException(roleInfo, "不能修改子系统角色的归属系统");
        }
        roleInfo.setRoleCode(roleCode);
        sysRoleManager.updateRoleInfo(roleInfo);
    }
    /*
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新角色权限",
        tag="{roleCode}")
    @WrapUpResponseBody
    public void updateRolePower(@ParamName("roleCode")@PathVariable String roleCode, RoleInfo roleInfo) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            throw new ObjectException(roleInfo, "角色信息不存在");
        }
        sysRoleManager.updateRolePower(roleInfo);
    }

    /*
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
        return sysRoleManager.judgeSysRoleNameCanBeUsed(roleName, null, null);
    }

    /*
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
        return sysRoleManager.judgeSysRoleNameCanBeUsed(roleName, roleCode, null);
    }

    /*
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
        return sysRoleManager.judgeSysRoleNameCanBeUsed(roleName, null, unitCode);
    }

    /*
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
        return sysRoleManager.judgeSysRoleNameCanBeUsed(roleName, roleCode, unitCode);
    }

    /*
     * 从操作定义反向删除角色代码
     *
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "从操作定义反向删除角色代码", notes = "从操作定义反向删除角色代码。")
    @ApiImplicitParam(
        name = "roleCode", value = "角色代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除角色",
        tag="{roleCode}")
    @WrapUpResponseBody
    public ResponseData deleteRole(@ParamName("roleCode") @PathVariable String roleCode) {
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
        return ResponseData.successResponse;
    }

    /*
     * 单个角色信息
     *
     * @param roleCode 角色代码
     */
    @ApiOperation(value = "获取单个角色信息", notes = "根据角色代码获取单个角色信息。")
    @ApiImplicitParam(
        name = "roleCode", value = "角色代码",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.GET)
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public RoleInfo findRoleInfo(@PathVariable String roleCode) {
        return sysRoleManager.getRoleInfo(roleCode);
    }

    /*
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

    /*
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
    public JSONArray listRoles(@PathVariable String type,String owner, String[] field, HttpServletRequest request) {
        if (ArrayUtils.isEmpty(field)) {
            field = new String[]{"roleCode", "roleName"};
        }
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
//        filterMap.put("roleType", type);
        filterMap.put("isValid", "T");
        filterMap.put("roleType",type);
        if (WebOptUtils.isTenantTopUnit(request)) {
            filterMap.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }

        if ("D".equals(type) && StringUtils.isBlank(owner)) {

            IUserUnit unit = CodeRepositoryUtil.getUserPrimaryUnit(
                WebOptUtils.getCurrentTopUnit(request),
                WebOptUtils.getCurrentUserCode(request));
            if (unit != null) {
                owner =  unit.getUnitCode();
            }
        }
        if(StringUtils.isNotBlank(owner)) {
            filterMap.put("unitCode", owner);
        }

        List<RoleInfo> listObjects = sysRoleManager.listObjects(filterMap);
        return DictionaryMapUtils.objectsToJSONArray(listObjects, field);
    }

}
