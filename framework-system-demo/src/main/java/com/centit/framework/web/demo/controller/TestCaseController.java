package com.centit.framework.web.demo.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.system.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.DatetimeOpt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/test")
public class TestCaseController extends BaseController {

    @Resource
    @NotNull
    protected OptFlowNoInfoManager optFlowNoInfoManager;

    @GetMapping("/lsh/{dateCode}")
    public ResponseData getLsh(@PathVariable String dateCode){
        return ResponseSingleData.makeResponseData(
            optFlowNoInfoManager.newNextLshBaseDay(
                OptFlowNoInfoManager.DefaultOwnerCode,
                dateCode, DatetimeOpt.currentUtilDate()));
    }
}
