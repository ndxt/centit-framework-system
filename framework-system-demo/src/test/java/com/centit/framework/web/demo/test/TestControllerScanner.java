package com.centit.framework.web.demo.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.RequestMappingUtils;
import com.centit.support.algorithm.ClassScannerOpt;

import org.junit.Test;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.List;

/**
 * Created by codefan on 17-9-11.
 */
public class TestControllerScanner {
    @Test
    public void testClassScanner() {
        List<Class<?>> classes = ClassScannerOpt.getClassList("com.centit" ,
            true, Controller.class);
        for(Class clazz : classes){
            System.out.println(clazz.getTypeName());
        }

        System.out.println("done!");
    }

//    @Test
//    public void testControllerMapping() {
//        JSONArray optInfoList = RequestMappingUtils.mapControllerInfosByPackage("com.centit" );
//        String excelFileName = "D:/Projects/RunData/demo_home/optInfos.xlsx";
//        try (OutputStream newExcelFile = new FileOutputStream(new File(excelFileName))) {
//            ExcelExportUtil.generateExcel(newExcelFile,"optInfo",optInfoList,
//                new String[]{"业务代码","业务名称","业务Url","页面Url","父类业务代码"},
//                new String[]{"optId","","","optUrl"});
//            for(Object object : optInfoList){
//                JSONArray optMethods = (JSONArray)((JSONObject)object).get("optMethods");
//                if(optMethods!=null && optMethods.size()>0){
//                    ExcelExportUtil.appendDataToExcelSheet(excelFileName,"optMethod",optMethods,
//                        new String[]{"业务代码","操作名称","操作Url","操作方法","请求类型"},
//                        new String[]{"optId","optName","optUrl","optName","optReq"});
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(optInfoList.toJSONString());
//    }
}
