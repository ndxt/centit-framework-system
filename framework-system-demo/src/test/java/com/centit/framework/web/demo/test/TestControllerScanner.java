package com.centit.framework.web.demo.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.RequestMappingUtils;
import com.centit.framework.system.po.OptInfo;
import com.centit.framework.system.po.OptMethod;
import com.centit.support.algorithm.ClassScannerOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.orm.OrmDaoUtils;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.TransactionHandler;
import com.centit.support.report.ExcelExportUtil;
import com.centit.support.report.ExcelImportUtil;
import org.junit.Test;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testControllerMapping() {
        JSONArray optInfoList = RequestMappingUtils.mapControllerInfosByPackage("com.centit" );
        String excelFileName = "D:/Projects/RunData/demo_home/optInfos.xlsx";
        try (OutputStream newExcelFile = new FileOutputStream(new File(excelFileName))) {
            ExcelExportUtil.generateExcel(newExcelFile,"optInfo",optInfoList,
                new String[]{"业务代码","业务名称","业务Url","页面Url","父类业务代码"},
                new String[]{"optId","","","optUrl"});
            for(Object object : optInfoList){
                JSONArray optMethods = (JSONArray)((JSONObject)object).get("optMethods");
                if(optMethods!=null && optMethods.size()>0){
                    ExcelExportUtil.appendDataToExcelSheet(excelFileName,"optMethod",optMethods,
                        new String[]{"业务代码","操作名称","操作Url","操作方法","请求类型"},
                        new String[]{"optId","optName","optUrl","optName","optReq"});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(optInfoList.toJSONString());
    }

    @Test
    public void testImportOptInfo(){
        Map<Integer, String> optInfoColumnField = new HashMap<>(10);
        optInfoColumnField.put(0, "optId");
        optInfoColumnField.put(1, "optName");
        optInfoColumnField.put(2, "optUrl");
        optInfoColumnField.put(3, "preOptId");

        Map<Integer, String> optMethodColumField = new HashMap<>(12);
        optMethodColumField.put(0, "optId");
        optMethodColumField.put(1, "optName");
        optMethodColumField.put(2, "optUrl");
        optMethodColumField.put(3, "optMethod");
        optMethodColumField.put(4, "optReq");
        List<OptInfo> optInfos = null;
        List<OptMethod> optMethods = null;
        try {
            optInfos = ExcelImportUtil.loadObjectFromExcel(
                "D:/Projects/RunData/demo_home/optInfos.xlsx", "optInfo", OptInfo.class, optInfoColumnField, 1);

            optMethods = ExcelImportUtil.loadObjectFromExcel(
                "D:/Projects/RunData/demo_home/optInfos.xlsx", "optMethod", OptMethod.class, optMethodColumField, 1);

        }catch (IOException | IllegalAccessException | InstantiationException e){
            e.printStackTrace();
        }
        DataSourceDescription dataSourceDescription =
            new DataSourceDescription(
                "jdbc:mysql://192.168.131.6:3306/framework?useUnicode=true&characterEncoding=utf-8",
                "framework","framework");
        final  List<OptInfo> finalOptInfos = optInfos;
        final  List<OptMethod> finalOptMethods = optMethods;
        try {
            TransactionHandler.executeInTransaction(
                dataSourceDescription,
                (conn) -> {
                    for (OptInfo optInfo : finalOptInfos) {
                        OrmDaoUtils.saveNewObject(conn, optInfo);
                    }
                    for(OptMethod optMethod : finalOptMethods) {
                        optMethod.setOptCode(
                            StringBaseOpt.castObjectToString(
                                OrmDaoUtils.getSequenceNextValue(conn,"S_OPTDEFCODE")));
                        OrmDaoUtils.saveNewObject(conn, optMethod);
                    }
                    return null;
//                    throw new SQLException("rollback");
                });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
