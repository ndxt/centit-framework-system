package com.centit.framework.system.controller;

import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.system.service.UserSettingManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 系统设置
 *
 * @author zou_wy@centit.com
 */
@Controller
@RequestMapping("/systemsetting")
public class SystemSettingController extends BaseController {
    @Resource
    private UserSettingManager userSettingManager;

    /**
     * 系统日志中记录
     *
     * @return 业务标识ID
     */
    public String getOptId() {
        return "SystemSetting";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void updateSystemName(String systemName) {

    }

    @RequestMapping(value = "/uploadico", method = RequestMethod.POST)
    @WrapUpResponseBody
    public ResponseData replaceIcon(HttpServletRequest request) {
        String webPath = request.getSession().getServletContext().getRealPath("");
        request.getServletContext().getRealPath("");
        String filePath = webPath + "/ui/favicon.ico";

        try (OutputStream fileOutputStream = new FileOutputStream(new File(filePath));
             InputStream inputStream = request.getInputStream()) {

            int ch = 0;
            while ((ch = inputStream.read()) != -1) {
                fileOutputStream.write(ch);
            }

        } catch (IOException e) {

        }
        return ResponseData.successResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void setSystemParameter(String systemName, HttpServletResponse response) {

    }
}
