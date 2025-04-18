package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.ViewDataTransform;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.model.basedata.OptDataScope;
import com.centit.framework.model.basedata.OptInfo;
import com.centit.framework.model.basedata.OptMethod;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.framework.system.service.OptMethodManager;
import com.centit.framework.system.service.OsInfoManager;
import com.centit.framework.system.service.WorkGroupManager;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/optinfo")
@Api(value = "系统业务菜单维护接口", tags = "系统业务菜单操作接口")
public class OptInfoController extends BaseController {
    @Autowired
    private OptInfoManager optInfoManager;

    @Autowired
    private OptMethodManager optMethodManager;

    @Autowired
    private OsInfoManager osInfoMag;

    @Autowired
    private WorkGroupManager workGroupManager;
    /*
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "OPTINFO";//CodeRepositoryUtil.getCode("OPTID", "optInfo");
    public String getOptId() {
        return "OPTINFO";
    }

    /*
     * 查询所有系统业务
     *
     * @param id      父id
     * @param request HttpServletRequest
     */
    @ApiOperation(value = "查询所有系统业务", notes = "根据某个父级系统业务id查询下面的所有系统业务。")
    @ApiImplicitParam(
        name = "id", value = "父级系统业务id",
        paramType = "query", dataType = "String")
    @RequestMapping(value = "/sub", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listFromParent(String id, HttpServletRequest request) {
        WebOptUtils.assertUserLogin(request);
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        if (StringUtils.isNotBlank(id)) {
            searchColumn.put("preOptId", id);
        } else {
            searchColumn.put("NP_TOPOPT", "true");
        }
        List<OptInfo> listObjects;
        searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        listObjects = optInfoManager.listFromParent(searchColumn);
        for (OptInfo opt : listObjects) {
            opt.setState(optInfoManager.hasChildren(opt.getOptId()) ? "closed" : "open");
        }
        return ResponseData.makeResponseData(makeMenuFuncsJson(listObjects));
    }

    private JSONArray makeMenuFuncsJson(List<OptInfo> menuFunsByUser) {
        return ViewDataTransform.makeTreeViewJson(menuFunsByUser,
            ViewDataTransform.createStringHashMap("id", "optId",
                "optId", "optId",
                "optCode", "optId",
                "pid", "preOptId",
                "text", "localOptName",
                "url", "optRoute",
                "icon", "icon",
                "children", "children",
                "isInToolbar", "isInToolbar",
                "state", "state",
                "optMethods", "optMethods",
                "topOptId","topOptId"
            ), (jsonObject, obj) -> jsonObject.put("external", !("D".equals(obj.getPageType()))));
    }

    /*
     * 查询所有需要通过权限管理的业务
     */
    @ApiOperation(value = "查询所有需要通过权限管理的业务", notes = "查询所有需要通过权限管理的业务。")
    @RequestMapping(value = "/poweropts", method = RequestMethod.GET)
    @WrapUpResponseBody
    public JSONArray listPowerOpts(HttpServletRequest request) {
        List<OptInfo> listObjects =
            optInfoManager.listFromParent(CollectionsOpt.createHashMap(
                "topUnit", WebOptUtils.getCurrentTopUnit(request)));
        listObjects = optInfoManager.listObjectFormatTree(listObjects, true);
        return makeMenuFuncsJson(listObjects);
    }

    /*
     * 查询所有项目权限管理的业务
     *
     * @param field 需要显示的字段
     */
    @ApiOperation(value = "查询所有项目权限管理的业务", notes = "查询所有项目权限管理的业务。")
    @ApiImplicitParam(
        name = "field", value = "需要显示的字段",
        allowMultiple = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "/itempoweropts", method = RequestMethod.GET)
    @WrapUpResponseBody
    public JSONArray listItemPowerOpts(String[] field) {
        List<OptInfo> listObjects = optInfoManager.listItemPowerOpts();
        listObjects = optInfoManager.listObjectFormatTree(listObjects, true);

        return DictionaryMapUtils.objectsToJSONArray(listObjects, field);
    }


    /*
     * 查询某个部门权限的业务
     *
     * @param field    需要显示的字段
     * @param unitCode unitCode
     */
    @ApiOperation(value = "查询某个部门权限的业务", notes = "查询某个部门权限的业务。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value = "机构编码",
            required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(
            name = "field", value = "需要显示的字段",
            allowMultiple = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/unitpoweropts/{unitCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listUnitPowerOpts(@PathVariable String unitCode, String[] field) {
        List<OptInfo> listObjects = optInfoManager.listOptWithPowerUnderUnit(unitCode);
        listObjects = optInfoManager.listObjectFormatTree(listObjects, false);

        return ResponseData.makeResponseData(makeMenuFuncsJson(listObjects));
    }

    private void judgePower(String osId, HttpServletRequest request){
        CentitUserDetails userDetails = WebOptUtils.assertUserDetails(request);
        OsInfo osInfo = osInfoMag.getObjectById(osId);
        if (osInfo == null) {
            throw new ObjectException("业务系统不存在");
        }
        if(!StringUtils.equals(osInfo.getTopUnit(), userDetails.getTopUnitCode())){
            throw new ObjectException(ResponseData.ERROR_UNAUTHORIZED,
                getI18nMessage("error.401.unauthorized", request));
        }
        if(! userDetails.checkUserRole("platadmin") && ! userDetails.checkUserRole("tenantadmin") ){
            if( ! workGroupManager.loginUserIsExistWorkGroup(osId, userDetails.getUserCode()))
                throw new ObjectException(ResponseData.HTTP_UNAUTHORIZED, "您没有权限更新菜单!");
        }
    }
    /*
     * 新增菜单
     *
     * @param optInfo 业务菜单信息
     */
    @ApiOperation(value = "新建系统业务菜单", notes = "新建系统业务菜单。")
    @ApiImplicitParams(@ApiImplicitParam(
        name = "optInfo", value = "业务菜系信息",
        required = true, paramType = "body", dataTypeClass = OptInfo.class
    ))
    @RequestMapping(method = {RequestMethod.POST})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增{optInfo.optName}菜单",
        tag = "{optInfo.optId}:{optInfo.optName}")
    @WrapUpResponseBody
    public OptInfo createOptInfo(@Valid OptInfo optInfo, HttpServletRequest request) {
        String optOsId = optInfo.getOsId();
        if(StringUtils.isBlank(optOsId)){
            optOsId = optInfo.getTopOptId();
        }
        judgePower(optOsId, request);
        optInfo.setOptName(StringEscapeUtils.unescapeHtml4(optInfo.getOptName()));
        optInfoManager.saveNewOptInfo(optInfo);
        return optInfo;
    }

    /*
     * optId是否已存在
     *
     * @param optId optId
     * @throws IOException IOException
     */
    @ApiOperation(value = "检查业务菜单是否存在", notes = "根据菜单id检查业务菜单是否存在。")
    @ApiImplicitParam(
        name = "optId", value = "业务菜单id",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/notexists/{optId}", method = {RequestMethod.GET})
    @WrapUpResponseBody(contentType = WrapUpContentType.RAW)
    public boolean isNotExists(@PathVariable String optId) throws IOException {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
//        JsonResultUtils.writeOriginalObject(null == optInfo, response);
        return null == optInfo;
    }

    /*
     * 更新菜单
     *
     * @param optId   主键
     * @param optInfo OptInfo
     */
    @ApiOperation(value = "查询某个部门权限的业务", notes = "查询某个部门权限的业务。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value = "菜单id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optInfo", value = "更新的菜单对象",
            required = true, paramType = "body", dataTypeClass = OptInfo.class)
    })
    @RequestMapping(value = "/{optId}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新菜单",
        tag = "{optId}")
    @WrapUpResponseBody
    public ResponseData edit(@ParamName("optId") @PathVariable String optId, @Valid OptInfo optInfo, HttpServletRequest request) {
        String optOsId = optInfo.getOsId();
        if(StringUtils.isBlank(optOsId)){
            optOsId = optInfo.getTopOptId();
        }
        judgePower(optOsId, request);
        optInfo.setOptName(StringEscapeUtils.unescapeHtml4(optInfo.getOptName()));
        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }

        if (!StringUtils.equals(dbOptInfo.getPreOptId(), optInfo.getPreOptId())) {
            if(optInfo.getPreOptId()==null){
                optInfo.setPreOptId("0");
            }
            OptInfo parentOpt = optInfoManager.getOptInfoById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId(dbOptInfo.getPreOptId());
            }
        }

        optInfoManager.updateOptInfo(optInfo);
        return ResponseData.makeResponseData(dbOptInfo);
    }

