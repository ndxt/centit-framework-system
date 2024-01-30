package com.centit.framework.users.service.impl;

import com.centit.framework.users.config.AppConfig;

/**
 * 钉钉账号登录
 *
 * @author zfg
 */
public class AuthDingTalkAccountRequest extends AbstractAuthDingtalkRequest {

    public AuthDingTalkAccountRequest(AppConfig config) {
        super(config, AuthDefaultSource.DINGTALK_ACCOUNT);
    }

}
