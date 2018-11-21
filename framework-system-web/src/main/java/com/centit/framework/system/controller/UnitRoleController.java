package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.UnitInfo;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.service.SysUnitManager;
import com.centit.framework.system.service.SysUnitRoleManager;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @author codefan
 */

@Controller
@RequestMapping("/unitrole")
@Api(value="系统机构角色操作接口", tags= "系统机构角色操作接口")
public class UnitRoleController extends BaseController {
    @Resource
    @NotNull
    private SysUnitRoleManager sysUnitRoleManager;

    @Resource
    private SysUnitManager sysUnitManager;

    /**
     * 系统日志中记录
     * @return 业务标识ID
     */
    //private String optId = "UNITROLE";//CodeRepositoryUtil.getCode("OPTID", "userRole");
    public String getOptId() {
        return  "UNITROLE";
    }
    /**
     * 通过角色代码获取机构
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     * //param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="通过角色代码获取机构",notes="通过角色代码获取机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value="角色代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value="json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/roleunits/{roleCode}", method = RequestMethod.GET)
    //@RecordOperationLog(content="用户",appendRequest = true )
    public void listUsersByRole(@PathVariable String roleCode, PageDesc pageDesc, HttpServletResponse response) {
        Map<String, Object> filterMap = new HashMap<>(5);
        filterMap.put("roleCode",roleCode);
        filterMap.put("unitValid", "T");
      ResponseMapData resData = new ResponseMapData();
      resData.addResponseData(BaseController.OBJLIST, sysUnitRoleManager.listObjects(filterMap,pageDesc));
      resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

      JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 通过角色代码获取当前登陆者所在机构下的所有子机构
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     * //param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="通过角色代码获取当前登陆者所在机构下的所有子机构",notes="通过角色代码获取当前登陆者所在机构下的所有子机构。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value="角色代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value="json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/rolesubunits/{roleCode}", method = RequestMethod.GET)
    public void listSubUnitByRole(@PathVariable String roleCode, PageDesc pageDesc,
                                  HttpServletResponse response) {

        String currentUnitCode = WebOptUtils.getLoginUser().getCurrentUnitCode();
        UnitInfo currentUnit = sysUnitManager.getObjectById(currentUnitCode);
        String unitPathPrefix = currentUnit.getUnitPath();
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("roleCode", roleCode);
        filterMap.put("unitPathPrefix", unitPathPrefix);
        filterMap.put("unitValid", "T");

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, sysUnitRoleManager.listObjects(filterMap,pageDesc));
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 通过机构代码获取角色
     *
     * @param unitCode 机构代码
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="通过机构代码获取角色",notes="通过机构代码获取角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value="机构代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value="json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/unitroles/{unitCode}", method = RequestMethod.GET)
    public void listRolesByUser(@PathVariable String unitCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("unitCode", unitCode);
        filterMap.put("roleValid", "T");

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, sysUnitRoleManager.listObjects(filterMap,pageDesc));
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 通过机构代码获取本机构角色
     *
     * @param unitCode 机构代码
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="通过机构代码获取本机构角色",notes="通过机构代码获取本机构角色。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value="机构代码",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value="json格式，分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @RequestMapping(value = "/currentunitroles/{unitCode}", method = RequestMethod.GET)
    public void listCurrentUnitRole(@PathVariable String unitCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("unitCode", unitCode);
//        filterMap.put("currentUnitCode", unitCode);
        filterMap.put("roleValid", "T");

        JSONArray ja = sysUnitRoleManager.listObjects(filterMap, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, ja);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 返回一条用户角色关联信息
     * @param roleCode 角色代码
     * @param unitCode 用户代码
     * @param response HttpServletResponse
     */
//    @RequestMapping(value = "/{roleCode}/{unitCode}", method = RequestMethod.GET)
//    public void getUserRole(@PathVariable String roleCode, @PathVariable String unitCode, HttpServletResponse response) {
//
//        UnitRole unitRole = sysUnitRoleManager.getUnitRoleById(unitCode,roleCode);
//        if (null == unitRole) {
//            JsonResultUtils.writeErrorMessageJson("当前机构中无此角色", response);
//            return;
//        }
//        JsonResultUtils.writeSingleDataJson(
//            DictionaryMapUtils.objectToJSON(unitRole), response);
//    }



