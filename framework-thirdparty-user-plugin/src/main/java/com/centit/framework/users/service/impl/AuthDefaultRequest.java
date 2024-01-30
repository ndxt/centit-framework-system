package com.centit.framework.users.service.impl;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.po.AccessToken;
import com.centit.framework.users.po.AuthCallback;
import com.centit.framework.users.service.AuthSource;
import com.centit.framework.users.service.LoginService;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.network.UrlOptUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的login处理类
 *
 * @author zfg
 */
public abstract class AuthDefaultRequest implements LoginService {

    private Logger logger = LoggerFactory.getLogger(AuthDefaultRequest.class);

    protected AppConfig config;

    protected AuthSource source;

    public AuthDefaultRequest(AppConfig config, AuthSource source) {
        this.config = config;
        this.source = source;
    }

    /**
     * 获取access token
     *
     * @param authCallback 授权成功后的回调参数
     * @return token
     * @see AuthDefaultRequest#authorize(String)
     */
    protected abstract AccessToken getAccessToken(AuthCallback authCallback);

    /**
     * 使用token换取用户信息
     *
     * @param accessToken token信息
     * @return 用户信息
     * @see AuthDefaultRequest#getAccessToken(AuthCallback)
     */
    protected abstract UserInfo getUserInfo(AccessToken accessToken);

    /**
     * 统一的登录入口。当通过{@link AuthDefaultRequest#authorize(String)}授权成功后，会跳转到调用方的相关回调方法中
     * 方法的入参可以使用{@code AuthCallback}，{@code AuthCallback}类中封装好了OAuth2授权回调所需要的参数
     *
     * @param authCallback 用于接收回调参数的实体
     * @return AuthResponse
     */
    @Override
    public ResponseData login(AuthCallback authCallback) {
        try {
            AccessToken accessToken = this.getAccessToken(authCallback);
            UserInfo user = this.getUserInfo(accessToken);
            return ResponseSingleData.makeResponseData(user);
        } catch (Exception e) {
            logger.error("Failed to login with oauth authorization.", e);
            return ResponseSingleData.errorResponse;
        }
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
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    protected String accessTokenUrl(String code) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("appkey", config.getAppKey());
        queryParam.put("appsecret", config.getAppSecret());
        return UrlOptUtils.appendParamsToUrl(source.accessToken(), queryParam);
    }

    /**
     * 返回获取accessToken的url
     *
     * @param refreshToken refreshToken
     * @return 返回获取accessToken的url
     */
    protected String refreshTokenUrl(String refreshToken) {
        return UrlOptUtils.appendParamsToUrl(source.refresh(), null);
    }

    /**
     * 返回获取userInfo的url
     *
     * @param accessToken token
     * @return 返回获取userInfo的url
     */
    protected String userInfoUrl(AccessToken accessToken) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("access_token", accessToken.getAccessToken());
        return UrlOptUtils.appendParamsToUrl(source.userInfo(), queryParam);
    }

    /**
     * 返回获取revoke authorization的url
     *
     * @param accessToken token
     * @return 返回获取revoke authorization的url
     */
    protected String revokeUrl(AccessToken accessToken) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("access_token", accessToken.getAccessToken());
        return UrlOptUtils.appendParamsToUrl(source.revoke(), queryParam);
    }

    /**
     * 获取state，如果为空， 则默认取当前日期的时间戳
     *
     * @param state 原始的state
     * @return 返回不为null的state
     */
    protected String getRealState(String state) {
        if (StringUtils.isEmpty(state)) {
            state = UuidOpt.getUuidAsString22();
        }
        return state;
    }

}
