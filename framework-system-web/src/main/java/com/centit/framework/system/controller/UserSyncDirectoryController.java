package com.centit.framework.system.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.system.service.UserSyncDirectoryManager;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author tian_y
 */
@Controller
@RequestMapping("/userSyncDirectory")
@Api(value = "用户同步目录接口", tags = "用户同步目录接口")
public class UserSyncDirectoryController extends BaseController {

    @Autowired
    private UserSyncDirectoryManager userSyncDirectoryManager;

    @ApiOperation(value = "新增用户同步目录信息")
    @PostMapping()
    @WrapUpResponseBody
    public void saveUserSyncDirectory(@RequestBody UserSyncDirectory userSyncDirectory, HttpServletResponse response, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        userSyncDirectoryManager.saveUserSyncDirectory(userSyncDirectory, userCode);
        JsonResultUtils.writeSingleDataJson(userSyncDirectory.getId(),response);
    }

    @ApiOperation(value = "修改用户同步目录信息")
    @PutMapping()
    @WrapUpResponseBody
    public void updateUserSyncDirectory(@RequestBody UserSyncDirectory userSyncDirectory) {
        userSyncDirectoryManager.updateUserSyncDirectory(userSyncDirectory);
    }

    @ApiOperation(value = "删除用户同步目录信息")
    @DeleteMapping(value = "/{id}")
    @WrapUpResponseBody
    public void deleteUserSyncDirectoryById(@PathVariable String id) {
        userSyncDirectoryManager.deleteUserSyncDirectoryById(id);
    }

    @ApiOperation(value = "查询版本信息列表")
    @GetMapping("/list")
    @WrapUpResponseBody
    public PageQueryResult<UserSyncDirectory> listObjects(HttpServletRequest request, PageDesc pageDesc) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        List<UserSyncDirectory> list = userSyncDirectoryManager.listObjects(BaseController.collectRequestParameters(request), pageDesc, userCode);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个版本信息")
    @GetMapping(value = "/{id}")
    @WrapUpResponseBody
    public UserSyncDirectory getObjectById(@PathVariable String id) {
        return userSyncDirectoryManager.getObjectById(id);
    }

}
