package com.centit.framework.system.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.UserPlat;
import com.centit.framework.operationlog.RecordOperationLog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zfg
 */
@Controller
@RequestMapping("/userplat")
@Api(value = "用户平台管理操作接口", tags = "用户平台管理操作接口")
public class UserPlatController extends BaseController {

    @Autowired
    private UserPlatService userPlatService;

    /*
     * 创建用户平台关联信息
     *
     * @param userPlat UserPlat
     * @param request  {@link HttpServletRequest}
     */
    @ApiOperation(value = "创建用户平台关联信息", notes = "创建用户平台关联信息。")
    @ApiImplicitParam(
        name = "userPlat", value = "json格式的用户平台关联对象信息", required = true,
        paramType = "body", dataTypeClass = UserPlat.class)
    @PostMapping
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}新增用户平台关联信息",
        tag = "{userPlat.userPlatId}:{userPlat.platId}:{userPlat.userCode}")
    @WrapUpResponseBody
    public ResponseData create(@ParamName("userPlat") @Valid UserPlat userPlat, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("platId", userPlat.getPlatId());
        map.put("corpId", userPlat.getCorpId());
        map.put("userCode", userPlat.getUserCode());
        List<UserPlat> list = userPlatService.listObjects(map, new PageDesc());
        if (!CollectionUtils.isEmpty(list)) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_DUPLICATE_OPERATION,
                getI18nMessage("error.801.duplicate_operation", request));
        }
        userPlat.setCreator(WebOptUtils.getCurrentUserCode(request));
        userPlatService.saveUserPlat(userPlat);
        return ResponseData.successResponse;
    }

    /*
     * 更新平台用户信息
     *
     * @param userPlatId userPlatId
     * @param userPlat   UserPlat
     * @param request    {@link HttpServletRequest}
     */
    @ApiOperation(value = "更新平台用户信息", notes = "根据用户平台代码更新平台用户信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userPlatId", value = "用户平台代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "userPlat", value = "json格式的用户平台关联对象信息", required = true,
            paramType = "body", dataTypeClass = UserPlat.class)
    })
    @PutMapping(value = "/{userPlatId}")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新用户平台关联信息",
        tag = "userPlatId")
    @WrapUpResponseBody
    public ResponseData edit(@ParamName("userPlatId") @PathVariable String userPlatId,
                             @Valid UserPlat userPlat, HttpServletRequest request) {
        userPlat.setUpdator(WebOptUtils.getCurrentUserCode(request));
        UserPlat dbUserPlat = userPlatService.getObjectById(userPlatId);
        if (null == dbUserPlat) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
               getI18nMessage("error.604.user_not_found", request, userPlat.getUserCode()));
        }
        userPlatService.updateUserPlat(userPlat);
        return ResponseData.makeResponseData(userPlat);
    }

    /*
     * 删除用户平台关联信息
     * @param userPlatId userPlatId
     */
    @ApiOperation(value = "删除用户平台关联信息", notes = "根据用户平台代码删除用户平台关联信息。")
    @ApiImplicitParam(
        name = "userPlatId", value = "用户平台代码",
        required = true, paramType = "path", dataType = "String")
    @DeleteMapping(value = "/{userPlatId}")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}删除用户平台关联信息",
        tag = "{userPlatId}")
    @WrapUpResponseBody
    public ResponseData delete(@ParamName("userPlatId") @PathVariable String userPlatId) {
        UserPlat dbUserPlat = userPlatService.getObjectById(userPlatId);
        userPlatService.deleteObject(dbUserPlat);
        return ResponseData.successResponse;
    }

    @ApiOperation(value = "通过用户代码获取用户所在平台", notes = "通过用户代码获取用户所在平台。")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "userCode", value = "用户代码",
            required = true, paramType = "path", dataType = "String"),
        @ApiImplicitParam(
            name = "pageDesc", value = "json格式的分页对象信息",
            paramType = "body", dataTypeClass = PageDesc.class)
    })
    @GetMapping(value = "/userplats/{userCode}")
    @WrapUpResponseBody
    public PageQueryResult<UserPlat> listUnitsByUser(@PathVariable String userCode, PageDesc pageDesc,
                                                     HttpServletRequest request) {
        Map<String, Object> filterMap = BaseController.collectRequestParameters(request);
        filterMap.put("userCode", userCode);
        List<UserPlat> listObjects = userPlatService.listObjects(filterMap, pageDesc);
        return PageQueryResult.createResultMapDict(listObjects, pageDesc);
    }

}
