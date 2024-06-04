package com.otherpackage.controller;

import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.otherpackage.po.TestCase;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestCaseController extends BaseController {

    @GetMapping("/lsh/{dateCode}")
    @WrapUpResponseBody
    public ResponseData getLsh(@PathVariable String dateCode, HttpServletResponse response) throws IOException {
        //this.getLoginUser()
        String data = "### 下面是返回的数据：";
        byte[] bytes = data.getBytes(); // 将字符串转换为字节数组

        response.setContentType("text/plain"); // 设置响应的内容类型
        ServletOutputStream outputStream = response.getOutputStream(); // 获取输出流
        outputStream.write(bytes); // 写入字节数据
        outputStream.flush(); // 刷新流，确保所有数据都已经写入
        // outputStream.close();
        return ResponseSingleData.makeResponseData( "hello: " + dateCode);
    }

    @GetMapping("/lsh2")
    public void getLsh2(HttpServletResponse response) throws IOException {
        //this.getLoginUser()
        String data = "### 下面是返回的数据2：";
        byte[] bytes = data.getBytes(); // 将字符串转换为字节数组

        response.setContentType("text/plain"); // 设置响应的内容类型
        ServletOutputStream outputStream = response.getOutputStream(); // 获取输出流
        outputStream.write(bytes); // 写入字节数据
        outputStream.flush(); // 刷新流，确保所有数据都已经写入
        // outputStream.close();
        JsonResultUtils.writeSingleDataJson("hello 我的祖国", response);
    }

    @PostMapping("/save")
    @WrapUpResponseBody
    public TestCase saveTestCase(@RequestBody @Valid TestCase ts) {

        return ts;
    }
}
