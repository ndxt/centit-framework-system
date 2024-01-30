package com.centit.framework.users.po;

import java.io.Serializable;

/**
 * 授权回调时的参数类
 */
public class AuthCallback implements Serializable {

    private static final long serialVersionUID = 9023382676927471854L;

    /**
     * 访问AuthorizeUrl后回调时带的参数code
     */
    private String code;

    /**
     * 访问AuthorizeUrl后回调时带的参数auth_code，该参数目前只使用于支付宝登录
     */
    private String auth_code;

    /**
     * 访问AuthorizeUrl后回调时带的参数state，用于和请求AuthorizeUrl前的state比较，防止CSRF攻击
     */
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
