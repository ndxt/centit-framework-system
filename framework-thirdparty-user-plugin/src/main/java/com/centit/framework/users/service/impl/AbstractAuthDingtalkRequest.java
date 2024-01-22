package com.centit.framework.users.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.po.AccessToken;
import com.centit.framework.users.po.AuthCallback;
import com.centit.framework.users.service.AuthSource;
import com.centit.support.network.UrlOptUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 钉钉登录抽象类，负责处理使用钉钉账号登录第三方网站和扫码登录第三方网站两种钉钉的登录方式
 * </p>
 *
 * @author zfg
 */
public abstract class AbstractAuthDingtalkRequest extends AuthDefaultRequest {

    public AbstractAuthDingtalkRequest(AppConfig config, AuthSource source) {
        super(config, source);
    }

    @Override
    protected AccessToken getAccessToken(AuthCallback authCallback) {
        //return AccessToken.builder().accessCode(authCallback.getCode()).build();
        return null;
    }

    @Override
    protected UserInfo getUserInfo(AccessToken accessToken) {
        String code = accessToken.getAccessCode();
        JSONObject param = new JSONObject();
        param.put("tmp_auth_code", code);
        //String response = new HttpUtils(config.getHttpConfig()).post(userInfoUrl(accessToken), param.toJSONString());
        String response = "";
        JSONObject object = JSON.parseObject(response);
        if (object.getIntValue("errcode") != 0) {
            //throw new AuthException(object.getString("errmsg"));
        }
        object = object.getJSONObject("user_info");
        UserInfo userInfo = new UserInfo();
        /*AccessToken token = AccessToken.builder()
            .openId(object.getString("openid"))
            .unionId(object.getString("unionid"))
            .build();*/

        return userInfo;
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    @Override
    public String authorize(String state) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("response_type", "code");
        queryParam.put("appid", config.getAppKey());
        queryParam.put("scope", "snsapi_login");
        queryParam.put("state", getRealState(state));
        queryParam.put("redirect_uri", config.getRedirectUri());
        return UrlOptUtils.appendParamsToUrl(source.authorize(), queryParam);
    }

    /**
     * 返回获取userInfo的url
     *
     * @param accessToken 用户授权后的token
     * @return 返回获取userInfo的url
     */
    @Override
    protected String userInfoUrl(AccessToken accessToken) {
        // 根据timestamp, appSecret计算签名值
        String timestamp = System.currentTimeMillis() + "";
        String urlEncodeSignature = "";
            //GlobalAuthUtils.generateDingTalkSignature(config.getAppSecret(), timestamp);

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("signature", urlEncodeSignature);
        queryParam.put("timestamp", timestamp);
        queryParam.put("accessKey", config.getAppKey());

        return UrlOptUtils.appendParamsToUrl(source.userInfo(), queryParam);
    }

}
