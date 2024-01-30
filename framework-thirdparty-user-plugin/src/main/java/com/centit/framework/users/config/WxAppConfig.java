package com.centit.framework.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxAppConfig {

    @Value("${wechat.appid:}")
    private String appID;

    @Value("${wechat.appsecret:}")
    private String appSecret;

    @Value("${wechat.mobileAppid:}")
    private String moblieAppId;

    @Value("${wechat.mobileAppSecret:}")
    private String moblieAppSecret;

    @Value("${third.redirect_uri:}")
    private String redirectLoginUri;

    @Value("${third.redirect_binduri:}")
    private String redirectBindUri;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectLoginUri() {
        return redirectLoginUri;
    }

    public void setRedirectLoginUri(String redirectLoginUri) {
        this.redirectLoginUri = redirectLoginUri;
    }

    public String getRedirectBindUri() {
        return redirectBindUri;
    }

    public void setRedirectBindUri(String redirectBindUri) {
        this.redirectBindUri = redirectBindUri;
    }

    public String getMoblieAppId() {
        return moblieAppId;
    }

    public void setMoblieAppId(String moblieAppId) {
        this.moblieAppId = moblieAppId;
    }

    public String getMoblieAppSecret() {
        return moblieAppSecret;
    }

    public void setMoblieAppSecret(String moblieAppSecret) {
        this.moblieAppSecret = moblieAppSecret;
    }
}
