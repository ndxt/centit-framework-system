package com.otherpackage.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestCaseController extends BaseController {

    @GetMapping("/lsh/{dateCode}")
    public ResponseData getLsh(@PathVariable String dateCode){
        //this.getLoginUser()
        return ResponseSingleData.makeResponseData( "hello: " + dateCode);
    }
}
