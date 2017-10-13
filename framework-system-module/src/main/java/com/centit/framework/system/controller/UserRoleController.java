package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSON;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.system.po.RoleInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserRoleId;
import com.centit.framework.system.service.SysUserRoleManager;
import com.centit.support.json.JsonPropertyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-11-27
 * Time: 上午10:16
 * 用户角色关联操作，此操作是双向操作。
 */

@Controller
@RequestMapping("/userrole")
public class UserRoleController extends BaseController {
    @Resource
    @NotNull
    private SysUserRoleManager sysUserRoleManager;

    /**
     * 系统日志中记录
     */
    private String optId = "USERROLE";//CodeRepositoryUtil.getCode("OPTID", "userRole");
    
    /**
     * 通过角色代码获取用户
     *
     * @param roleCode 角色代码
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/roleusers/{roleCode}", method = RequestMethod.GET)
    public void listUsersByRole(@PathVariable String roleCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = convertSearchColumn(request);
        filterMap.put("roleCode", roleCode);
        if(!Objects.isNull(filterMap.get("userName"))){
            filterMap.put("userName", "%"+filterMap.get("userName")+"%");
        }
        listObject(filterMap, pageDesc, response);
    }
    
    /**
     * 通过用户代码获取角色
     *
     * @param userCode 用户代码
     * @param pageDesc PageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/userroles/{userCode}", method = RequestMethod.GET)
    public void listRolesByUser(@PathVariable String userCode, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = convertSearchColumn(request);
        filterMap.put("userCode", userCode);
        
        String type = request.getParameter("type");
        if("S".equals(type)){
            filterMap.put("NP_userRoleType", true);
        }else if("D".equals(type)){
            filterMap.put("NP_unitRoleType", true);

        }

        listObject(filterMap, pageDesc, response);
    }
    
    
    /**
     * 查询所有用户角色
     *
     * @param filterMap   显示结果中只需要显示的字段
     * @param pageDesc PageDesc
     * @param response HttpServletResponse
     */
    protected void listObject(Map<String, Object> filterMap, PageDesc pageDesc, HttpServletResponse response) {
        List<UserRole> listObjects = sysUserRoleManager.listObjects(filterMap, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        Map<Class<?>, String[]> excludes = new HashMap<>();
        excludes.put(RoleInfo.class, new String[]{"rolePowers"});

        JsonResultUtils.writeResponseDataAsJson(resData, response, JsonPropertyUtils.getExcludePropPreFilter(excludes));
    }


    /**
     * 返回一条用户角色关联信息
     * @param roleCode 角色代码
     * @param userCode 用户代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{roleCode}/{userCode}", method = RequestMethod.GET)
    public void getUserRole(@PathVariable String roleCode, @PathVariable String userCode, HttpServletResponse response) {
        
        UserRole userRole = sysUserRoleManager.getObjectById(new UserRoleId(userCode,roleCode));

        if (null == userRole) {

            JsonResultUtils.writeErrorMessageJson("当前角色中无此用户", response);
            return;
        }
        Map<Class<?>, String[]> excludes = new HashMap<>();
        excludes.put(RoleInfo.class, new String[]{"rolePowers"});

 
        JsonResultUtils.writeSingleDataJson(userRole, response, JsonPropertyUtils.getExcludePropPreFilter(excludes));
    }



    /**
     * 创建用户角色关联信息
     * @param userRole UserRole
     * @param userCode  userCode
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(method = RequestMethod.POST)
    public void create(@Valid UserRole userRole,@Valid String[] userCode, HttpServletRequest request, HttpServletResponse response) {
        userRole.setCreateDate(new Date());
        if(userCode!=null && userCode.length>0){
            UserRole ur=null;
            for(String u: userCode){
                ur = new UserRole();
                ur.copy(userRole);
                ur.setUserCode(u);
                sysUserRoleManager.mergeObject(ur);
            }
        }

        JsonResultUtils.writeBlankJson(response);
        
        /*********log*********/
        OperationLogCenter.logNewObject(request,optId, userRole.getUserCode()+"-"+ userRole.getRoleCode(),
                OperationLog.P_OPT_LOG_METHOD_C, "新增用户角色关联" , userRole);
        /*********log*********/
    }


    /**
     * 更新用户角色关联信息
     * @param roleCode 角色代码
     * @param userCode 用户代码
     * @param userRole UserRole
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{roleCode}/{userCode}", method = RequestMethod.PUT)
    public void edit(@PathVariable String roleCode, @PathVariable String userCode, @Valid UserRole userRole,
                     HttpServletRequest request, HttpServletResponse response) {
        UserRole dbUserRole = sysUserRoleManager.getObjectById(new UserRoleId(userCode,roleCode));

        if (null == userRole) {

            JsonResultUtils.writeErrorMessageJson("当前角色中无此用户", response);
            return;
        }

        sysUserRoleManager.mergeObject(dbUserRole, userRole);
        JsonResultUtils.writeSingleDataJson(userRole, response);
        
        /*********log*********/

        StringBuilder optContent = new StringBuilder();
        optContent.append("更改用户角色信息:" + JSON.toJSONString(userRole.getId()));
        OperationLogCenter.logUpdateObject(request,optId,dbUserRole.getUserCode(),
                OperationLog.P_OPT_LOG_METHOD_U,  optContent.toString(),userRole,dbUserRole);
        /*********log*********/
    }

    /**
     * 删除用户角色关联信息
     * @param roleCode 角色代码
     * @param userCodes 用户代码
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{roleCode}/{userCodes}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String roleCode, @PathVariable String userCodes,
                       HttpServletRequest request, HttpServletResponse response) {

        String[] userCodeArray = userCodes.split(",");
        for(String userCode : userCodeArray){
            UserRoleId userRoleId=new UserRoleId(userCode,roleCode);
            UserRole userRole = sysUserRoleManager.getObjectById(userRoleId);
            sysUserRoleManager.deleteObjectById(userRoleId);
            /*********log*********/
            OperationLogCenter.logDeleteObject(request,optId,userCode+"-"+roleCode, OperationLog.P_OPT_LOG_METHOD_D,
                    "删除用户角色关联信息", userRole);
            /*********log*********/
        }
        JsonResultUtils.writeBlankJson(response);
    }
    
    /**
     * 删除用户角色关联信息
     * @param roleCode 角色代码
     * @param userCode 用户代码
     * @param response  {@link HttpServletResponse}
     * @param request  HttpServletRequest
     */
    @RequestMapping(value = "/ban/{roleCode}/{userCode}", method = RequestMethod.PUT)
    public void ban(@PathVariable String roleCode, @PathVariable String userCode,
                        HttpServletRequest request, HttpServletResponse response) {
        UserRoleId a=new UserRoleId(userCode,roleCode);
        UserRole userRole = sysUserRoleManager.getObjectById(a);
        sysUserRoleManager.deleteObjectById(a);
        JsonResultUtils.writeSuccessJson(response);

        /*********log*********/
        OperationLogCenter.logDeleteObject(request, optId, userCode+"-"+roleCode,
                OperationLog.P_OPT_LOG_METHOD_D, "删除用户角色关联信息", userRole);
        /*********log*********/
    }
}
