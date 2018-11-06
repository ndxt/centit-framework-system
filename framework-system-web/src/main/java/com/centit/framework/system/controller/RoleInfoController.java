package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller
@RequestMapping("/roleinfo")
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
     * @return 业务标识ID
     */
    //private String optId = "ROLEMAG";//CodeRepositoryUtil.getCode("OPTID", "roleInfo");
    public String getOptId() {
      return "ROLEMAG";
    }

    private void writeRoleListToResponse(List<RoleInfo> roleInfos,String[] field,PageDesc pageDesc,HttpServletResponse response) {

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        if (ArrayUtils.isNotEmpty(field)) {
            JsonResultUtils.writeResponseDataAsJson(respData, response,
                JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class,field));
        }else{
            JsonResultUtils.writeResponseDataAsJson(respData, response,
                JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers","userRoles"));
        }
    }
    /**
     * 查询所有系统角色
     * @param field field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public void listAllRole(String[] field,PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("NP_ALL", "true");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        writeRoleListToResponse(roleInfos,field,pageDesc,response );
    }

    /**
     * 查询所有系统角色
     * @param field field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/global", method = RequestMethod.GET)
    public void listGlobalAndPublicRole(String[] field,PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("NP_GLOBAL", "true");
        filterMap.put("isValid", "T");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);
        writeRoleListToResponse(roleInfos,field,pageDesc,response );
    }

    /**
     * 查询所有 当前部门角色
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/currentunit")
    public void listUnitAndPublicRole(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {

        String currentUnit = WebOptUtils.getLoginUser().getCurrentUnitCode();
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("publicUnitRole", currentUnit);
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(respData, response,
            JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers","userRoles"));
    }

    /**
     * 查询所有某部门部门角色
     * @param field field[]
     * @param pageDesc PageDesc
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public void listItemRole(String[] field,PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = BaseController.convertSearchColumn(request);
        filterMap.put("ROLETYPE",  "I");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap, pageDesc);

        ResponseMapData respData = new ResponseMapData();
        respData.addResponseData(BaseController.OBJLIST, roleInfos);
        respData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        if (ArrayUtils.isNotEmpty(field)) {
            JsonResultUtils.writeResponseDataAsJson(respData, response,
              JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class,field));
        } else {
          JsonResultUtils.writeResponseDataAsJson(respData, response,
            JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers","userRoles"));
        }
    }

    /**
     * 根据角色代码获取角色操作定义信息
     *
     * @param roleCode 角色代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/power/role/{roleCode}", method = RequestMethod.GET)
    public void getRolePowerByRoleCode(@PathVariable String roleCode, HttpServletResponse response) {
        List<RolePower> rolePowers = sysRoleManager.getRolePowers(roleCode);

        JsonResultUtils.writeSingleDataJson(rolePowers, response/*,
                JsonPropertyUtils.getExcludePropPreFilter(RoleInfo.class, "rolePowers")*/);
    }

    /**
     * 根据操作定义代码获取角色信息
     *
     * @param defCode  操作定义代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/power/defCode/{defCode}", method = RequestMethod.GET)
    public void getRolePowerByOptCode(@PathVariable String defCode, HttpServletResponse response) {

        List<RolePower> rolePowers = sysRoleManager.getRolePowersByDefCode(defCode);

        JsonResultUtils.writeSingleDataJson(rolePowers, response);
    }

    /**
     * 根据业务代码获取角色信息
     *
     * @param optId  操作定义代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/power/optCode/{optId}", method = RequestMethod.GET)
    public void getRolePowerByOptId(@PathVariable String optId, HttpServletResponse response) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<OptMethod> optDefs = optMethodManager.listOptMethodByOptID(optId);


        for (OptMethod def : optDefs) {
            Map<String, Object> temp = new HashMap<>();

            List<RolePower> rolePowers = sysRoleManager.getRolePowersByDefCode(def.getOptCode());

            temp.put("optDef", def);
            temp.put("rolePowers", rolePowers);
            result.add(temp);
        }

      /*  Map<Class<?>, String[]> excludes = new HashMap<>();
        excludes.put(RoleInfo.class, new String[]{"rolePowers"});
        excludes.put(OptMethod.class, new String[]{"optInfo"});*/
        JsonResultUtils.writeSingleDataJson(result, response);
    }

    /**
     * 新增系统角色
     *
     * @param roleInfo RoleInfo
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}新增角色")
    public void createGlobalRole(@Valid RoleInfo roleInfo,HttpServletRequest request, HttpServletResponse response) {
        String roleType = roleInfo.getRoleType();
        if(StringUtils.isBlank(roleType)){
            JsonResultUtils.writeErrorMessageJson("新建角色必须指定角色类别。",response);
            return;
        }
        if("D".equals(roleType)){
            if(StringUtils.isBlank(roleInfo.getUnitCode())){
                //JsonResultUtils.writeErrorMessageJson("机构角色必须指定所属机构。",response);
                //return;
                roleInfo.setUnitCode( super.getLoginUser(request).getCurrentUnitCode());
            }
        }
        //roleInfo.setUnitCode("G");
        roleInfo.setCreator(WebOptUtils.getLoginUserName(request));
        roleInfo.setCreateDate(new Date());
        sysRoleManager.saveNewRoleInfo(roleInfo);
      //刷新缓存
        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId,roleInfo.getRoleCode(),
//                OperationLog.P_OPT_LOG_METHOD_C, "新增角色" ,roleInfo);
        /*********log*********/
    }


    /**
     * 从操作定义反向添加角色代码
     * @param roleCode 角色代码
     * @param optCode 操作定义
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/addopt/{roleCode}/{optCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}给角色添加权限")
    public void addOptToRole(@PathVariable String roleCode, @PathVariable String optCode,
            HttpServletRequest request,HttpServletResponse response) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);

        if (null == dbRoleInfo) {
            JsonResultUtils.writeErrorMessageJson("角色信息不存在", response);
            return;
        }

        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));

        if (dbRoleInfo.getRolePowers().contains(rolePower)) {
            JsonResultUtils.writeBlankJson(response);
            return;
        }

        dbRoleInfo.getRolePowers().add(rolePower);

        sysRoleManager.updateRoleInfo(dbRoleInfo);

        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId, rolePower.getOptCode(),
//                OperationLog.P_OPT_LOG_METHOD_C,  "角色"+dbRoleInfo.getRoleName()+"添加权限:" , rolePower);
        /*********log*********/
    }

    /**
     * 从操作定义反向删除角色代码
     * @param roleCode 角色代码
     * @param optCode 操作定义
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/delopt/{roleCode}/{optCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}删除角色权限")
    public void deleteOptFormRole(@PathVariable String roleCode, @PathVariable String optCode,
            HttpServletRequest request, HttpServletResponse response) {
        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);

        if (null == dbRoleInfo) {
            JsonResultUtils.writeErrorMessageJson("角色信息不存在", response);
            return;
        }

        RolePower rolePower = new RolePower(new RolePowerId(roleCode, optCode));
        if (!dbRoleInfo.getRolePowers().contains(rolePower)) {
            JsonResultUtils.writeBlankJson(response);
            return;
        }

        dbRoleInfo.getRolePowers().remove(rolePower);
        sysRoleManager.updateRoleInfo(dbRoleInfo);
        //刷新缓存
        JsonResultUtils.writeBlankJson(response);
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
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}更新角色")
    public void edit(@PathVariable String roleCode, @Valid RoleInfo roleInfo,
                     HttpServletRequest request, HttpServletResponse response) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            JsonResultUtils.writeErrorMessageJson("角色信息不存在", response);
            return;
        }

        RoleInfo oldValue = new RoleInfo();
        oldValue.copy(dbRoleInfo);

        sysRoleManager.updateRoleInfo(roleInfo);

        JsonResultUtils.writeBlankJson(response);

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
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/power/{roleCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}更新角色权限")
    public void updateRolePower(@PathVariable String roleCode, RoleInfo roleInfo,
                     HttpServletRequest request, HttpServletResponse response) {

        RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        if (null == dbRoleInfo) {
            JsonResultUtils.writeErrorMessageJson("角色信息不存在", response);
            return;
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
        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//        OperationLogCenter.logUpdateObject(request,optId, roleCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新角色"+dbRoleInfo.getRoleName()+"权限",dbRoleInfo,oldRoleInfo);
        /*********log*********/
    }

    /**
     * 新增系统角色 判断名称是否存在
     * @param roleName 角色名称
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/issysroleunique/{roleName}")
    public void isSysRoleNotExist(@PathVariable String roleName, HttpServletResponse response){
        JsonResultUtils.writeOriginalObject(sysRoleManager.judgeSysRoleNameExist(roleName,null, null), response);
    }

    /**
     * 更新系统角色 判断名称是否存在
     * @param roleName 角色名称
     * @param roleCode 角色代码
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/issysroleunique/{roleName}/{roleCode}")
    public void isSysRoleUnique(@PathVariable String roleName,
                                @PathVariable String roleCode, HttpServletResponse response){
        JsonResultUtils.writeOriginalObject(sysRoleManager.judgeSysRoleNameExist(roleName,roleCode, null), response);
    }

    /**
     * 新增部门角色 判断名称是否存在
     * @param unitCode 部门代码
     * @param roleName 角色名称
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/isunitroleunique/{unitCode}/{roleName}")
    public void isUnitRoleNotExist(@PathVariable String unitCode,
                                   @PathVariable String roleName, HttpServletResponse response){
        JsonResultUtils.writeOriginalObject(sysRoleManager.judgeSysRoleNameExist(roleName,null, unitCode), response);
    }

    /**
     * 更新部门角色 判断名称是否存在
     * @param unitCode 部门代码
     * @param roleName 角色名称
     * @param roleCode 角色代码
     * @param response HttpServletResponse
     */
    @GetMapping(value = "/isunitroleunique/{unitCode}/{roleName}/{roleCode}")
    public void isUnitRoleUnique(@PathVariable String unitCode, @PathVariable String roleName,
                                 @PathVariable String roleCode, HttpServletResponse response){
        JsonResultUtils.writeOriginalObject(sysRoleManager.judgeSysRoleNameExist(roleName, roleCode, unitCode), response);
    }

    /**
     * 从操作定义反向删除角色代码
     * @param roleCode 角色代码
     * //param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{userInfo.userName}删除角色")
    public void deleteRole(@PathVariable String roleCode, HttpServletResponse response) {
        if(StringUtils.equalsAny(roleCode,
          "public", "anonymous", "forbidden")){
            JsonResultUtils.writeErrorMessageJson("系统内置角色不能删除。", response);
            return;
        }
//        int n = sysRoleManager.countRoleUserSum(roleCode);
        List<UserInfo> users = sysUserRoleManager.listUsersByRole(roleCode);
        boolean isValid = true;
        for(UserInfo u : users){
            if("T".equals(u.getIsValid())){
              JsonResultUtils.writeErrorMessageJson("有用户引用这个角色，不能删除。", response);
              return ;
            }
            isValid = false;
        }
        if(!isValid){
          JsonResultUtils.writeErrorMessageJson("有禁用用户引用这个角色，不能删除。", response);
          return ;
        }
        JSONArray roleUnts = sysUnitRoleManager.listRoleUnits(roleCode, new PageDesc(1,2));
        if(roleUnts!=null && roleUnts.size()>0){
            JsonResultUtils.writeErrorMessageJson("有机构引用这个角色，不能删除。", response);
            return ;
        }
        //RoleInfo dbRoleInfo = sysRoleManager.getObjectById(roleCode);
        sysRoleManager.deleteRoleInfo(roleCode);
        JsonResultUtils.writeSuccessJson(response);
    }

    /**
     * 单个角色信息
     *
     * @param roleCode 角色代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.GET)
    public void findRoleInfo(@PathVariable String roleCode, HttpServletResponse response) {
        RoleInfo roleInfo = sysRoleManager.getRoleInfo(roleCode);
        if (null == roleInfo) {
            JsonResultUtils.writeErrorMessageJson("角色信息不存在", response);
            return;
        }

        JsonResultUtils.writeSingleDataJson(roleInfo, response);
    }


    /**
     * 机构权限
     *
     * @param unitCode 机构代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/power/unit/{unitCode}", method = RequestMethod.GET)
    public void getUnitInfoPower(@PathVariable String unitCode, HttpServletResponse response) {

        List<RolePower> rolePowers = sysRoleManager.getRolePowers("G$" + unitCode);

        JsonResultUtils.writeSingleDataJson(rolePowers, response);
    }



    /**
     * 对角色信息进行模糊搜索，适用于带搜索条件的下拉框。
     *
     * @param type      搜索条件
     * @param field    需要搜索的字段，如为空，默认，roleCode,roleName
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/listRoles/{type}", method = RequestMethod.GET)
    public void listRoles(@PathVariable String type, String[] field, HttpServletRequest request,HttpServletResponse response) {
        if (ArrayUtils.isEmpty(field)) {
            field = new String[]{"roleCode", "roleName"};
        }
        Map<String,Object> filterMap = BaseController.convertSearchColumn(request);
//        filterMap.put("roleType", type);
        filterMap.put("isValid","T");
        if("S".equals(type)){
            filterMap.put("NP_unitCode", true);

        }else if("D".equals(type)){
            IUserUnit unit = CodeRepositoryUtil.getUserPrimaryUnit(super.getLoginUserCode(request));
            if(unit!=null) {
                filterMap.put("publicUnitRole", unit.getUnitCode());
            }else return;
        }
        List<RoleInfo> listObjects = sysRoleManager.listObjects(filterMap);

        JsonResultUtils.writeSingleDataJson(listObjects, response,
                JsonPropertyUtils.getIncludePropPreFilter(RoleInfo.class, field));
    }

}