    /**
     * 创建用户角色关联信息
     * @param unitRole unitRole
     * @param unitCode  批量赋权
     *  param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="创建用户角色关联信息",notes="创建用户角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitRole", value="json格式，机构角色对象信息",
            paramType = "body", dataTypeClass= UnitRole.class),
        @ApiImplicitParam(
            name = "unitCode", value="机构代码（数组）",
            allowMultiple=true, paramType = "query", dataType= "String")
    })
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content="操作IP地址:{loginIp},用户{loginUser.userName}给机构{arg1}赋予权限{arg0.roleCode}")
    public void create(@Valid UnitRole unitRole,@Valid String[] unitCode, HttpServletResponse response) {
        if (sysUnitRoleManager.getUnitRoleById(unitRole.getUnitCode(),unitRole.getRoleCode()) != null){
            JsonResultUtils.writeErrorMessageJson("该角色已经关联此机构", response);
            return;
        }

        unitRole.setCreateDate(new Date());
        if(unitCode!=null && unitCode.length>0){
            for(String u: unitCode){
                UnitRole ur = new UnitRole();
                ur.copy(unitRole);
                ur.setUnitCode(u);
                sysUnitRoleManager.saveNewUnitRole(ur);
            }
        }else{
            sysUnitRoleManager.mergeUnitRole(unitRole);
        }

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
        //OperationLogCenter.logNewObject(request,optId, unitRole.getUnitCode()+"-"+ unitRole.getRoleCode(),
                //OperationLog.P_OPT_LOG_METHOD_C, "新增用户角色关联" , unitCodes);
        /*********log*********/
    }


    /**
     * 更新机构角色关联信息
     * @param roleCode 角色代码
     * @param unitCode 机构代码
     * @param unitRole UserRole
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="更新机构角色关联信息",notes="更新机构角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value="角色代码",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "unitCode", value="机构代码",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "unitRole", value="json格式，机构角色对象信息",
            paramType = "body", dataTypeClass= UnitRole.class)
    })
    @RequestMapping(value = "/{roleCode}/{unitCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content="操作IP地址:{loginIp},用户{loginUser.userName}修改机构角色关联信息")
    public void edit(@PathVariable String roleCode, @PathVariable String unitCode, @Valid UnitRole unitRole,
                     HttpServletRequest request, HttpServletResponse response) {
        UnitRole dbUnitRole = sysUnitRoleManager.getUnitRoleById(unitCode,roleCode);
        if (null == dbUnitRole) {
            JsonResultUtils.writeErrorMessageJson("当前角色中无此用户", response);
            return;
        }
        dbUnitRole.copyNotNullProperty(unitRole);
        sysUnitRoleManager.updateUnitRole(dbUnitRole);
        JsonResultUtils.writeSingleDataJson(dbUnitRole, response);

        /*********log*********/
        //OperationLogCenter.logUpdateObject(request,optId,dbUnitRole.getUnitCode(),
            //OperationLog.P_OPT_LOG_METHOD_U,"更改用户角色信息:" + JSON.toJSONString(unitRole.getId()) ,unitRole,dbUnitRole);
        /*********log*********/
    }

    /**
     * 删除机构角色关联信息
     * @param roleCode 角色代码
     * @param unitCodes 批量删除代码
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @ApiOperation(value="删除机构角色关联信息",notes="根据角色代码和机构代码（机构代码可以是多个）删除机构角色关联信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "roleCode", value="角色代码",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "unitCodes", value="机构代码（数组）",
            allowMultiple = true, paramType = "path", dataType= "String")
    })
    @RequestMapping(value = "/{roleCode}/{unitCodes}", method = RequestMethod.DELETE)
    @RecordOperationLog(content="操作IP地址:{loginIp},用户{loginUser.userName}删除机构{arg1}角色{arg0.roleCode}")
    public void delete(@PathVariable String roleCode, @PathVariable String unitCodes,
                       HttpServletRequest request, HttpServletResponse response) {

        String[] unitCodeArray = unitCodes.split(",");
        for(String unitCode : unitCodeArray){
            UnitRole dbUnitRole = sysUnitRoleManager.getUnitRoleById(unitCode,roleCode);
            if(dbUnitRole!=null) {
                sysUnitRoleManager.deleteUnitRole(unitCode, roleCode);
                /*********log*********/
                //OperationLogCenter.logDeleteObject(request, optId, unitCode + "-" + roleCode,
                    //OperationLog.P_OPT_LOG_METHOD_D, "删除用户角色关联信息", dbUnitRole);
                /*********log*********/
            }
        }
        JsonResultUtils.writeBlankJson(response);
    }


}
