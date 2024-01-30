package com.centit.framework.users.service;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.users.po.AccessToken;
import com.centit.framework.users.po.AuthCallback;

/**
 * @author zfg
 */
public interface LoginService {

    /**
     * 返回带{@code state}参数的授权url，可自行跳转页面，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    String authorize(String state);

    /**
     * 第三方登录
     *
     * @param authCallback 用于接收回调参数的实体
     * @return 返回登录成功后的用户信息
     */
    ResponseData login(AuthCallback authCallback);

    /**
     * 撤销授权
     *
     * @param accessToken 登录成功后返回的Token信息
     * @return ResponseSingleData
     */
    default ResponseData revoke(AccessToken accessToken) {
        return ResponseSingleData.makeResponseData("");
    }

    /**
     * 刷新access token （续期）
     *
     * @param accessToken 登录成功后返回的Token信息
     * @return ResponseSingleData
     */
    default ResponseData refresh(AccessToken accessToken) {
        return ResponseSingleData.makeResponseData("");
    }
}
