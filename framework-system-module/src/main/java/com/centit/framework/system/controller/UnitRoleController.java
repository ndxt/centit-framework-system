package com.centit.framework.system.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.UnitRole;
import com.centit.framework.system.service.SysUnitRoleManager;
import com.centit.support.database.utils.PageDesc;
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

/**
 * Created with IntelliJ IDEA.
 * @author codefan
 */

@Controller
@RequestMapping("/unitrole")
public class UnitRoleController extends BaseController {
    @Resource
    @NotNull
    private SysUnitRoleManager sysUnitRoleManager;

    /**
     * 系统日志中记录
     */
    private String optId = "UNITROLE";//CodeRepositoryUtil.getCode("OPTID", "userRole");

    /**
     * 通过角色代码获取机构
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/roleunits/{roleCode}", method = RequestMethod.GET)
    //@RecordOperationLog(content="用户",appendRequest = true )
    public void listUsersByRole(@PathVariable String roleCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
      ResponseMapData resData = new ResponseMapData();
      resData.addResponseData(OBJLIST, sysUnitRoleManager.listRoleUnits(roleCode,pageDesc));
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
    @RequestMapping(value = "/unitroles/{unitCode}", method = RequestMethod.GET)
    public void listRolesByUser(@PathVariable String unitCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, sysUnitRoleManager.listUnitRoles(unitCode,pageDesc));
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 返回一条用户角色关联信息
     * @param roleCode 角色代码
     * @param unitCode 用户代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{roleCode}/{unitCode}", method = RequestMethod.GET)
    public void getUserRole(@PathVariable String roleCode, @PathVariable String unitCode, HttpServletResponse response) {

        UnitRole unitRole = sysUnitRoleManager.getUnitRoleById(unitCode,roleCode);
        if (null == unitRole) {
            JsonResultUtils.writeErrorMessageJson("当前机构中无此角色", response);
            return;
        }
        JsonResultUtils.writeSingleDataJson(
            DictionaryMapUtils.objectToJSON(unitRole), response);
    }



    /**
     * 创建用户角色关联信息
     * @param unitRole unitRole
     * @param unitCode  批量赋权
     *  param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content="用户{userInfo.userCode}给机构{arg1}赋予权限{arg0.roleCode}")
    public void create(@Valid UnitRole unitRole,@Valid String[] unitCode, HttpServletResponse response) {
        unitRole.setCreateDate(new Date());
        if(unitCode!=null && unitCode.length>0){
            for(String u: unitCode){
                UnitRole ur = new UnitRole();
                ur.copy(unitRole);
                ur.setUnitCode(u);
                sysUnitRoleManager.mergeUnitRole(ur);
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
     * 更新用户角色关联信息
     * @param roleCode 角色代码
     * @param unitCode 机构代码
     * @param unitRole UserRole
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{roleCode}/{unitCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content="用户{userInfo.userCode}修改机构{arg1}角色{arg0.roleCode}")
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
     * 删除用户角色关联信息
     * @param roleCode 角色代码
     * @param unitCodes 批量删除代码
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{roleCode}/{unitCodes}", method = RequestMethod.DELETE)
    @RecordOperationLog(content="用户{userInfo.userCode}删除机构{arg1}角色{arg0.roleCode}")
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
