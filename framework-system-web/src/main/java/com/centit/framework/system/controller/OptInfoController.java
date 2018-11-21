package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.ViewDataTransform;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.framework.system.service.OptInfoManager;
import com.centit.framework.system.service.OptMethodManager;
import com.centit.support.common.ParamName;
import com.centit.support.json.JsonPropertyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/optinfo")
@Api(value="系统业务菜单维护接口", tags= "系统业务菜单操作接口")
public class OptInfoController extends BaseController {
    @Resource
    private OptInfoManager optInfoManager;

    @Resource
    private OptMethodManager optMethodManager;

    @Resource
    private PlatformEnvironment platformEnvironment;

    /**
     * 系统日志中记录
     * @return 业务标识ID
     */
    //private String optId = "OPTINFO";//CodeRepositoryUtil.getCode("OPTID", "optInfo");
    public String getOptId() {
        return "OPTINFO";
    }

    /**
     * 查询所有系统业务
     *
     * @param id       父id
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询所有系统业务",notes="根据某个父级系统业务id查询下面的所有系统业务。")
    @ApiImplicitParam(
        name = "id", value="父级系统业务id",
        paramType = "query", dataType= "String")
    @RequestMapping(value = "/sub", method = RequestMethod.GET)
    public void listFromParent(String id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.convertSearchColumn(request);

        if (StringUtils.isNotBlank(id)) {
            searchColumn.put("preOptId", id);
        } else {
            searchColumn.put("NP_TOPOPT", "true");
        }

        List<OptInfo> listObjects = optInfoManager.listObjects(searchColumn);

        for (OptInfo opt : listObjects) {
            opt.setState(optInfoManager.hasChildren(opt.getOptId()) ? "closed" : "open");
        }
        JsonResultUtils.writeSingleDataJson(makeMenuFuncsJson(listObjects), response);
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
     *
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询所有需要通过权限管理的业务",notes="查询所有需要通过权限管理的业务。")
    @RequestMapping(value = "/poweropts", method = RequestMethod.GET)
    public void listPowerOpts(HttpServletResponse response) {
        List<OptInfo> listObjects = optInfoManager.listSysAndOptPowerOpts();
        listObjects = optInfoManager.listObjectFormatTree(listObjects, true);
        JsonResultUtils.writeSingleDataJson(makeMenuFuncsJson(listObjects), response);
    }


    /**
     * 查询所有项目权限管理的业务
     *
     * @param field    需要显示的字段
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询所有项目权限管理的业务",notes="查询所有项目权限管理的业务。")
    @ApiImplicitParam(
        name = "field", value="需要显示的字段",
        allowMultiple = true, paramType = "query", dataType= "String")
    @RequestMapping(value = "/itempoweropts", method = RequestMethod.GET)
    public void listItemPowerOpts(String[] field, HttpServletResponse response) {
        List<OptInfo> listObjects = optInfoManager.listItemPowerOpts();
        listObjects = optInfoManager.listObjectFormatTree(listObjects, true);

        if (ArrayUtils.isNotEmpty(field))
            JsonResultUtils.writeSingleDataJson(listObjects, response,
                JsonPropertyUtils.getIncludePropPreFilter(OptInfo.class, field));
        else
            JsonResultUtils.writeSingleDataJson(listObjects, response);
    }


    /**
     * 查询某个部门权限的业务
     *
     * @param field    需要显示的字段
     * @param unitCode unitCode
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询某个部门权限的业务",notes="查询某个部门权限的业务。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "unitCode", value="机构编码",
            required = true, paramType = "query", dataType= "String"),
        @ApiImplicitParam(
            name = "field", value="需要显示的字段",
            allowMultiple = true, paramType = "query", dataType= "String")
    })
    @RequestMapping(value = "/unitpoweropts/{unitCode}", method = RequestMethod.GET)
    public void listUnitPowerOpts(@PathVariable String unitCode, String[] field,
                                  HttpServletResponse response) {
        List<OptInfo> listObjects = optInfoManager.listOptWithPowerUnderUnit(unitCode);
        listObjects = optInfoManager.listObjectFormatTree(listObjects, false);

        JsonResultUtils.writeSingleDataJson(makeMenuFuncsJson(listObjects), response);
    }

    /**
     * 新增菜单
     * @param optInfo  业务菜单信息
     */
    @ApiOperation(value="新建系统业务菜单",notes="新建系统业务菜单。")
    @ApiImplicitParams(@ApiImplicitParam(
        name = "optInfo", value="业务菜系信息",
        required=true, paramType = "body", dataTypeClass= OptInfo.class
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
     * @param optId    optId
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @ApiOperation(value="检查业务菜单是否存在",notes="根据菜单id检查业务菜单是否存在。")
    @ApiImplicitParam(
        name = "optId", value="业务菜单id",
        required = true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/notexists/{optId}", method = {RequestMethod.GET})
    public void isNotExists(@PathVariable String optId, HttpServletResponse response) throws IOException {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
        JsonResultUtils.writeOriginalObject(null == optInfo, response);
    }

    /**
     * 更新菜单
     *
     * @param optId    主键
     * @param optInfo  OptInfo
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询某个部门权限的业务",notes="查询某个部门权限的业务。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value="菜单id",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "optInfo", value="更新的菜单对象",
            required = true, paramType = "body", dataTypeClass= OptInfo.class)
    })
    @RequestMapping(value = "/{optId}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新菜单")
    public void edit(@PathVariable String optId, @Valid OptInfo optInfo,
                     HttpServletRequest request, HttpServletResponse response) {

        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        if (!StringUtils.equals(dbOptInfo.getPreOptId(), optInfo.getPreOptId())) {
            OptInfo parentOpt = optInfoManager.getOptInfoById(optInfo.getPreOptId());
            if (parentOpt == null) {
                optInfo.setPreOptId(dbOptInfo.getPreOptId());
            }
        }

        dbOptInfo.copyNotNullProperty(optInfo);
        //BeanUtils.copyProperties(optInfo, dbOptInfo, "optMethods", "dataScopes");
        optInfoManager.updateOptInfo(dbOptInfo);

        JsonResultUtils.writeSingleDataJson(dbOptInfo, response);
    }

    /**
     * 更新操作权限
     *
     * @param optId    主键
     * @param optInfo  OptInfo
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="更新操作权限",notes="更新操作权限。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value="菜单id",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "optInfo", value="更新的菜单对象",
            required = true, paramType = "body", dataTypeClass= OptInfo.class)
    })
    @RequestMapping(value = "/editpower{optId}", method = {RequestMethod.PUT})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新操作权限")
    public void editPower(@PathVariable String optId, @Valid OptInfo optInfo,
                          HttpServletRequest request, HttpServletResponse response) {

        OptInfo dbOptInfo = optInfoManager.getObjectById(optId);
        if (null == dbOptInfo) {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
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

        dbOptInfo.copyNotNullProperty(optInfo);

        dbOptInfo.addAllOptMethods(optInfo.getOptMethods());
        dbOptInfo.addAllDataScopes(optInfo.getDataScopes());
        optInfoManager.updateOperationPower(dbOptInfo);
        JsonResultUtils.writeSuccessJson(response);
    }

    /**
     * 删除菜单
     *
     * @param optId    主键
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="删除菜单",notes="删除菜单。")
    @ApiImplicitParam(
        name = "optId", value="菜单id",
        required = true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{optId}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除菜单")
    public void delete(@PathVariable String optId, HttpServletRequest request, HttpServletResponse response) {
        OptInfo dboptInfo = optInfoManager.getObjectById(optId);

        optInfoManager.deleteOptInfo(dboptInfo);

        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 查询单条数据
     *
     * @param optId    主键
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询单条数据",notes="根据菜单id查询单条数据。")
    @ApiImplicitParam(
        name = "optId", value="菜单id",
        required = true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/{optId}", method = {RequestMethod.GET})
    public void getOptInfoById(@PathVariable String optId, HttpServletResponse response) {
        OptInfo dbOptInfo = optInfoManager.getOptInfoById(optId);

        JsonResultUtils.writeSingleDataJson(dbOptInfo, response);
    }

    /**
     * 新增页面时获取OptDef主键
     *
     * @param response HttpServletResponse
     */
    @ApiOperation(value="获取菜单的下个主键",notes="获取菜单的下个主键。")
    @RequestMapping(value = "/nextOptCode", method = RequestMethod.GET)
    public void getNextOptCode(HttpServletResponse response) {
        String optCode = optMethodManager.getNextOptCode();

        ResponseMapData responseData = new ResponseMapData();
        responseData.addResponseData("optCode", optCode);

        JsonResultUtils.writeResponseDataAsJson(responseData, response);
    }

    /**
     * 新建或更新业务操作
     *
     * @param optId    主键
     * @param optCode  optCode
     * @param optDef   OptMethod
     * @param response HttpServletResponse
     */
    @ApiOperation(value="新建或更新业务操作",notes="新建或更新业务操作。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "optId", value="菜单id",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "optCode", value="菜单英文代码",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "optDef", value="更新的菜单操作方法对象",
            required = true, paramType = "body", dataTypeClass= OptMethod.class)
    })
    @RequestMapping(value = "/{optId}/{optCode}", method = {RequestMethod.POST, RequestMethod.PUT})
    public void optDefEdit(@PathVariable String optId, @PathVariable String optCode, @Valid OptMethod optDef,
                           HttpServletResponse response) {
        OptInfo optInfo = optInfoManager.getObjectById(optId);
        if (null == optInfo) {
            JsonResultUtils.writeSingleErrorDataJson(
                ResponseData.ERROR_INTERNAL_SERVER_ERROR,
                "数据库不匹配", "数据库中不存在optId为" + optId + "的业务信息。", response);
            return;
        }

        OptMethod dbOptDef = optMethodManager.getObjectById(optCode);
        if (null == dbOptDef) {
            JsonResultUtils.writeSingleErrorDataJson(
                ResponseData.ERROR_INTERNAL_SERVER_ERROR,
                "数据库不匹配", "数据库中不存在optCode为" + optCode + "的操作信息。", response);
            return;
        } else {
            dbOptDef.copy(optDef);
            optMethodManager.updateOptMethod(dbOptDef);
        }

        JsonResultUtils.writeSuccessJson(response);
    }

    /**
     * 获取所有的业务菜单
     * @param response HttpServletResponse
     */
    @ApiOperation(value="获取所有的业务菜单",notes="获取所有的业务菜单。")
    @RequestMapping(value = "/allOptInfo", method = RequestMethod.GET)
    public void loadAllOptInfo(HttpServletResponse response) {
        List<OptInfo> optInfos = optInfoManager.listObjects();
        JsonResultUtils.writeSingleDataJson(optInfos, response);
    }

    /**
     * 获取所有的操作方法
     * @param response HttpServletResponse
     */
    @ApiOperation(value="获取所有的操作方法",notes="获取所有的操作方法。")
    @RequestMapping(value = "/allOptMethod", method = RequestMethod.GET)
    public void loadAllOptMethod(HttpServletResponse response) {
        List<OptMethod> optDefs = optMethodManager.listObjects();
        JsonResultUtils.writeSingleDataJson(optDefs, response);
    }

    /**
     * 获取用户的操作方法
     * @param userCode 用户ID
     * @param response HttpServletResponse
     */
    @ApiOperation(value="查询单条数据",notes="根据菜单id查询单条数据。")
    @ApiImplicitParam(
        name = "userCode", value="用户id",
        required = true, paramType = "path", dataType= "String")
    @RequestMapping(value = "/userpoweropts/{userCode}", method = RequestMethod.GET)
    public void listUserOpts(@PathVariable String userCode, HttpServletResponse response) {
//        List<OptInfo> optInfos = (List<OptInfo>) platformEnvironment.listUserMenuOptInfos(userCode, false);
        List<OptInfo> optInfos = optInfoManager.listUserAllPower(userCode, false);
        optInfos = optInfoManager.listObjectFormatTree(optInfos, true);
        JsonResultUtils.writeSingleDataJson(makeMenuFuncsJson(optInfos), response);
    }

}
