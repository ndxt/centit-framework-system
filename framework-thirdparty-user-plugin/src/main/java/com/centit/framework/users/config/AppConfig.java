package com.centit.framework.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 应用凭证配置
 */
@Configuration
public class AppConfig {
    @Value("${dingtalk.app_key}")
    private String appKey;

    @Value("${dingtalk.app_secret}")
    private String appSecret;

    @Value("${dingtalk.agent_id}")
    private String agentId;

    @Value("${dingtalk.corp_id}")
    private String corpId;

    @Value("${third.redirect_uri}")
    private String redirectUri;

    @Value("${third.redirect_binduri}")
    private String redirectBindUri;

    @Value("${third.redirect_loginuri}")
    private String redirectLoginUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectBindUri() {
        return redirectBindUri;
    }

    public void setRedirectBindUri(String redirectBindUri) {
        this.redirectBindUri = redirectBindUri;
    }

    public String getRedirectLoginUrl() {
        return redirectLoginUrl;
    }

    public void setRedirectLoginUrl(String redirectLoginUrl) {
        this.redirectLoginUrl = redirectLoginUrl;
    }
}
