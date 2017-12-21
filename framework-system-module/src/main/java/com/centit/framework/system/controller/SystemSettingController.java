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
import com.centit.support.file.FileSystemOpt;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author zou_wy@centit.com
 */
@Controller
@RequestMapping("/systemsetting")
public class SystemSettingController extends BaseController {
    @Resource
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     */
    public String getOptId() {
        return  "SystemSetting";
    }

    @RequestMapping(value="uploadico", method = RequestMethod.POST)
    public void replaceIcon(HttpServletRequest request, HttpServletResponse response) {
        String webPath = request.getSession().getServletContext().getRealPath("");
        request.getServletContext().getRealPath("");
        String filePath = webPath + "/favicon.ico";

        try (OutputStream fileOutputStream = new FileOutputStream(new File(filePath));
             InputStream inputStream = request.getInputStream()) {

            int ch = 0;
            while ((ch = inputStream.read()) != -1){
                fileOutputStream.write(ch);
            }

        } catch (IOException e) {

        }
        JsonResultUtils.writeBlankJson(response);
    }
}
