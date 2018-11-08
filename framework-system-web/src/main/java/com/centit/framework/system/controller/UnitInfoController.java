package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.po.*;
import com.centit.framework.system.service.*;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-10-28
 * Time: 下午1:32
 * 机构管理Controller
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

    @Resource
    @NotNull
    private SysUnitRoleManager sysUnitRoleManager;

    @Resource
    private PlatformEnvironment platformEnvironment;
    /**
     * 系统日志中记录
     * @return 业务标识ID
     */
    //private String optId = "UNITMAG";// CodeRepositoryUtil.getCode("OPTID", "unitInfo");
    public String getOptId() {
        return  "UNITMAG";
    }
    /**
     * 查询所有机构信息
     *
     * @param struct    boolean
     * @param id        id
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(boolean struct, String id,
                     HttpServletRequest request,
                     HttpServletResponse response) {

        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        String unitName = (String)searchColumn.get("unitName");

        if(StringUtils.isNotBlank(unitName) && StringUtils.isBlank(id)){

            List<UnitInfo> listObjects= sysUnitManager.listObjects(searchColumn);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            if(struct){
                ja = CollectionsOpt.srotAsTreeAndToJSON(ja, (p, c) ->
                                StringUtils.equals(
                                        ((JSONObject)p).getString("unitCode"),
                                        ((JSONObject)c).getString("parentUnit")), "children");
            }
            JsonResultUtils.writeSingleDataJson(ja, response);
        }else{
            Map<String,Object> filterMap = new HashMap<>(2);
            if (StringUtils.isNotBlank(id)) {
                filterMap.put("parentUnit", id);
            }else{
                filterMap.put("NP_TOPUnit", "true");
            }
            List<UnitInfo>  listObjects= sysUnitManager.listObjects(filterMap);
            sysUnitManager.checkState(listObjects);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            JsonResultUtils.writeSingleDataJson(ja, response, null);
        }
    }

    /**
     * 查询所有子机构信息
     * @param id    String parentUnit 父类机构
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/subunits",method = RequestMethod.GET)
    public void listSub(String id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        String currentUnitCode = WebOptUtils.getLoginUser().getCurrentUnitCode();

        String unitName = StringBaseOpt.castObjectToString(searchColumn.get("unitName"));

        if(StringUtils.isNotBlank(unitName) && StringUtils.isBlank(id)){

            List<UnitInfo> listObjects= sysUnitManager.listObjects(searchColumn);
            JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
            JsonResultUtils.writeSingleDataJson(ja, response);
        }else{
            Map<String,Object> filterMap = new HashMap<>(2);
            if (StringUtils.isNotBlank(id)) {
              filterMap.put("parentUnit", id);
            }else{
              filterMap.put("parentUnit", StringUtils.isNotBlank(id) ? id : currentUnitCode);
            }
            List<UnitInfo>  listObjects= sysUnitManager.listAllSubUnits(currentUnitCode);

          JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
          for(Object o : ja){
            ((JSONObject)o).put("state", "open");
            ((JSONObject)o).put("id", ((JSONObject) o).getString("unitCode"));
            ((JSONObject)o).put("text", ((JSONObject) o).getString("unitName"));
          }
          ja = CollectionsOpt.srotAsTreeAndToJSON(ja, (p, c) ->
            StringUtils.equals(
              ((JSONObject)p).getString("unitCode"),
              ((JSONObject)c).getString("parentUnit")), "children");
          JsonResultUtils.writeSingleDataJson(ja, response, null);
        }
      }

    /**
     * 查询 当前机构 子机构
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/validsubunits",method = RequestMethod.GET)
    public void listValidSubUnit(HttpServletRequest request, HttpServletResponse response) {

        String currentUnitCode = WebOptUtils.getLoginUser().getCurrentUnitCode();
        List<UnitInfo>  listObjects= sysUnitManager.listValidSubUnits(currentUnitCode);

        JSONArray ja = DictionaryMapUtils.objectsToJSONArray(listObjects);
        for(Object o : ja){
            ((JSONObject)o).put("state", "open");
            ((JSONObject)o).put("id", ((JSONObject) o).getString("unitCode"));
            ((JSONObject)o).put("text", ((JSONObject) o).getString("unitName"));
        }
        ja = CollectionsOpt.srotAsTreeAndToJSON(ja, (p, c) ->
        StringUtils.equals(
          ((JSONObject)p).getString("unitCode"),
          ((JSONObject)c).getString("parentUnit")),
            "children");
        JsonResultUtils.writeSingleDataJson(ja, response, null);
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除机构")
    public void delete(@PathVariable String unitCode,HttpServletRequest request, HttpServletResponse response) {
        UnitInfo unitInfo = sysUnitManager.getObjectById(unitCode);
        if(unitInfo==null){
            JsonResultUtils.writeErrorMessageJson("The object not found!", response);
            return;
        }

        List<UserUnit> userUnits = sysUserUnitManager.listUnitUsersByUnitCode(unitCode);
        if(userUnits != null && userUnits.size() != 0){
          JsonResultUtils.writeErrorMessageJson("该机构存在关联用户，不能删除！", response);
          return;
        }
        sysUnitManager.deleteUnitInfo(unitInfo);
        //删除unitrole关系表
        JSONArray unitRoles = sysUnitRoleManager.listUnitRoles(unitCode,new PageDesc());
        if (unitRoles != null && unitRoles.size()>0){
            for (Object obj : unitRoles){
                sysUnitRoleManager.deleteUnitRole(unitCode,
                    JSONObject.toJavaObject((JSON) DictionaryMapUtils.objectToJSON(obj),UnitRole.class).getRoleCode());
            }
        }

        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//        OperationLogCenter.logDeleteObject(request,optId,unitInfo.getUnitCode(), OperationLog.P_OPT_LOG_METHOD_D,
//                "删除机构"+unitInfo.getUnitName(), unitInfo);
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增机构")
    public void create(@Valid UnitInfo unitInfo, HttpServletRequest request,HttpServletResponse response) {

        if(!sysUnitManager.isUniqueName(unitInfo)){
            JsonResultUtils.writeErrorMessageJson(
                    ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                    "机构名"+unitInfo.getUnitName()+"已存在，请更换！", response);
            return;
        }
        HashMap<String,Object> map = new HashMap();
        map.put("unitWord",unitInfo.getUnitWord());
        List<UnitInfo> unitInfos = sysUnitManager.listObjects(map);
        if (unitInfos != null && unitInfos.size()>0){
            JsonResultUtils.writeErrorMessageJson(
                ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                "机构编码"+unitInfo.getUnitWord()+"已存在，请更换！", response);
            return;
        }
      while (!sysUnitManager.isUniqueOrder(unitInfo)){
        unitInfo.setUnitOrder(unitInfo.getUnitOrder()+1);
      }
        sysUnitManager.saveNewUnitInfo(unitInfo);

        JsonResultUtils.writeSingleDataJson(unitInfo, response);


        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId,unitInfo.getUnitCode(),
//                OperationLog.P_OPT_LOG_METHOD_C,  "新增机构" , unitInfo);
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构")
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

        HashMap<String,Object> map = new HashMap();
        map.put("unitWord",unitInfo.getUnitWord());
        List<UnitInfo> unitInfos = sysUnitManager.listObjects(map);
        if (unitInfos != null && unitInfos.size()>0){
            if (!unitCode.equals(unitInfos.get(0).getUnitCode())){
                JsonResultUtils.writeErrorMessageJson(
                    ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                    "机构编码"+unitInfo.getUnitWord()+"已存在，请更换！", response);
                return;
            }
        }

        if("F".equals(unitInfo.getIsValid())){
            List<UnitInfo> units = sysUnitManager.listValidSubUnit(unitCode);
            if(units != null && units.size() != 0){
              JsonResultUtils.writeErrorMessageJson("该机构包含下级机构，不能设为禁用！", response);
              return;
            }
            List<UserUnit> userUnits = sysUserUnitManager.listUnitUsersByUnitCode(unitCode);
            if(userUnits != null && userUnits.size() != 0){
              JsonResultUtils.writeErrorMessageJson("该机构存在关联用户，不能设为禁用！", response);
              return;
            }
        }

        UnitInfo oldValue = new UnitInfo();
        oldValue.copy(dbUnitInfo);

        sysUnitManager.updateUnitInfo(unitInfo);

      JsonResultUtils.writeSingleDataJson(unitInfo, response);

        /*********log*********/
