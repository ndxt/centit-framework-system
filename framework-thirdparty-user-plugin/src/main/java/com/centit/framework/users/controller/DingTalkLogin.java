package com.centit.framework.users.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.basedata.UserSyncDirectory;
import com.centit.framework.system.dao.UnitInfoDao;
import com.centit.framework.system.service.UserSyncDirectoryManager;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.config.UrlConstant;
import com.centit.framework.users.dto.DingUnitDTO;
import com.centit.framework.users.dto.DingUserDTO;
import com.centit.framework.users.po.SocialDeptAuth;
import com.centit.framework.users.service.DingTalkLoginService;
import com.centit.framework.users.service.SocialDeptAuthService;
import com.centit.framework.users.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zfg
 */
@Controller
@RequestMapping("/ddlogin")
@Api(value = "钉钉平台登录相关接口", tags = "钉钉平台登录相关接口")
public class DingTalkLogin extends BaseController {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DingTalkLoginService dingTalkLoginService;

    @Autowired
    private UserSyncDirectoryManager userSyncDirectoryManager;

    @Autowired
    private SocialDeptAuthService socialDeptAuthService;

    @Autowired
    private UnitInfoDao unitInfoDao;

    @ApiOperation(value = "钉钉二维码登录", notes = "钉钉二维码登录。")
    @GetMapping(value = "/qrconnect")
    public void qrConnect(HttpServletResponse response) throws IOException {
        String authorizeUrl = UrlConstant.URL_GET_QRCONNECT + "?appid=" + appConfig.getAppKey() + "&response_type=code" +
            "&scope=snsapi_login&redirect_uri=" + appConfig.getRedirectUri();
        response.sendRedirect(authorizeUrl);
    }

    @ApiOperation(value = "钉钉账号登录", notes = "钉钉账号登录。")
    @GetMapping(value = "/snsauthorize")
    public void snsAuthorize(HttpServletResponse response) throws IOException {
        String authorizeUrl = UrlConstant.URL_GET_SNSCONNECT + "?appid=" + appConfig.getAppKey() + "&response_type=code" +
            "&scope=snsapi_login&redirect_uri=" + appConfig.getRedirectUri();
        response.sendRedirect(authorizeUrl);
    }

    private String getAccessToken() {
        String accessToken = "";
        ResponseData accessTokenData = tokenService.getAccessToken();
        if (accessTokenData.getCode() != 0) {
            return "";
        }
        accessToken = accessTokenData.getData().toString();
        return accessToken;
    }

    @ApiOperation(value = "同步钉钉创建用户", notes = "同步钉钉创建用户。")
    @PostMapping(value = "/usercreate")
    @WrapUpResponseBody
    public ResponseData userCreate(DingUserDTO userInfo, HttpServletRequest request) {
        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return ResponseData.makeErrorMessage("获取钉钉access_token失败");
        }
        String unitCode = userInfo.getPrimaryUnit();
        SocialDeptAuth socialDeptAuth = socialDeptAuthService.getObjectById(unitCode);
        if (null == socialDeptAuth) {
            UnitInfo unitInfo = unitInfoDao.getObjectById(unitCode);
            DingUnitDTO dingUnitDTO = new DingUnitDTO();
            dingUnitDTO.setUnitCode(unitCode);
            dingUnitDTO.setParentUnit(unitInfo.getParentUnit());
            dingUnitDTO.setUnitName(unitInfo.getUnitName());
            dingTalkLoginService.unitCreate(accessToken, dingUnitDTO);
        }
        return dingTalkLoginService.userCreate(accessToken, userInfo);
    }

    @ApiOperation(value = "同步钉钉创建机构部门", notes = "同步钉钉创建机构部门。")
    @PostMapping(value = "/unitcreate")
    @WrapUpResponseBody
    public ResponseData unitCreate(DingUnitDTO unitInfo, HttpServletRequest request) {
        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return ResponseData.makeErrorMessage("获取钉钉access_token失败");
        }
        return dingTalkLoginService.unitCreate(accessToken, unitInfo);
    }

    @ApiOperation(value = "根据部门deptId获取钉钉部门详情", notes = "根据部门deptId获取钉钉部门详情。")
    @GetMapping(value = "/{deptId}")
    @WrapUpResponseBody
    public ResponseData getUnitInfo(@PathVariable String deptId, HttpServletResponse response) {
        String accessToken = getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            return ResponseData.makeErrorMessage("获取钉钉access_token失败");
        }
        return dingTalkLoginService.getUnitInfo(accessToken, deptId);
    }

    @ApiOperation(value="用户组织同步",notes="用户组织同步")
    @WrapUpResponseBody
    @RequestMapping(value = "/syncuserdirectory",
        method = RequestMethod.POST)
    public void syncUserDirectory(@RequestParam("directory") String directory) {
        UserSyncDirectory userSyncDirectory = userSyncDirectoryManager.getObjectById(directory);
        if(userSyncDirectory != null && userSyncDirectory.getType().equalsIgnoreCase("DING")){
            dingTalkLoginService.synchroniseUserDirectory(userSyncDirectory);
        }
    }
}
