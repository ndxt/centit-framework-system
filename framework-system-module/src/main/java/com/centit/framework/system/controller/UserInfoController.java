package com.centit.framework.system.controller;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserRole;
import com.centit.framework.system.po.UserUnit;
import com.centit.framework.system.service.SysUserManager;
import com.centit.framework.system.service.SysUserUnitManager;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.json.JsonPropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userinfo")
public class UserInfoController extends BaseController {
    @Resource
    @NotNull
    private SysUserManager sysUserManager;

    @Resource
    @NotNull
    private SysUserUnitManager sysUserUnitManager;

    @Resource
    @NotNull
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     */
    //private String optId = "USERMAG";//CodeRepositoryUtil.getCode("OPTID", "userInfo");
    public String getOptId() {
        return  "USERMAG";
    }
    /**
     * 查询所有用户信息
     *
     * @param field    显示结果中只需要显示的字段
     * @param pageDesc PageDesc
     * @param _search _search
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, String _search,
                     HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<UserInfo> listObjects = null;
        if (Boolean.parseBoolean(_search)) {
            listObjects = sysUserManager.listObjects(searchColumn);
            pageDesc = null;
        } else {
            listObjects = sysUserManager.listObjects(searchColumn, pageDesc);
        }

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(UserInfo.class, field);
        }
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response, simplePropertyPreFilter);
            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response, simplePropertyPreFilter);
    }

    /**
     * 新增用户
     *
     * @param userInfo UserInfo
     * @param userUnit  用户机构
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    @RecordOperationLog(content = "新增用户")
    public void create(@Valid UserInfo userInfo, UserUnit userUnit,
                       HttpServletRequest request, HttpServletResponse response) {

        UserInfo dbuserinfo=sysUserManager.loadUserByLoginname(userInfo.getLoginName());
        if(null!=dbuserinfo) {
             JsonResultUtils.writeErrorMessageJson(
                     ResponseData.ERROR_FIELD_INPUT_CONFLICT,
                     "登录名"+userInfo.getLoginName()+"已存在，请更换！", response);
             return;
        }
        if(null!=userInfo.getUserUnits()){
            for(UserUnit uu:userInfo.getUserUnits()){
                uu.setUserCode(userInfo.getUserCode());
            }
        }
        if(null!=userInfo.listUserRoles()){
            for(UserRole ur:userInfo.listUserRoles()){
                ur.setUserCode(userInfo.getUserCode());
            }
        }
        userUnit.setCreator(getLoginUserCode(request));
        sysUserManager.saveNewUserInfo(userInfo,userUnit);
        JsonResultUtils.writeSingleDataJson(userInfo, response);


        /*********log*********/
//        OperationLogCenter.logNewObject(request,optId,userInfo.getUserCode(),
//                OperationLog.P_OPT_LOG_METHOD_C,  "新增用户", userInfo);
    }

    /**
     * 更新用户信息
     * @param userCode userCode
     * @param userInfo userInfo
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "更新用户信息")
    public void edit(@PathVariable String userCode, @Valid UserInfo userInfo, UserUnit userUnit,
                     HttpServletRequest request, HttpServletResponse response) {

        UserInfo dbUserInfo = sysUserManager.getObjectById(userCode);
        if (null == dbUserInfo) {
            JsonResultUtils.writeErrorMessageJson("当前用户不存在", response);
            return;
        }
        //删除旧主机构
//        if(!dbUserInfo.getPrimaryUnit().equals(userInfo.getPrimaryUnit())){
            sysUserUnitManager.deletePrimaryUnitByUserCode(userCode);
            userUnit.setUserCode(userInfo.getUserCode());
            userUnit.setUnitCode(userInfo.getPrimaryUnit());
            userUnit.setIsPrimary("T");
            userUnit.setCreator(getLoginUserCode(request));
            sysUserUnitManager.saveNewUserUnit(userUnit);

//        }
        UserInfo oldValue= new UserInfo();
        oldValue.copy(dbUserInfo);
        if(oldValue.getUserUnits().size() == 0){
            oldValue.setUserUnits(null);
        }
        if(oldValue.listUserRoles().size() == 0){
            oldValue.setUserRoles(null);
        }

        if (StringUtils.isBlank(userInfo.getUserPin())){
            userInfo.setUserPin(dbUserInfo.getUserPin());
        }

        sysUserManager.updateUserInfo(userInfo);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
       //OperationLogCenter.logUpdateObject(request,optId, userCode, OperationLog.P_OPT_LOG_METHOD_U,
       //       "更新用户信息", userInfo, oldValue);
        /*********log*********/
    }

    /**
     * 当前登录用户信息
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public void getCurrentUserInfo(HttpServletRequest request, HttpServletResponse response) {
        CentitUserDetails userDetails = super.getLoginUser(request);
        UserInfo userinfo = sysUserManager.getObjectById(userDetails.getUserInfo().getUserCode());
        JsonResultUtils.writeSingleDataJson(userinfo, response);
    }

    /**
     * 获取单个用户信息
     *
     * @param userCode 用户代码
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{userCode}", method = RequestMethod.GET)
    public void getUserInfo(@PathVariable String userCode, HttpServletResponse response) {
        UserInfo userInfo = sysUserManager.getObjectById(userCode);
        UserUnit userUnit = sysUserUnitManager.getPrimaryUnitByUserCode(userCode);
        ResponseMapData responseData = new ResponseMapData();
        responseData.addResponseData("userInfo", userInfo);
        responseData.addResponseData("userUnit", userUnit);

        Map<Class<?>, String[]> excludes  =new HashMap<>();
        excludes.put(UserUnit.class,new String[]{"userInfo"});
        excludes.put(UserRole.class,new String[]{"userInfo"});
        JsonResultUtils.writeResponseDataAsJson(responseData,response, JsonPropertyUtils.getExcludePropPreFilter(excludes));
    }


    /**
     * 当前登录名是否已存在
     * @param request  HttpServletRequest
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public void isAnyExist(HttpServletRequest request,HttpServletResponse response){
        String userCode = request.getParameter("userCode");
        String loginName = request.getParameter("loginName");
        String regPhone = request.getParameter("regPhone");
        String regEmail = request.getParameter("regEmail");

        JsonResultUtils.writeOriginalObject(
                sysUserManager.isAnyOneExist(
                        userCode,  loginName, regPhone, regEmail), response);
    }

     /**
     * 当前登录名是否已存在
     *
     * @param loginName 登录名
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @RequestMapping(value = "/exists/{loginName}", method = RequestMethod.GET)
    public void isExists(@PathVariable String loginName,
            HttpServletRequest request,HttpServletResponse response) throws IOException {
        UserInfo userInfo = sysUserManager.loadUserByLoginname(loginName);

        JsonResultUtils.writeOriginalObject(null != userInfo, response);
    }

    /**
     * 更新用户密码
     *
     * @param userCode    用户代码
     * @param password    旧密码
     * @param newPassword 新密码
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/change/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "更新用户密码")
    public void changePwd(@PathVariable String userCode, String password, String newPassword,
            HttpServletRequest request,HttpServletResponse response) {

        sysUserManager.setNewPassword(userCode, password, newPassword);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
        //OperationLogCenter.log(request,optId,userCode, "changePassword", "更新用户密码,用户代码:" + userCode);
        /*********log*********/
    }

    /**
     * 强制更新用户密码
     * @param userCode 用户代码
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/changePwd/{userCode}", method = RequestMethod.PUT)
    @RecordOperationLog(content = "强制更新用户密码")
    public void forceChangePwd(@PathVariable String userCode,
                               HttpServletRequest request,HttpServletResponse response) {
        String newPassword = request.getParameter("newPassword");
        if(StringUtils.isBlank(newPassword)) {
          sysUserManager.resetPwd(userCode);
        }else {
          sysUserManager.forceSetPassword(userCode, newPassword);
        }

        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
        //OperationLogCenter.log(request,optId,userCode, "forceChangePwd", "更新用户密码,用户代码:" + userCode);
        /*********log*********/
    }

    @RequestMapping(value = "/canchange/{userCode}/{oldPassword}", method = RequestMethod.GET)
    public void canChangePwd(@PathVariable String userCode,@PathVariable String oldPassword,
                             HttpServletRequest request,HttpServletResponse response) {
        boolean bo=true;
        bo=sysUserManager.checkUserPassword(userCode,oldPassword);

        JsonResultUtils.writeSingleDataJson(bo,response);
    }

    /**
     * 批量重置密码
     * @param userCodes 用户代码集合
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @RecordOperationLog(content = "重置用户密码")
    public void resetBatchPwd(String[] userCodes, HttpServletResponse response) {
        if (ArrayUtils.isEmpty(userCodes)) {
            JsonResultUtils.writeErrorMessageJson("用户代码集合为空", response);
            return;
        }
        sysUserManager.resetPwd(userCodes);

        JsonResultUtils.writeBlankJson(response);

        /*********log*********/
        //OperationLogCenter.logNewObject(request,optId,null, "resetPassword",  "批量重置密码",userCodes);
    }

    /**
     * 删除用户
     * @param userCodes 用户代码
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value="/{userCodes}",method=RequestMethod.DELETE)
    @RecordOperationLog(content = "删除用户")
    public  void deleteUser(@PathVariable String[] userCodes,HttpServletRequest request,HttpServletResponse response){
        for(String userCode : userCodes) {
            UserInfo userInfo = sysUserManager.getObjectById(userCode);
            if (null != userInfo) {

                sysUserManager.deleteUserInfo(userCode);

            } else {
                JsonResultUtils.writeErrorMessageJson("该用户不存在", response);
                return;
            }

            /*********log*********/
//            OperationLogCenter.logDeleteObject(request, optId, userCode, OperationLog.P_OPT_LOG_METHOD_D,
//                    "删除用户"+userInfo.getUserName(), userInfo);
            /*********log*********/
        }
        JsonResultUtils.writeSuccessJson(response);
    }

}
