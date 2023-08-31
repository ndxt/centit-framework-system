package com.centit.framework.system.controller;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.model.security.CentitPasswordEncoder;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.system.service.OsInfoManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/osinfo")
@Api(tags= "业务系统护接口",value = "业务系统护接口")
public class OsInfoController extends BaseController {

    @Autowired
    private OsInfoManager osInfoMag;


    @Autowired
    private CentitPasswordEncoder passwordEncoder;


    private String optId = "OS";

    /**
     * 刷新单个系统数据
     * @param osInfo 系统对象
     * @param response HttpServletResponse
     */
    @ApiOperation(value="刷新单个系统数据",notes="刷新单个系统数据。")
    @ApiImplicitParam(
        name = "osInfo", value="json格式，业务系统对象信息",
        paramType = "body", dataTypeClass = OsInfo.class)
    @RequestMapping(value = "/data/refresh/single" ,method = {RequestMethod.POST})
    public void refreshSingle(@RequestBody OsInfo osInfo, HttpServletResponse response){
        if(osInfo == null){
            return;
        }
        boolean flag = osInfoMag.refreshSingle(osInfo);
        JsonResultUtils.writeSingleDataJson(flag, response);
    }

    /**
     * 刷新查询的所有系统的数据
     * @param response HttpServletResponse
     */
    @ApiOperation(value="刷新所有系统的数据",notes="刷新查询的所有系统的数据。")
    @RequestMapping(value = "/data/refresh/all" ,method = {RequestMethod.GET})
    public void refreshAll(HttpServletResponse response){
        boolean flag = osInfoMag.refreshAll();
        JsonResultUtils.writeSingleDataJson(flag, response);
    }

    /**
     * 查询所有的业务系统信息
     * @param pageDesc 分页对象
     * @param request HttpServletRequest
     * @return PageQueryResult 分页查询结果
     */
    @ApiOperation(value="查询所有的业务系统信息",notes="查询所有的业务系统信息。")
    @RequestMapping(method = RequestMethod.GET)
    @ApiImplicitParam(
        name = "pageDesc", value="json格式，分页对象信息",
        paramType = "body", dataTypeClass = PageDesc.class)
    @WrapUpResponseBody
    public PageQueryResult<Object> list(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        if (WebOptUtils.isTenantTopUnit(request)) {
            searchColumn.put("topUnit", WebOptUtils.getCurrentTopUnit(request));
        }
        JSONArray listObjects = osInfoMag.listOsInfoAsJson(searchColumn, pageDesc);
        return PageQueryResult.createJSONArrayResult(listObjects,pageDesc, OsInfo.class);
    }

    /**
     * 添加业务系统
     * @param osinfo 业务系统对象
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @ApiOperation(value="添加业务系统",notes="添加业务系统。")
    @ApiImplicitParam(
        name = "osInfo", value="json格式，业务系统对象信息",
        paramType = "body", dataTypeClass = OsInfo.class)
    @RequestMapping(method = {RequestMethod.POST})
    public void saveOsInfo(@Valid OsInfo osinfo,HttpServletRequest request, HttpServletResponse response) {
        if (osinfo == null) {
            JsonResultUtils.writeErrorMessageJson("对象不能为空", response);
            return;
        }
        if(osInfoMag.getObjectById(osinfo.getOsId())!=null){
            JsonResultUtils.writeErrorMessageJson("业务系统ID已存在", response);
            return;
        }
        osinfo.setCreated(WebOptUtils.getCurrentUserCode(request));
        osinfo.setCreateTime(new Date());
        if(osinfo.getOauthPassword()!=null && ! passwordEncoder.isCorrectPasswordFormat(osinfo.getOauthPassword())){
            osinfo.setOauthPassword(passwordEncoder.createPassword(
                osinfo.getOauthPassword(), osinfo.getOsId()));
        }
        osInfoMag.saveNewObject(osinfo);

        JsonResultUtils.writeBlankJson(response);

        /**********************log***********************/
        OperationLogCenter.logNewObject(request, optId, osinfo.getOsId(), OperationLog.P_OPT_LOG_METHOD_C,
                "新增业务系统", osinfo);
        /**********************log***********************/
    }

    /**
     * 更新业务系统信息
     * @param osId 业务系统id
     * @param osinfo 业务系统对象
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{osId}", method = {RequestMethod.PUT})
    @ApiOperation(value="更新业务系统信息",notes="更新业务系统信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "osId", value="业务系统id",
            required = true, paramType = "path", dataType= "String"),
        @ApiImplicitParam(
            name = "osinfo", value="json格式，业务系统对象信息", required = true,
            paramType = "body", dataTypeClass = OsInfo.class)
    })
    public void updateOsInfo(@PathVariable String osId, @Valid OsInfo osinfo,
                             HttpServletRequest request, HttpServletResponse response) {

        OsInfo dbOsInfo = osInfoMag.getObjectById(osId);
        OsInfo oldValue = new OsInfo();
        BeanUtils.copyProperties(dbOsInfo, oldValue);
        osinfo.setLastModifyDate(new Date());
        if(! passwordEncoder.isCorrectPasswordFormat(osinfo.getOauthPassword())){
            osinfo.setOauthPassword(passwordEncoder.createPassword(
                osinfo.getOauthPassword(), osinfo.getOsId()));
        }
        osInfoMag.mergeObject(osinfo);
        JsonResultUtils.writeBlankJson(response);
        /**********************log*********************/
        OperationLogCenter.logUpdateObject(request, optId, osId, OperationLog.P_OPT_LOG_METHOD_U,
                "更新业务系统信息", osinfo, oldValue);
        /**********************log*********************/
    }

    /**
     * 根据ID查询单个业务系统信息
     * @param osId 业务系统ID
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{osId}", method = {RequestMethod.GET})
    @ApiOperation(value="根据ID查询单个业务系统信息",notes="根据ID查询单个业务系统信息。")
    @ApiImplicitParam(
        name = "osId", value="业务系统id",
        required = true, paramType = "path", dataType= "String")
    public void getOsInhfo(@PathVariable String osId, HttpServletResponse response) {
        OsInfo osInfo = osInfoMag.getObjectById(osId);

        JsonResultUtils.writeSingleDataJson(osInfo, response,
                JsonPropertyUtils.getExcludePropPreFilter(OsInfo.class, "osInfo"));
    }

    /**
     * 根据ID删除单个业务系统信息
     * @param osId 业务系统ID
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     */
    @RequestMapping(value = "/{osId}", method = {RequestMethod.DELETE})
    @ApiOperation(value="根据ID删除单个业务系统信息",notes="根据ID删除单个业务系统信息。")
    @ApiImplicitParam(
        name = "osId", value="业务系统id",
        required = true, paramType = "path", dataType= "String")
    public void deleteOsInfo(@PathVariable String osId,
                             HttpServletRequest request, HttpServletResponse response) {

        osInfoMag.deleteObjectById(osId);
        JsonResultUtils.writeBlankJson(response);
        /********************log***********************/
        OsInfo dbOsInfo = osInfoMag.getObjectById(osId);
        OperationLogCenter.logDeleteObject(request, optId, osId, OperationLog.P_OPT_LOG_METHOD_D,
                "删除业务系统", dbOsInfo);
        /********************log***********************/
    }
}
