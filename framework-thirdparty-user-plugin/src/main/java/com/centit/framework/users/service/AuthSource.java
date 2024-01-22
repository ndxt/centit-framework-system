package com.centit.framework.users.service;

/**
 * OAuth平台的API地址的统一接口，提供以下方法：
 * 1) {@link AuthSource#authorize()}: 获取授权url. 必须实现
 * 2) {@link AuthSource#accessToken()}: 获取accessToken的url. 必须实现
 * 3) {@link AuthSource#userInfo()}: 获取用户信息的url. 必须实现
 * 4) {@link AuthSource#revoke()}: 获取取消授权的url. 非必须实现接口（部分平台不支持）
 * 5) {@link AuthSource#refresh()}: 获取刷新授权的url. 非必须实现接口（部分平台不支持）
 *
 * @author zfg
 */
public interface AuthSource {

    /**
     * 授权的api
     *
     * @return url
     */
    String authorize();

    /**
     * 获取accessToken的api
     *
     * @return url
     */
    String accessToken();

    /**
     * 获取用户信息的api
     *
     * @return url
     */
    String userInfo();

    /**
     * 取消授权的api
     *
     * @return url
     */
    default String revoke() {
        return "";
    }

    /**
     * 刷新授权的api
     *
     * @return url
     */
    default String refresh() {
        return "";
    }

    /**
     * 获取Source的字符串名字
     *
     * @return name
     */
    default String getName() {
        if (this instanceof Enum) {
            return String.valueOf(this);
        }
        return this.getClass().getSimpleName();
    }
}