//        OperationLogCenter.logUpdateObject(request, optId, unitCode, OperationLog.P_OPT_LOG_METHOD_U,
//                "更新机构信息", unitInfo, oldValue);
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构状态")
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
//         String optContent = "更新机构状态,机构名称:" + CodeRepositoryUtil.getCode(CodeRepositoryUtil.UNIT_CODE, unitCode) + ",机构是否启用:" + ("T".equals
//                (statusValue) ? "是" : "否");
//        OperationLogCenter.log(request,optId,unitCode, OperationLog.P_OPT_LOG_METHOD_U,  optContent);
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
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        searchColumn.put("parentUnit", unitCode);

        List<UnitInfo> listObjects = sysUnitManager.listObjects(searchColumn);

        JsonResultUtils.writeSingleDataJson(listObjects, response, JsonPropertyUtils.getIncludePropPreFilter(UnitInfo.class, field));
    }

    /**
     * 当前机构下所有用户
     *
     * @param pageDesc 分页信息
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/currentunit/users", method = RequestMethod.GET)
    public void listUnitUsers(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {

        String currentUnitCode = WebOptUtils.getLoginUser().getCurrentUnitCode();

        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);
        searchColumn.put("unitCode", currentUnitCode);

        //特殊字符转义
        if (searchColumn.get("userName") != null){
            searchColumn.put("likeUserOrLoginName", StringEscapeUtils.escapeHtml4(searchColumn.get("userName").toString()));
            searchColumn.remove("userName");
        }

        List<UserInfo> listObjects = sysUserMag.listObjects(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

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
   * 获取当前用户所在机构下所有用户
   * @param state 是否启用 T|F
   * @param request HttpServletRequest
   * @param response {@link HttpServletResponse}
   */
    @RequestMapping(value = "/currentusers/{state}", method = RequestMethod.GET)
    public void listAllUsersByCurrentUser(@PathVariable String state, HttpServletRequest request, HttpServletResponse response) {
        CentitUserDetails userInfo =  getLoginUser(request);
        String unitCode = userInfo.getCurrentUnitCode();
        UnitInfo currentUnitInfo = sysUnitManager.getObjectById(unitCode);

        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("unitPath", currentUnitInfo.getUnitPath());
        filterMap.put("isValid", state);
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
     * @param unitCode 机构代码
     * @param optCodes 操作权限代码 以，隔开
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/unit/saveopts/{unitCode}",method = RequestMethod.POST)
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新机构权限")
    public void setUnitPowers(@PathVariable String unitCode, String optCodes,
                              HttpServletRequest request,HttpServletResponse response) {
        String[] optCodesArray = optCodes.split(",");
        RoleInfo roleInfo = sysRoleManager.getObjectById("G$"+ unitCode);
        if(roleInfo==null){
            roleInfo = new RoleInfo();
            roleInfo.setIsValid("T");
            roleInfo.setRoleCode("G$"+ unitCode);
            roleInfo.setRoleName("赋给部门"+unitCode+"的权限");
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
                if(StringUtils.isNotBlank(optCode)) {
                    rolePowers.add(new RolePower(new RolePowerId(roleInfo.getRoleCode(), optCode)));
                }
            }
        }

        roleInfo.addAllRolePowers(rolePowers);
        sysRoleManager.updateRolePower(roleInfo);
        //sysRoleManager.loadRoleSecurityMetadata();
        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//       OperationLogCenter.logNewObject(request,optId, roleInfo.getRoleCode(), OperationLog.P_OPT_LOG_METHOD_U,
//               "更新机构权限",roleInfo);
        /*********log*********/
    }

    @GetMapping(value = "/validroles")
    public void listUnitAndPublicRole(HttpServletRequest request, HttpServletResponse response) {

        String currentUnitCode = WebOptUtils.getLoginUser().getCurrentUnitCode();
        Map<String, Object> filterMap = new HashMap<>(4);
        filterMap.put("publicUnitRole", currentUnitCode);
        filterMap.put("isValid", "T");
        List<RoleInfo> roleInfos = sysRoleManager.listObjects(filterMap);
        JsonResultUtils.writeSingleDataJson(roleInfos, response);
    }
}
