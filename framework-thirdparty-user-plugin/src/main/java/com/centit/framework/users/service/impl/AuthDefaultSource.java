package com.centit.framework.users.service.impl;

import com.centit.framework.users.service.AuthSource;

/**
 * @author zfg
 */
public enum AuthDefaultSource implements AuthSource {

    /**
     * 钉钉扫码登录
     */
    DINGTALK {
        @Override
        public String authorize() {
            return "https://oapi.dingtalk.com/connect/qrconnect";
        }

        @Override
        public String accessToken() {
            return "https://oapi.dingtalk.com/gettoken";
        }

        @Override
        public String userInfo() {
            return "https://oapi.dingtalk.com/sns/getuserinfo_bycode";
        }
    },

    /**
     * 钉钉账号登录
     */
    DINGTALK_ACCOUNT {
        @Override
        public String authorize() {
            return "https://oapi.dingtalk.com/connect/oauth2/sns_authorize";
        }

        @Override
        public String accessToken() {
            return DINGTALK.accessToken();
        }

        @Override
        public String userInfo() {
            return DINGTALK.userInfo();
        }
    },
}
