package com.centit.framework.system.controller;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.UserInfo;
import com.centit.framework.system.po.UserSetting;
import com.centit.framework.system.po.UserSettingId;
import com.centit.framework.system.service.UserSettingManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户设置
 *
 * @author sx
 * 2014年10月14日
 */
@Controller
@RequestMapping("/usersetting")
public class UserSettingController extends BaseController {
    @Resource
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     * @return 业务标识ID
     */
    public String getOptId() {
        return  "userSetting";
    }

    /**
     * 查询当前用户所有的用户参数设置信息
     *
     * @param pageDesc pageDesc
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        UserInfo userInfo = (UserInfo) getLoginUser(request).getUserInfo();
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userInfo.getUserCode());

        List<UserSetting> listObjects = userSettingManager.listObjects(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查询用户个人设置列表
     * @param userCode 用户代码
     * @param pageDesc 分页信息
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @GetMapping(value = "/list/{userCode}")
    public void listUserSetting(@PathVariable String userCode, PageDesc pageDesc,
                                HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userCode);

        List<UserSetting> listObjects = userSettingManager.listObjects(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查询用户个人默认设置列表
     * @param pageDesc 分页信息
     * @param request {@link HttpServletRequest}
     * @return  ResponseData ajax 请求
     */
    @GetMapping(value = "/listdefault")
    @ResponseBody
    public ResponseData listUserDefaultSetting(PageDesc pageDesc, HttpServletRequest request) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        List<UserSetting> listObjects = userSettingManager.listDefaultSettings(searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        return resData;
    }

    /**
     * 查询当前用户所有个人设置 不分页
     * @param field 需要返回的字段
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    @RequestMapping(value = "/listall", method = RequestMethod.GET)
    public void listAll(String[] field, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = new HashMap<>();
        UserInfo userInfo = (UserInfo) getLoginUser(request);
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userInfo.getUserCode());

        List<UserSetting> listObjects = userSettingManager.listObjects(searchColumn);

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (ArrayUtils.isNotEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(UserSetting.class, field);
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        JsonResultUtils.writeResponseDataAsJson(resData, response, simplePropertyPreFilter);
    }

    /**
     * 获取当前用户设置的参数
     *
     * @param paramCode paramCode
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{paramCode}", method = RequestMethod.GET)
    public void getUserSetting(@PathVariable String paramCode, HttpServletRequest request, HttpServletResponse response) {
        UserSettingId id = new UserSettingId(super.getLoginUserCode(request), paramCode);

        UserSetting userSetting = userSettingManager.getObjectById(id);
        if (null != userSetting) {
            userSetting.setParamValue(HtmlUtils.htmlUnescape(userSetting.getParamValue()));
        }

        JsonResultUtils.writeSingleDataJson(userSetting, response);
    }

    /**
     * 更新用户设置参数
     *
     * @param userSetting   UserSetting
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(method = {RequestMethod.POST})
    @RecordOperationLog(content = "更新用户设置参数")
    public void editUserSetting(@Valid UserSetting userSetting, HttpServletResponse response) {

        boolean isDefaultValue = userSetting.isDefaultValue();
        if(isDefaultValue){
//            userSetting.setUserCode(WebOptUtils.getLoginUser().getUserCode());
            userSettingManager.saveNewUserSetting(userSetting);
        }else {
            userSettingManager.updateUserSetting(userSetting);
        }
        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 更新用户默认设置参数
     *
     * @param userSetting UserSetting
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "updatedefault", method = {RequestMethod.POST})
    @RecordOperationLog(content = "更新当前用户设置参数")
    public void editDefaultSetting(@Valid UserSetting userSetting, HttpServletResponse response) {

        UserSetting dbSetting = userSettingManager.getUserSetting(userSetting.getUserCode(), userSetting.getParamCode());
        if(dbSetting == null){
            userSetting.setUserCode("default");
            userSettingManager.saveNewUserSetting(userSetting);
        }else {
            userSettingManager.updateUserSetting(userSetting);
        }
        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 删除当前用户设置参数
     *
     * @param paramCode paramCode
     * @param request  {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "删除用户设置参数")
    public void deleteUserSetting(@PathVariable String paramCode,
                                  HttpServletRequest request, HttpServletResponse response) {
        UserSetting dbUserSetting=userSettingManager.getObjectById(
            new UserSettingId(super.getLoginUserCode(request), paramCode));
        userSettingManager.deleteObject(dbUserSetting);
        JsonResultUtils.writeBlankJson(response);
        /*********log*********/
//        OperationLogCenter.logDeleteObject(request,optId,dbUserSetting.getUserCode(),
//                OperationLog.P_OPT_LOG_METHOD_D,  "已删除",dbUserSetting);
        /*********log*********/
    }

    /**
     * 删除用户设置参数
     * @param userCode 用户代码
     * @param paramCode 设置编码
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value="/{userCode}/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "删除用户设置参数")
    public void delete(@PathVariable String userCode, @PathVariable String paramCode,
                       HttpServletResponse response) {
        UserSetting userSetting = userSettingManager.getUserSetting(userCode, paramCode);
        if(userSetting != null){
            if("default".equals(userSetting.getUserCode())){
                JsonResultUtils.writeErrorMessageJson("默认设置不能删除！", response);
                return;
            }
            userSettingManager.deleteObject(userSetting);
        }
        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 删除用户默认设置
     * @param paramCode 设置编码
     * @param response  {@link HttpServletResponse}
     */
    @RequestMapping(value="/deletedefault/{paramCode}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "删除用户设置参数")
    public void deleteDefault(@PathVariable String paramCode, HttpServletResponse response) {

        UserSetting userSetting = userSettingManager.getUserSetting("default", paramCode);
        if(userSetting != null){
            userSettingManager.deleteObject(userSetting);
        }else{
            JsonResultUtils.writeErrorMessageJson("值已为null！", response);
        }
        JsonResultUtils.writeBlankJson(response);
    }


    /*
     * 导出当前用户下的所有参数设置
     *
     * @param request
     * @param response
     */
    /*@RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = (UserInfo) getLoginUser(request);
        Map<String, Object> searchColumn = new HashMap<>();
        searchColumn.put(CodeRepositoryUtil.USER_CODE, userInfo.getUserCode());

        List<UserSetting> listObjectsAll = userSettingManager.listObjectsAll(searchColumn);

        String[] header = new String[]{"参数中文名称", "参数代码", "参数值", "创建时间"};
        String[] property = new String[]{"paramName", "paramCode", "paramValue", "createDate"};


        InputStream generateExcel = ExportExcelUtil.generateExcel(listObjectsAll, header, property);

        try {
            WebOptUtils.download(generateExcel, "用户参数信息.xls", response);
        } catch (IOException e) {
            throw new ObjectException(e.getMessage());
        }

    }*/
}
