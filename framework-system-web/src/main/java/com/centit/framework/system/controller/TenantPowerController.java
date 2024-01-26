package com.centit.framework.system.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.system.service.TenantPowerManage;
import com.centit.support.common.ObjectException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tenantPower")
@Api(
    tags = {"租户权限范围接口"},
    value = "租户权限范围接口"
)
public class TenantPowerController extends BaseController {

    @Autowired
    private TenantPowerManage tenantPowerManage;

    @ApiOperation(
        value = "判断当前用户是否为租户所有者",
        notes = "判断当前用户是否为租户所有者 topUnit:租户id"
    )
    @RequestMapping(value = "/userIsTenantOwner", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userIsTenantOwner(@RequestParam("topUnit") String topUnit, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userIsTenantOwner(userCode, topUnit));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("判断当前用户是否为租户所有者失败,错误原因{},租户id：{}", e, topUnit);
            return ResponseData.makeErrorMessage("判断当前用户是否为租户所有者失败!");
        }
    }

    @ApiOperation(
        value = "判断当前用户是否为租户管理员",
        notes = "判断当前用户是否为租户管理员 topUnit:租户id"
    )
    @RequestMapping(value = "/userIsTenantAdmin", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userIsTenantAdmin(@RequestParam("topUnit")String topUnit, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userIsTenantAdmin(userCode, topUnit));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("判断当前用户是否为租户管理员失败,错误原因{},租户id：{}", e, topUnit);
            return ResponseData.makeErrorMessage("判断当前用户是否为租户管理员失败!");
        }
    }


    @ApiOperation(
        value = "当前用户是否在租户中的角色",
        notes = "所有者：OWN 管理员：ZHGLY，普通成员：ZHZY 不在租户内：\"\" 参数：topUnit:租户id"
    )
    @RequestMapping(value = "/userTenantRole", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userTenantRole(@RequestParam("topUnit")String topUnit, HttpServletRequest request) {

        if (StringUtils.isBlank(topUnit)){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "输入的参数topUnit为空！");
        }
        String userCode = WebOptUtils.getCurrentUserCode(request);
        if(StringUtils.isBlank(userCode)){
            throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "获取当前用户失败，请确认用户是否登录！");
        }
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userTenantRole(userCode, topUnit));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("当前用户是否在租户中的角色,错误原因{},租户id：{}", e, topUnit);
            return ResponseData.makeErrorMessage("当前用户不在租户中的角色!");
        }
    }

    @ApiOperation(
        value = "判断当前用户是否为租户成员",
        notes = "判断当前用户是否为租户成员 topUnit:租户id"
    )
    @RequestMapping(value = "/userIsTenantMember", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userIsTenantMember(@RequestParam("topUnit")String topUnit, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userIsTenantMember(userCode, topUnit));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("判断当前用户是否为租户成员失败,错误原因{},租户id：{}", e, topUnit);
            return ResponseData.makeErrorMessage("判断当前用户是否为租户成员失败!");
        }
    }

    @ApiOperation(
        value = "判断当前用户是否为应用管理员",
        notes = "判断当前用户是否为应用管理员 osId:应用id"
    )
    @RequestMapping(value = "/userIsApplicationAdmin", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userIsApplicationAdmin(@RequestParam("osId")String osId, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userIsApplicationAdmin(userCode, osId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("判断当前用户是否为应用管理员失败,错误原因{},应用id：{}", e, osId);
            return ResponseData.makeErrorMessage("判断当前用户是否为应用管理员失败!");
        }
    }

    @ApiOperation(
        value = "判断当前用户是否为应用成员",
        notes = "判断当前用户是否为应用成员 osId:应用id"
    )
    @RequestMapping(value = "/userIsApplicationMember", method = RequestMethod.GET)
    @WrapUpResponseBody
    public ResponseData userIsApplicationMember(@RequestParam("osId")String osId, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        try {
            return ResponseData.makeResponseData(tenantPowerManage.userIsApplicationMember(userCode, osId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("判断当前用户是否为应用成员失败,错误原因{},应用id：{}", e, osId);
            return ResponseData.makeErrorMessage("判断当前用户是否为应用成员失败!");
        }
    }

}
