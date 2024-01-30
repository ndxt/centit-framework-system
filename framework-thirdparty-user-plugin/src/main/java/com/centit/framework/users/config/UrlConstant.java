package com.centit.framework.users.config;

/**
 * 钉钉开放接口网关常量
 */
public class UrlConstant {
    private static final String HOST = "https://oapi.dingtalk.com";

    /**
     * 钉钉扫码登录
     */
    public static final String URL_GET_QRCONNECT= HOST + "/connect/qrconnect";

    /**
     * 钉钉账号登录
     */
    public static final String URL_GET_SNSCONNECT= HOST + "/connect/oauth2/sns_authorize";

    /**
     * 获取企业内部应用的access_token url
     */
    public static final String URL_GET_TOKEN = HOST + "/gettoken";

    /**
     * 获取第三方应用授权企业的access_token
     */
    public static final String URL_GET_CORP_TOKEN = HOST + "/service/get_corp_token";

    /**
     * 根据sns临时授权码获取用户信息
     */
    public static final String URL_GET_USER_BYCODE = HOST + "/sns/getuserinfo_bycode";

    /**
     * 根据unionid获取用户userid
     */
    public static final String URL_GET_USER_BYUNIONID = HOST + "/topapi/user/getbyunionid";

    /**
     * 根据userid获取用户详情
     */
    public static final String URL_GET_USER = HOST + "/topapi/v2/user/get";

    /**
     * 获取jsapi_ticket url
     */
    public static final String URL_GET_JSTICKET = HOST + "/get_jsapi_ticket";

    /**
     * 通过免登授权码获取用户信息 url
     */
    public static final String URL_GET_USER_INFO = HOST + "/user/getuserinfo";

    /**
     * 根据用户id获取用户详情 url
     */
    public static final String URL_USER_GET = HOST + "/user/get";

    /**
     * 获取部门列表 url
     */
    public static final String URL_DEPARTMENT_LIST = HOST + "/department/list";

    /**
     * 获取部门用户 url
     */
    public static final String URL_USER_SIMPLELIST = HOST + "/user/simplelist";

    /**
     * 根据手机号查询用户
     */
    public static final String URL_GET_USER_BYMOBILE = HOST + "/topapi/v2/user/getbymobile";

    /**
     * 创建用户
     */
    public static final String USER_CREATE = HOST + "/topapi/v2/user/create";

    /**
     * 创建部门  接口调用请求地址（请求方式：post）
     **/
    public static final String DEPARTMENT_CREATE = HOST + "/topapi/v2/department/create";

    /**
     * 获取部门列表
     */
    public static final String DEPARTMENTS_URL = HOST + "/topapi/v2/department/listsub";

    /**
     * 获取部门详情
     */
    public static final String URL_DEPARTMENT_GET = HOST + "/topapi/v2/department/get";

    /**
     * 获取部门用户详情
     */
    public static final String URL_USER_LIST = HOST + "/topapi/v2/user/list";
}
