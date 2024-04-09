package com.centit.framework.system.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpContentType;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.Platform;
import com.centit.framework.model.basedata.UserPlat;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.service.PlatformService;
import com.centit.framework.system.service.UserPlatService;
import com.centit.support.common.ObjectException;
import com.centit.support.common.ParamName;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author zfg
 */
@Controller
@RequestMapping("/plat")
@Api(value = "登录平台管理操作接口", tags = "登录平台管理操作接口")
public class PlaformController extends BaseController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private UserPlatService userPlatService;

    @Autowired
    protected PlatformEnvironment platformEnvironment;

    @ApiOperation(value = "分页查询平台信息", notes = "分页查询平台信息。")
    @ApiImplicitParam(
        name = "pageDesc", value = "json格式的分页信息",
        paramType = "body", dataTypeClass = PageDesc.class
    )
    @GetMapping(value = "/list")
    @WrapUpResponseBody
    public PageQueryResult<Platform> list(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        List<Platform> listObjects = platformService.listObjects(searchColumn, pageDesc);
        return PageQueryResult.createResultMapDict(listObjects, pageDesc);
    }

    /*
     * 查询单个平台信息
     *
     * @param platId 平台id
     */
    @ApiOperation(value = "查询单个平台信息", notes = "根据平台D查询单个平台信息。")
    @ApiImplicitParam(
        name = "platId", value = "平台ID",
        paramType = "query", dataType = "String")
    @GetMapping(value = "/{platId}")
    @WrapUpResponseBody(contentType = WrapUpContentType.MAP_DICT)
    public Platform getPlatform(@PathVariable String platId) {
        return platformService.getObjectById(platId);
    }

    /*
     * 删除平台
     *
     * @param platId platId
     */
    @ApiOperation(value = "删除平台信息", notes = "根据平台ID删除平台信息。")
    @ApiImplicitParam(
        name = "platId", value = "平台ID",
        paramType = "query", dataType = "String")
    @DeleteMapping(value = "/{platId}")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除平台",
        tag = "{platId}")
    @WrapUpResponseBody
    public ResponseData delete(@ParamName("platId") @PathVariable String platId, HttpServletRequest request) {
        Platform platform = platformService.getObjectById(platId);
        if (platform == null) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                getI18nMessage("error.604.object_not_found", request,
                    "Platform", platId));//"当前对象不存在");
        }

        List<UserPlat> userPlats = userPlatService.listPlatUsersByPlatId(platId);
        if (!CollectionUtils.isEmpty(userPlats)) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_INTEGRATED,
                getI18nMessage("error.610.cannot_delete_parent", request, "PlatUser-"+platId));
            //"该平台存在关联用户，不能删除！");
        }
        platformService.deletePlatform(platform);
        return ResponseData.successResponse;
    }

    /*
     * 新建平台
     * @param platform pf
     */
    @ApiOperation(value = "新建平台", notes = "新建一个平台。")
    @ApiImplicitParam(
        name = "platform", value = "json格式，平台信息对象",
        paramType = "body", dataTypeClass = Platform.class)
    @PostMapping
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增平台",
        tag = "{pf.platId}:{pf.platSourceCode}")
    @WrapUpResponseBody
    public ResponseData create(@ParamName("pf") @Valid Platform pf, HttpServletRequest request) {

        if (platformService.hasSamePlat(pf)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                getI18nMessage("error.702.duplicate_primary_key", request,"Platform", pf.getPlatSourceCode()));
                //"平台代码" + platform.getPlatSourceCode() + "已存在，请更换！");
        }
        platformService.savePlatform(pf);
        return ResponseData.makeResponseData(pf);
    }

    /*
     * 更新平台信息
     * @param platId 平台id
     * @param platform platform
     */
    @ApiOperation(value = "更新平台信息", notes = "更新平台信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "platId", value = "平台id",
            paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "platform", value = "json格式，平台信息对象",
            paramType = "body", dataTypeClass = Platform.class)
    })
    @PutMapping(value = "/{platId}")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新平台",
        tag = "{platId}")
    @WrapUpResponseBody
    public ResponseData edit(@ParamName("platId") @PathVariable String platId,
                             @Valid Platform platform, HttpServletRequest request) {

        Platform dbPlatform = platformService.getObjectById(platId);
        if (null == dbPlatform) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                getI18nMessage("error.604.object_not_found", request,
                    "Platform", platId));//"当前对象不存在");
        }
        if (!dbPlatform.getPlatSourceCode().equals(platform.getPlatSourceCode()) &&
            platformService.hasSamePlat(platform)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                getI18nMessage("error.702.duplicate_primary_key", request,"Platform", platform.getPlatSourceCode()));
                //"平台代码" + platform.getPlatSourceCode() + "已存在，请更换！");
        }
        platformService.updatePlatform(platform);
        return ResponseData.makeResponseData(platform);
    }

}
