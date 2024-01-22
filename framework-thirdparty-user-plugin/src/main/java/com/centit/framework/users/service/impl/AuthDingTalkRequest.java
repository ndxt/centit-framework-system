package com.centit.framework.users.service.impl;

import com.centit.framework.users.config.AppConfig;

/**
 * 钉钉二维码登录
 *
 * @author zfg
 */
public class AuthDingTalkRequest extends AbstractAuthDingtalkRequest {

    public AuthDingTalkRequest(AppConfig config) {
        super(config, AuthDefaultSource.DINGTALK);
    }
}
