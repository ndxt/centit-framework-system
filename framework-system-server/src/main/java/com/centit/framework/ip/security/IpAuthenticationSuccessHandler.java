package com.centit.framework.ip.security;

import com.centit.framework.security.AjaxAuthenticationSuccessHandler;
import com.centit.framework.system.controller.MainFrameController;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 在集成平台中登录 只能进入 部署平台，不能进入任何业务系统
 * @author codefan
 *
 */
public class IpAuthenticationSuccessHandler extends AjaxAuthenticationSuccessHandler{
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		request.getSession().setAttribute(
				MainFrameController.ENTRANCE_TYPE, MainFrameController.DEPLOY_LOGIN);
		super.onAuthenticationSuccess(request,response,authentication);
    }
}
