package com.otherpackage.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.otherpackage.po.TestCase;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
public class TestCaseController extends BaseController {

    @GetMapping("/lsh/{dateCode}")
    @WrapUpResponseBody
    public ResponseData getLsh(@PathVariable String dateCode){
        //this.getLoginUser()
        return ResponseSingleData.makeResponseData( "hello: " + dateCode);
    }

    @PostMapping("/save")
    @WrapUpResponseBody
    public TestCase saveTestCase(@RequestBody @Valid TestCase ts) {

        return ts;
    }
}
