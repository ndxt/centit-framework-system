package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.ViewDataTransform;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.framework.system.service.OptMethodManager;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/optinfo")
@Api(value = "系统业务菜单维护接口", tags = "系统业务菜单操作接口")
public class OptInfoController extends BaseController {
    @Resource
    private OptInfoManager optInfoManager;

    @Resource
    private OptMethodManager optMethodManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    //private String optId = "OPTINFO";//CodeRepositoryUtil.getCode("OPTID", "optInfo");
    public String getOptId() {
        return "OPTINFO";
    }

    /**
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
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);

        if (StringUtils.isNotBlank(id)) {
            searchColumn.put("preOptId", id);
        } else {
            searchColumn.put("NP_TOPOPT", "true");
        }

        List<OptInfo> listObjects = optInfoManager.listObjects(searchColumn);

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
                "text", "optName",
                "url", "optRoute",
                "icon", "icon",
                "children", "children",
                "isInToolbar", "isInToolbar",
                "state", "state",
                "optMethods", "optMethods"
            ), (jsonObject, obj) -> jsonObject.put("external", !("D".equals(obj.getPageType()))));
    }

    /**
     * 查询所有需要通过权限管理的业务
     */
    @ApiOperation(value = "查询所有需要通过权限管理的业务", notes = "查询所有需要通过权限管理的业务。")
    @RequestMapping(value = "/poweropts", method = RequestMethod.GET)
    @WrapUpResponseBody
    public JSONArray listPowerOpts() {
        List<OptInfo> listObjects = optInfoManager.listSysAndOptPowerOpts();
        listObjects = optInfoManager.listObjectFormatTree(listObjects, true);
        return makeMenuFuncsJson(listObjects);
    }


    /**
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


    /**
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

    /**
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增{optInfo.optName}菜单")
    @WrapUpResponseBody
    public OptInfo createOptInfo(@ParamName("optInfo") @Valid OptInfo optInfo) {
        optInfoManager.saveNewOptInfo(optInfo);
        CodeRepositoryCache.evictCache("OptInfo");
        return optInfo;
    }

    /**
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

    /**
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
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新菜单")
    @WrapUpResponseBody
    public ResponseData edit(@PathVariable String optId, @Valid OptInfo optInfo) {

        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }

        if (!StringUtils.equals(dbOptInfo.getPreOptId(), optInfo.getPreOptId())) {
            OptInfo parentOpt = optInfoManager.getOptInfoById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId(dbOptInfo.getPreOptId());
            }
        }

        optInfoManager.updateOptInfo(optInfo);
        return ResponseData.makeResponseData(dbOptInfo);
    }

    /**
     * 更新操作权限
     *
     * @param optId   主键
     * @param optInfo OptInfo
     */
    @ApiOperation(value = "更新操作权限", notes = "更新操作权限。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value = "菜单id",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "optInfo", value = "更新的菜单对象",
            required = true, paramType = "body", dataTypeClass = OptInfo.class)
    })
    @RequestMapping(value = "/editpower/{optId}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新操作权限")
    @WrapUpResponseBody
    public ResponseData editPower(@PathVariable String optId, @Valid OptInfo optInfo) {

        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            return ResponseData.makeErrorMessage("当前对象不存在");
        }

        if (!StringUtils.equals(dbOptInfo.getPreOptId(), optInfo.getPreOptId())) {
            OptInfo parentOpt = optInfoManager.getOptInfoById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId(dbOptInfo.getPreOptId());
            }
        }

        for (OptMethod optDef : optInfo.getOptMethods()) {
            if (StringUtils.isBlank(optDef.getOptCode())) {
                optDef.setOptCode(optMethodManager.getNextOptCode());
            }
        }

    /*  dbOptInfo.addAllOptMethods(optInfo.getOptMethods());
        dbOptInfo.addAllDataScopes(optInfo.getDataScopes());*/
        optInfoManager.updateOperationPower(optInfo);
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 删除菜单
     *
     * @param optId 主键
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单。")
    @ApiImplicitParam(
        name = "optId", value = "菜单id",
        required = true, paramType = "path", dataType = "String")
    @RequestMapping(value = "/{optId}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除菜单")
    @WrapUpResponseBody
    public ResponseData delete(@PathVariable String optId) {
        int hasChild = optInfoManager.countSubOptInfo(optId);
        if(hasChild > 0){
            throw new ObjectException(optId, ResponseData.ERROR_BAD_PROCESS_DATASCOPE,
                "不能删除有子菜单的菜单！");
        }
        optInfoManager.deleteOptInfoById(optId);
        return ResponseData.makeSuccessResponse();
    }

    /**
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

    /**
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

    /**
     * 新建或更新业务操作
     *
     * @param optId   主键
     * @param optCode optCode
     * @param optDef  OptMethod
     */
    @ApiOperation(value = "新建或更新业务操作", notes = "新建或更新业务操作。")
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
    @RequestMapping(value = "/{optId}/{optCode}", method = {RequestMethod.POST, RequestMethod.PUT})
    @WrapUpResponseBody
    public ResponseData optDefEdit(@PathVariable String optId, @PathVariable String optCode, @Valid OptMethod optDef) {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
        if (null == optInfo) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_INTERNAL_SERVER_ERROR,
                "数据库不匹配,数据库中不存在optId为" + optId + "的业务信息。");
        }
        optDef.setOptCode(optCode);
        optMethodManager.updateOptMethod(optDef);
        return ResponseData.makeSuccessResponse();
    }

    /**
     * 获取所有的业务菜单
     */
    @ApiOperation(value = "获取所有的业务菜单", notes = "获取所有的业务菜单。")
    @RequestMapping(value = "/allOptInfo", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData loadAllOptInfo() {
        List<OptInfo> optInfos = optInfoManager.listObjects();
        return ResponseData.makeResponseData(optInfos);
    }

    /**
     * 获取所有的操作方法
     */
    @ApiOperation(value = "获取所有的操作方法", notes = "获取所有的操作方法。")
    @RequestMapping(value = "/allOptMethod", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData loadAllOptMethod() {
        List<OptMethod> optDefs = optMethodManager.listObjects();
        return ResponseData.makeResponseData(optDefs);
    }

    /**
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
