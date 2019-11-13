package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.service.SysUnitManager;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author codefan
 */

@Controller
@RequestMapping("/unitrole")
@Api(value = "系统机构角色操作接口", tags = "系统机构角色操作接口")
public class UnitRoleController extends BaseController {
    @Resource
    @NotNull
    private SysUnitRoleManager sysUnitRoleManager;

    @Resource
    private SysUnitManager sysUnitManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "UNITROLE";//CodeRepositoryUtil.getCode("OPTID", "userRole");
    public String getOptId() {
        return "UNITROLE";
    }

    /**
     * 通过角色代码获取机构
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     */
    @ApiOperation(value = "通过角色代码获取机构", notes = "通过角色代码获取机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/roleunits/{roleCode}", method = RequestMethod.GET)
    //@RecordOperationLog(content="用户",appendRequest = true )
    @WrapUpResponseBody
    public PageQueryResult<Object> listUsersByRole(@PathVariable String roleCode, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("roleCode", roleCode);
        filterMap.put("unitValid", "T");
        JSONArray listObjects = sysUnitRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc);
    }

    /**
     * 通过角色代码获取当前登陆者所在机构下的所有子机构
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     */
    @ApiOperation(value = "通过角色代码获取当前登陆者所在机构下的所有子机构", notes = "通过角色代码获取当前登陆者所在机构下的所有子机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/rolesubunits/{roleCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listSubUnitByRole(@PathVariable String roleCode, PageDesc pageDesc, HttpServletRequest request) {

        String currentUnitCode = WebOptUtils.getCurrentUnitCode(request);
        UnitInfo currentUnit = sysUnitManager.getObjectById(currentUnitCode);
        String unitPathPrefix = currentUnit.getUnitPath();
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("roleCode", roleCode);
        filterMap.put("unitPathPrefix", unitPathPrefix);
        filterMap.put("unitValid", "T");
        JSONArray listObjects = sysUnitRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc);
    }

    /**
     * 通过机构代码获取角色
     *
     * @param unitCode 机构代码
     * @param pageDesc PageDesc
     */
    @ApiOperation(value = "通过机构代码获取角色", notes = "通过机构代码获取角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/unitroles/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listRolesByUser(@PathVariable String unitCode, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("unitCode", unitCode);
        filterMap.put("roleValid", "T");

        JSONArray listObjects = sysUnitRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc);
    }

    /**
     * 通过机构代码获取本机构角色
     *
     * @param unitCode 机构代码
     * @param pageDesc PageDesc
     */
    @ApiOperation(value = "通过机构代码获取本机构角色", notes = "通过机构代码获取本机构角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/currentunitroles/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public PageQueryResult<Object> listCurrentUnitRole(@PathVariable String unitCode, PageDesc pageDesc) {
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("unitCode", unitCode);
//        filterMap.put("currentUnitCode", unitCode);
        filterMap.put("roleValid", "T");

        JSONArray listObjects = sysUnitRoleManager.listObjects(filterMap, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects, pageDesc);
    }

    /**
     * 创建用户角色关联信息
     *
     * @param unitRole unitRole
     * @param unitCode 批量赋权
     */
    @ApiOperation(value = "创建用户角色关联信息", notes = "创建用户角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitRole", value = "json格式，机构角色对象信息",
            paramType = "body", dataTypeClass = UnitRole.class),
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码（数组）",
            allowMultiple = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}给机构{arg1}赋予权限{arg0.roleCode}")
    @WrapUpResponseBody
    public ResponseData create(@Valid UnitRole unitRole, @Valid String[] unitCode) {
        if (sysUnitRoleManager.getUnitRoleById(unitRole.getUnitCode(), unitRole.getRoleCode()) != null) {
            return ResponseData.makeErrorMessage("该角色已经关联此机构");
        }

        unitRole.setCreateDate(new Date());
        if (unitCode != null && unitCode.length > 0) {
            for (String u : unitCode) {
                UnitRole ur = new UnitRole();
                ur.copy(unitRole);
                ur.setUnitCode(u);
                sysUnitRoleManager.saveNewUnitRole(ur);
            }
        } else {
            sysUnitRoleManager.mergeUnitRole(unitRole);
        }
        return ResponseData.makeSuccessResponse();

        /*********log*********/
        //OperationLogCenter.logNewObject(request,optId, unitRole.getUnitCode()+"-"+ unitRole.getRoleCode(),
        //OperationLog.P_OPT_LOG_METHOD_C, "新增用户角色关联" , unitCodes);
        /*********log*********/
    }

    /**
     * 更新机构角色关联信息
     *
     * @param roleCode 角色代码
     * @param unitCode 机构代码
     * @param unitRole unitRole
     * @return UnitRole 机构角色关联信息
     */
    @ApiOperation(value = "更新机构角色关联信息", notes = "更新机构角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "unitCode", value = "机构代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "unitRole", value = "json格式，机构角色对象信息",
            paramType = "body", dataTypeClass = UnitRole.class)
    })
    @RequestMapping(value = "/{roleCode}/{unitCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}修改机构角色关联信息")
    @WrapUpResponseBody
    public UnitRole updateUnitRole(@PathVariable String roleCode, @PathVariable String unitCode, @Valid UnitRole unitRole) {
        UnitRole dbUnitRole = sysUnitRoleManager.getUnitRoleById(unitCode, roleCode);
        if (null == dbUnitRole) {
            throw new ObjectException(unitRole, "当前角色中无此机构");
        }
        dbUnitRole.copyNotNullProperty(unitRole);
        sysUnitRoleManager.updateUnitRole(dbUnitRole);
        return dbUnitRole;
    }

    /**
     * 删除机构角色关联信息
     *
     * @param roleCode  角色代码
     * @param unitCodes 批量删除代码
     */
    @ApiOperation(value = "删除机构角色关联信息", notes = "根据角色代码和机构代码（机构代码可以是多个）删除机构角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value = "角色代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "unitCodes", value = "机构代码（数组）",
            allowMultiple = true, paramType = "path", dataType = "String")
    })
    @RequestMapping(value = "/{roleCode}/{unitCodes}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除机构{arg1}角色{arg0.roleCode}")
    @WrapUpResponseBody
    public ResponseData delete(@PathVariable String roleCode, @PathVariable String unitCodes) {

        String[] unitCodeArray = unitCodes.split(",");
        for (String unitCode : unitCodeArray) {
            UnitRole dbUnitRole = sysUnitRoleManager.getUnitRoleById(unitCode, roleCode);
            if (dbUnitRole != null) {
                sysUnitRoleManager.deleteUnitRole(unitCode, roleCode);
                /*********log*********/
                //OperationLogCenter.logDeleteObject(request, optId, unitCode + "-" + roleCode,
                //OperationLog.P_OPT_LOG_METHOD_D, "删除用户角色关联信息", dbUnitRole);
                /*********log*********/
            }
        }
        return ResponseData.makeSuccessResponse();
    }

}