    /*
     * 更新操作权限
     *
     * @param optId   主键
     * @param optInfo OptInfo
     */
    @ApiOperation(value = "批量更新操作权限", notes = "批量更新操作权限。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value = "菜单id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optInfo", value = "更新的菜单对象",
            required = true, paramType = "body", dataTypeClass = OptInfo.class)
    })
    @RequestMapping(value = "/editpower/{optId}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新操作权限",
        tag = "{optId}")
    @WrapUpResponseBody
    public ResponseData editPower(@ParamName("optId") @PathVariable String optId, @Valid OptInfo optInfo,
                                  HttpServletRequest request) {
        optInfo.setOptName(StringEscapeUtils.unescapeHtml4(optInfo.getOptName()));
        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                getI18nMessage("error.604.object_not_found", request,
                    "OptInfo", optId));//"当前对象不存在");
        }

        if (!StringUtils.equals(dbOptInfo.getPreOptId(), optInfo.getPreOptId())) {
            OptInfo parentOpt = optInfoManager.getOptInfoById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId(dbOptInfo.getPreOptId());
            }
        }

        for (OptMethod optDef : optInfo.getOptMethods()) {
            optDef.setOptId(optId);
            if (StringUtils.isBlank(optDef.getOptCode())) {
                optDef.setOptCode(optMethodManager.getNextOptCode());
            }
        }
        for (OptDataScope optDef : optInfo.getDataScopes()) {
           optDef.setFilterCondition(StringEscapeUtils.unescapeHtml4(optDef.getFilterCondition()));
        }
    /*  dbOptInfo.addAllOptMethods(optInfo.getOptMethods());
        dbOptInfo.addAllDataScopes(optInfo.getDataScopes());*/
        optInfoManager.updateOperationPower(optInfo);
        return ResponseData.successResponse;
    }

    /*
     * 删除菜单
     *
     * @param optId 主键
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单。")
    @ApiImplicitParam(
        name = "optId", value = "菜单id",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{optId}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除菜单",
        tag = "{optId}")
    @WrapUpResponseBody
    public ResponseData delete(@ParamName("optId") @PathVariable String optId, HttpServletRequest request) {
        int hasChild = optInfoManager.countSubOptInfo(optId);
        if(hasChild > 0){
            throw new ObjectException(optId, ObjectException.DATA_NOT_INTEGRATED,
                getI18nMessage("error.610.cannot_delete_parent", request, "Children-"+optId));
            //"不能删除有子菜单的菜单！");
        }
        optInfoManager.deleteOptInfoById(optId);
        return ResponseData.successResponse;
    }

    /*
     * 查询单条数据
     *
     * @param optId 主键
     */
    @ApiOperation(value = "查询单条数据", notes = "根据菜单id查询单条数据。")
    @ApiImplicitParam(
        name = "optId", value = "菜单id",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{optId}", method = {RequestMethod.GET})
    @WrapUpResponseBody
    public ResponseData getOptInfoById(@PathVariable String optId) {
        OptInfo dbOptInfo = optInfoManager.getOptInfoById(optId);
        return ResponseData.makeResponseData(dbOptInfo);
    }

    /*
     * 新增页面时获取OptDef主键
     */
    @ApiOperation(value = "获取菜单的下个主键", notes = "获取菜单的下个主键。")
    @RequestMapping(value = "/nextOptCode", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData getNextOptCode() {
        String optCode = optMethodManager.getNextOptCode();
        ResponseMapData responseData = new ResponseMapData();
        responseData.addResponseData("optCode", optCode);
        return responseData;
    }

    /*
     * 新建或更新业务操作
     *
     * @param optId   主键
     * @param optDef  OptMethod
     */
    @ApiOperation(value = "新建业务操作", notes = "新建业务操作。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value = "菜单id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optDef", value = "更新的菜单操作方法对象",
            required = true, paramType = "body", dataTypeClass = OptMethod.class)
    })
    @RequestMapping(value = "/{optId}", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData optDefSave(@PathVariable String optId, @Valid OptMethod optDef,
                                   HttpServletRequest request) {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
        if (null == optInfo) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_INTEGRATED,
                getI18nMessage("error.604.object_not_found", request, "OptInfo", optId));
            //"数据库不匹配,数据库中不存在optId为" + optId + "的业务信息。");
        }
        optMethodManager.saveNewObject(optDef);
        return ResponseData.successResponse;
    }

    @ApiOperation(value = "更新业务操作", notes = "更新业务操作。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value = "菜单id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optCode", value = "菜单英文代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optDef", value = "更新的菜单操作方法对象",
            required = true, paramType = "body", dataTypeClass = OptMethod.class)
    })
    @RequestMapping(value = "/{optId}/{optCode}", method = RequestMethod.PUT)
    @WrapUpResponseBody
    public ResponseData optDefEdit(@PathVariable String optId, @PathVariable String optCode,
                                   @Valid OptMethod optDef, HttpServletRequest request) {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
        if (null == optInfo) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_INTEGRATED,
                getI18nMessage("error.604.object_not_found", request, "OptInfo", optId));
            //"数据库不匹配,数据库中不存在optId为" + optId + "的业务信息。");
        }
        optDef.setOptCode(optCode);
        optMethodManager.updateOptMethod(optDef);
        return ResponseData.successResponse;
    }

    /*
     * 获取所有的业务菜单
     */
    @ApiOperation(value = "获取所有的业务菜单", notes = "获取所有的业务菜单。")
    @RequestMapping(value = "/allOptInfo", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData loadAllOptInfo(HttpServletRequest request) {
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        List<OptInfo> optInfos = optInfoManager.listAllOptInfoByUnit(topUnit);
        return ResponseData.makeResponseData(optInfos);
    }

    /*
     * 获取所有的操作方法
     */
    @ApiOperation(value = "获取所有的操作方法", notes = "获取所有的操作方法。")
    @RequestMapping(value = "/allOptMethod", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData loadAllOptMethod(HttpServletRequest request) {
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        List<OptMethod> optDefs = optMethodManager.listAllOptMethodByUnit(topUnit);
        return ResponseData.makeResponseData(optDefs);
    }

    /*
     * 获取用户的操作方法
     *
     * @param userCode 用户ID
     */
    @ApiOperation(value = "查询单条数据", notes = "根据菜单id查询单条数据。")
    @ApiImplicitParam(
        name = "userCode", value = "用户id",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/userpoweropts/{userCode}", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData listUserOpts(@PathVariable String userCode) {
//        List<OptInfo> optInfos = (List<OptInfo>) platformEnvironment.listUserMenuOptInfos(userCode, false);
        List<OptInfo> optInfos = optInfoManager.listUserAllPower(userCode, false);
        optInfos = optInfoManager.listObjectFormatTree(optInfos, true);
        return ResponseData.makeResponseData(makeMenuFuncsJson(optInfos));
    }

}
