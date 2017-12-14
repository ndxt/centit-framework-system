package com.centit.framework.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.model.basedata.OperationLog;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.framework.system.po.OptLog;
import com.centit.framework.system.service.OptLogManager;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/optlog")
public class OptLogController extends BaseController {

    @Resource
    @NotNull
    private OptLogManager optLogManager;

    //private String optId = "OPTLOG";
    public String getOptId() {
        return "OPTLOG";
    }

    /**
     * 查询系统日志
     *
     * @param field    需要显示的字段
     * @param pageDesc 分页信息
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        JSONArray jsonArray = optLogManager.listObjectsAsJson(field, searchColumn, pageDesc);

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, jsonArray);
        resData.addResponseData(PAGE_DESC, pageDesc);
        resData.addResponseData(CodeBook.SELF_ORDER_BY, searchColumn.get(CodeBook.SELF_ORDER_BY));
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查询单条日志
     *
     * @param logId    logId
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{logId}", method = {RequestMethod.GET})
    public void getOptLogById(@PathVariable Long logId, HttpServletResponse response) {
        OptLog dbOptLog = optLogManager.getObjectById(logId);
        if (null == dbOptLog) {
            JsonResultUtils.writeErrorMessageJson("日志信息不存在", response);
        }
        JsonResultUtils.writeSingleDataJson(dbOptLog, response);
    }

    /**
     * 删除单条系统日志
     *
     * @param logId    logId
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/{logId}", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "删除日志")
    public void deleteOne(@PathVariable Long logId, HttpServletRequest request, HttpServletResponse response) {
        OptLog optLog = optLogManager.getObjectById(logId);
        optLogManager.deleteObjectById(logId);
        JsonResultUtils.writeBlankJson(response);

        /***************log*******************/
//        OperationLogCenter.logDeleteObject(request, optId, logId.toString(), OperationLog.P_OPT_LOG_METHOD_D,
//                "删除日志", optLog);
        /***************log*******************/
    }

    /**
     * 删除多条系统日志
     *
     * @param logIds   logIds[]
     * @param response HttpServletResponse
     * @param request  HttpServletRequest
     */
    @RequestMapping(value = "/deleteMany", method = RequestMethod.DELETE)
    @RecordOperationLog(content = "删除日志")
    public void deleteMany(Long[] logIds, HttpServletRequest request, HttpServletResponse response) {
        /*for(Long logId : logIds) {
            OptLog optLog = optLogManager.getObjectById(logId);
            *//***************log*******************//*
            OperationLogCenter.logDeleteObject(request, optId, logId.toString(), OperationLog.P_OPT_LOG_METHOD_D,
                    "删除日志", optLog);
            *//***************log*******************//*
        }*/
        optLogManager.deleteMany(logIds);

        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 删除某时段之前的系统日志
     *
     * @param begin    Date
     * @param end      Date
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    @RecordOperationLog(content = "删除日志")
    public void deleteByTime(Date begin, Date end, HttpServletResponse response) {
        optLogManager.delete(begin, end);

        JsonResultUtils.writeBlankJson(response);
    }
}
