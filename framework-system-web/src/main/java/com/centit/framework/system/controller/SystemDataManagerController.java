package com.centit.framework.system.controller;

import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.system.service.UserDirectory;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.system.service.UserSyncDirectoryManager;
import com.centit.framework.users.service.DingTalkLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/datamanager")
@Api(tags= "用户组织同步接口",value = "用户组织同步接口")
public class SystemDataManagerController  extends BaseController {

    @Autowired
    @Qualifier("activeDirectoryUserDirectory")
    private UserDirectory activeDirectoryUserDirectory;

    @Autowired
    private DingTalkLoginService dingTalkLoginService;

    @Autowired
    private UserSyncDirectoryManager userSyncDirectoryManager;

    @ApiOperation(value="用户组织同步",notes="用户组织同步")
    @WrapUpResponseBody
    @RequestMapping(value = "/syncuserdirectory",
            method = RequestMethod.POST)
    public void syncUserDirectory(@RequestParam("directory") String directory) {
        UserSyncDirectory userSyncDirectory = userSyncDirectoryManager.getObjectById(directory);
        if(userSyncDirectory != null && userSyncDirectory.getType().equalsIgnoreCase("LDAP")){
            activeDirectoryUserDirectory.synchroniseUserDirectory(userSyncDirectory);
        }else if(userSyncDirectory != null && userSyncDirectory.getType().equalsIgnoreCase("DING")){
            dingTalkLoginService.synchroniseUserDirectory(userSyncDirectory);
        }
    }
}
