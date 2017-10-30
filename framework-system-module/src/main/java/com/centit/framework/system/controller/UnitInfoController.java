package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.SysRoleManager;
import com.centit.framework.system.service.SysUnitManager;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.support.algorithm.ListOpt;
import com.centit.support.json.JsonPropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
 * Date: 14-10-28
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/unitinfo")
public class UnitInfoController extends BaseController {

    @Resource
    @NotNull
    private SysUnitManager sysUnitManager;

    @Resource
    @NotNull
    private SysUserManager sysUserMag;

    @Resource
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Resource
    @NotNull
    private SysRoleManager sysRoleManager;

    /**
     * 系统日志中记录
     */
    private String optId = "UNITMAG";// CodeRepositoryUtil.getCode("OPTID", "unitInfo");

    /**
     * 查询所有机构信息
     *
     * @param field    需要显示的字段
     * @param struct    boolean
     * @param id        id
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, boolean struct, String id,
                     HttpServletRequest request,
                     HttpServletResponse response) {

        Map<String, Object> searchColumn = convertSearchColumn(request);
        String unitName = (String)searchColumn.get("unitName");

        if(StringUtils.isNotBlank(unitName) && StringUtils.isBlank(id)){

            List<UnitInfo> listObjects= sysUnitManager.listObjects(searchColumn);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            if(struct){
                ja = ListOpt.srotAsTreeAndToJSON(ja, (p, c) ->
                                StringUtils.equals(
                                        ((JSONObject)p).getString("unitCode"),
                                        ((JSONObject)c).getString("parentUnit")), "children");
            }
            JsonResultUtils.writeSingleDataJson(ja,
                    response, JsonPropertyUtils.getIncludePropPreFilter(JSONObject.class, field));
        }else{
            Map<String,Object> filterMap = new HashMap<>();
            if (StringUtils.isNotBlank(id)) {
                filterMap.put("parentUnit", id);
            }else{
                filterMap.put("NP_TOPUnit", "true");
            }
            List<UnitInfo>  listObjects= sysUnitManager.listObjects(filterMap);
            sysUnitManager.checkState(listObjects);
             /*for (UnitInfo unit : listObjects) {
                 unit.setState(sysUnitManager.hasChildren(unit.getUnitCode())?
                   "closed":"open");
            }*/
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            JsonResultUtils.writeSingleDataJson(ja, response, null);
        }
    }

    /**
     * 查询所有子机构信息
     *
     * @param field    需要显示的字段
     * @param id    String parentUnit 父类机构
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/subunits",method = RequestMethod.GET)
    public void listSub(String[] field, String id,
                        HttpServletRequest request, HttpServletResponse response) {

        UserInfo user=sysUserMag.getObjectById(this.getLoginUser(request).getUserCode());
        Map<String,Object> filterMap = new HashMap<>();
        filterMap.put("parentUnit", StringUtils.isNotBlank(id) ? id : user.getPrimaryUnit());
        List<UnitInfo> listObjects = sysUnitManager.listObjects(filterMap);
        if(listObjects == null){
            JsonResultUtils.writeSuccessJson(response);
            return;
        }
//        Collections.sort(listObjects, (o1, o2) -> {
//            if (o2.getUnitOrder() == null && o1.getUnitOrder() == null) {
//                return 0;
//            }
//            if (o2.getUnitOrder() == null) {
//                return 1;
//            }
//            if (o1.getUnitOrder() == null) {
//                return -1;
//            }
//            return Long.compare(o1.getUnitOrder(), o2.getUnitOrder());
//        });
        sysUnitManager.checkState(listObjects);
        JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
//        if(struct){
//            ja = ListOpt.srotAsTreeAndToJSON(ja, (p, c) ->
//                    StringUtils.equals(
//                                    ((JSONObject)p).getString("unitCode"),
//                                    ((JSONObject)c).getString("parentUnit")),
//                    "children");
//        }
        JsonResultUtils.writeSingleDataJson(ja,
                response, JsonPropertyUtils.getIncludePropPreFilter(JSONObject.class, field));
      }

    /**
     * 获取当前机构及其下属机构
     * @param id    String parentUnit 父类机构
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/underunits", method = RequestMethod.GET)
    public void allunits(String id, HttpServletRequest request, HttpServletResponse response) {
        UserInfo user=sysUserMag.getObjectById(this.getLoginUser(request).getUserCode());
        Map<String,Object> filterMap = new HashMap<>();
        if (StringUtils.isNotBlank(id)) {
            filterMap.put("parentUnit", id);
        }else{
            filterMap.put("unitCode", user.getPrimaryUnit());
        }
        List<UnitInfo> listObjects = sysUnitManager.listObjects(filterMap);
        if(listObjects == null){
            JsonResultUtils.writeSuccessJson(response);
            return;
        }
        sysUnitManager.checkState(listObjects);
        JSONArray ja = new JSONArray();
        for(UnitInfo u : listObjects){
            JSONObject json = (JSONObject)JSON.toJSON(u);
            json.put("id", u.getUnitCode());
            json.put("text",u.getUnitName());
            ja.add(json);
        }
//        JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
        JsonResultUtils.writeSingleDataJson(ja, response);
    }


    /**
     * 查询单个机构信息
     *
     * @param unitCode 机构代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.GET)
    public void getUnitInfo(@PathVariable String unitCode, HttpServletResponse response) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);

        JsonResultUtils.writeSingleDataJson(unitInfo, response);
    }

    /**
     * 删除机构
     * @param request HttpServletRequest
     * @param unitCode unitCode
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable String unitCode,HttpServletRequest request, HttpServletResponse response) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);
        if(unitInfo==null){
            JsonResultUtils.writeErrorMessageJson("The object not found!", response);
            return;
        }
        List<UserUnit> userUnits = sysUserUnitManager.listUnitUsersByUnitCode(unitCode);
        if(userUnits != null && userUnits.size() != 0){
          JsonResultUtils.writeErrorMessageJson("该机构包含组织信息，不能删除！", response);
          return;
        }
        sysUnitManager.deleteUnitInfo(unitInfo);

        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
        OperationLogCenter.logDeleteObject(request,optId,unitInfo.getUnitCode(), OperationLog.P_OPT_LOG_METHOD_D,
                "删除机构"+unitInfo.getUnitName(), unitInfo);
        /*********log*********/
    }


    /**
     * 新建机构
     *
     * @param unitInfo UnitInfo
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    public void create(@Valid UnitInfo unitInfo, HttpServletRequest request,HttpServletResponse response) {

        if(!sysUnitManager.isUniqueName(unitInfo)){
            JsonResultUtils.writeErrorMessageJson(
                    ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                    "机构名"+unitInfo.getUnitName()+"已存在，请更换！", response);
            return;
        }
        sysUnitManager.saveNewUnitInfo(unitInfo);

        JsonResultUtils.writeSingleDataJson(unitInfo, response);


        /*********log*********/
        OperationLogCenter.logNewObject(request,optId,unitInfo.getUnitCode(),
                OperationLog.P_OPT_LOG_METHOD_C,  "新增机构" , unitInfo);
        /*********log*********/
    }

    /**
     * 更新机构信息
     *
     * @param unitCode 机构代码
     * @param unitInfo UnitInfo
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}", method = RequestMethod.PUT)
    public void edit(@PathVariable String unitCode, @Valid UnitInfo unitInfo,
            HttpServletRequest request,HttpServletResponse response) {

        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            JsonResultUtils.writeErrorMessageJson("机构不存在", response);
            return;
        }
        if(!sysUnitManager.isUniqueName(unitInfo)){
            JsonResultUtils.writeErrorMessageJson(
                    ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                    "机构名"+unitInfo.getUnitName()+"已存在，请更换！", response);
            return;
        }

        UnitInfo oldValue = new UnitInfo();
        oldValue.copy(dbUnitInfo);

        sysUnitManager.updateUnitInfo(unitInfo);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
        OperationLogCenter.logUpdateObject(request, optId, unitCode, OperationLog.P_OPT_LOG_METHOD_U,
                "更新机构信息", unitInfo, oldValue);
        /*********log*********/
    }

    /**
     * 更新机构及子机构的状态
     *
     * @param unitCode    机构代码
     * @param statusValue 状态码 T 或 F
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/status/{statusValue}", method = RequestMethod.PUT)
    public void changeStatus(@PathVariable String unitCode, @PathVariable String statusValue,
            HttpServletRequest request,HttpServletResponse response) {
        UnitInfo dbUnitInfo = sysUnitManager.getObjectById(unitCode);
        if (null == dbUnitInfo) {
            JsonResultUtils.writeErrorMessageJson("机构不存在", response);

            return;
        }

        if (!"T".equals(statusValue) && !"F".equals(statusValue)) {
            JsonResultUtils.writeErrorMessageJson("机构状态不正确", response);

            return;
        }

        sysUnitManager.changeStatus(unitCode, statusValue);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
         String optContent = "更新机构状态,机构名称:" + CodeRepositoryUtil.getCode(CodeRepositoryUtil.UNIT_CODE, unitCode) + ",机构是否启用:" + ("T".equals
                (statusValue) ? "是" : "否");
        OperationLogCenter.log(request,optId,unitCode, OperationLog.P_OPT_LOG_METHOD_U,  optContent);
        /*********log*********/
    }

    /**
     * 获取单个机构下属子机构
     *
     * @param field    需要显示的字段
     * @param unitCode 机构代码
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/children", method = RequestMethod.GET)
    public void listChildren(@PathVariable String unitCode, String[] field, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.put("parentUnit", unitCode);

        List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);

        JsonResultUtils.writeSingleDataJson(listObjects, response, JsonPropertyUtils.getIncludePropPreFilter(UnitInfo.class, field));
    }

    /**
     * 当前机构下所有用户
     *
     * @param unitCode 机构代码
     * @param pageDesc 分页信息
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{unitCode}/users", method = RequestMethod.GET)
    public void listUnitUsers(@PathVariable String unitCode, PageDesc pageDesc,
                              HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.put("unitCode", unitCode);

        List<UserInfo> listObjects = sysUserMag.listObjects(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value = "/{unitCode}/validusers", method = RequestMethod.GET)
    public void listUnitAllUsers(@PathVariable String unitCode, HttpServletResponse response) {

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("unitCode", unitCode);
        filterMap.put("isValid", "T");
        List<UserInfo> listObjects = sysUserMag.listObjects(filterMap);

        JsonResultUtils.writeSingleDataJson(listObjects, response);
    }

    /**
     * 当前机构下用户
     *
     * @param userunitid    机构代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/unitusers/{userunitid}", method = RequestMethod.GET)
    public void getUnitUser(@PathVariable String userunitid,  HttpServletResponse response) {
        UserUnit userUnit = sysUserUnitManager.getObjectById(userunitid);

        if (null == userUnit) {

            JsonResultUtils.writeErrorMessageJson("当前机构中无此用户", response);
            return;
        }

        JsonResultUtils.writeSingleDataJson(userUnit, response);
    }

    /**
     * 将权限付给部门
     * @param unitcode unitcode
     * @param optCodes optCodes
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/unit/saveopts/{unitcode}",method = RequestMethod.POST)
    public void setUnitPowers(@PathVariable String unitcode,
            String optCodes,
            HttpServletRequest request,HttpServletResponse response) {
        String optCodesArray[]=optCodes.split(",");
        RoleInfo roleInfo = sysRoleManager.getObjectById("G$"+ unitcode);
        if(roleInfo==null){
            roleInfo = new RoleInfo();
            roleInfo.setIsValid("T");
            roleInfo.setRoleCode("G$"+ unitcode);
            roleInfo.setRoleName("赋给部门"+unitcode+"的权限");
            roleInfo.setRoleDesc(roleInfo.getRoleName());
            roleInfo.setRoleType("D");
            roleInfo.setCreateDate(new Date());
            sysRoleManager.saveNewRoleInfo(roleInfo);
            //刷新缓存
            sysRoleManager.loadRoleSecurityMetadata();
        }

        List<RolePower> rolePowers = new ArrayList<>();
        //为空时更新RoleInfo中字段数据
       if (ArrayUtils.isNotEmpty(optCodesArray)) {
           for (String optCode : optCodesArray) {
               if(StringUtils.isNotBlank(optCode))
                   rolePowers.add(new RolePower(new RolePowerId(roleInfo.getRoleCode(), optCode)));
           }
       }

       roleInfo.addAllRolePowers(rolePowers);
       sysRoleManager.updateRolePower(roleInfo);
       sysRoleManager.loadRoleSecurityMetadata();
       JsonResultUtils.writeBlankJson(response);
       /*********log*********/
       OperationLogCenter.logNewObject(request,optId, roleInfo.getRoleCode(), OperationLog.P_OPT_LOG_METHOD_U,
               "更新机构权限",roleInfo);
       /*********log*********/
    }
}
