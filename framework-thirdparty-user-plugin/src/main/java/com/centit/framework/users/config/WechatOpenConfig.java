package com.centit.framework.users.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class WechatOpenConfig {

    @Autowired
    private WxAppConfig wxAppConfig;

    @Bean
    public WxMpService wxOpenService() {
        WxMpService wxOpenService = new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpenConfigStorage());
        return wxOpenService;
    }

    @Bean
    public WxMpConfigStorage wxOpenConfigStorage() {
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(wxAppConfig.getAppID());
        String secret = wxAppConfig.getAppSecret();
        if(StringUtils.isNotBlank(secret)) {
            byte[] decoded = Base64.getDecoder().decode(secret);
            String decodeStr = new String(decoded);
            wxMpInMemoryConfigStorage.setSecret(decodeStr);
        }
        return wxMpInMemoryConfigStorage;
    }
}
