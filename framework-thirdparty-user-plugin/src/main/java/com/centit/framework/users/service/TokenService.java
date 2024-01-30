package com.centit.framework.users.service;

import com.centit.framework.common.ResponseData;
import com.centit.framework.users.config.AppConfig;
import com.centit.framework.users.config.UrlConstant;
import com.centit.framework.users.po.AccessToken;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 获取access_token 和 jsTicket方法
 */
@Service
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    /**
     * 缓存时间：7200秒（2小时）
     */
    private static final long CACHE_TTL = 60 * 60 * 2 * 1000L;

    private AppConfig appConfig;

    @Autowired
    public TokenService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private DingTalkSuiteService dingTalkSuiteService;


    /**
     * access_token的有效期为7200秒（2小时），有效期内重复获取会返回相同结果并自动续期，过期后获取会返回新的access_token。
     *
     * @return
     */
    public ResponseData getAccessToken() {
        // 从持久化存储中读取
        //String accessToken = getFromCache("accessToken", "access_token");
        String accessToken = getFromDb(appConfig.getAppKey());
        if (StringUtils.isNotBlank(accessToken)) {
            return ResponseData.makeResponseData(accessToken);
        }

        DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        OapiGettokenResponse response;

        request.setAppkey(appConfig.getAppKey());
        request.setAppsecret(appConfig.getAppSecret());
        request.setHttpMethod("GET");

        try {
            response = client.execute(request);
        } catch (ApiException e) {
            log.error("getAccessToken failed", e);
            return ResponseData.makeErrorMessage(Integer.valueOf(e.getErrCode()), e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        accessToken = response.getAccessToken();
        //putToCache("accessToken", "access_token", accessToken);
        saveTokenTodb(appConfig.getAppKey(), accessToken, response.getExpiresIn());
        return ResponseData.makeResponseData(accessToken);
    }


    public String getFromDb(String section) {
        AccessToken accessToken = accessTokenService.getObjectById(section);
        if (null != accessToken) {
            long createTime = accessToken.getCreateTime().getTime();
            if (System.currentTimeMillis() - createTime <= CACHE_TTL) {
                return accessToken.getAccessToken();
            }
        }
        return null;
    }

    private void saveTokenTodb(String section, String value, Long expiresIn) {
        AccessToken accessToken = new AccessToken();
        accessToken.setAppId(section);
        accessToken.setAccessToken(value);
        accessToken.setExpireIn(expiresIn);
        accessToken.setExpireTime(new Date((System.currentTimeMillis() + expiresIn * 1000)));
        accessTokenService.saveAccessToke(accessToken);
    }

    /**
     * 获取JSTicket, 用于js的签名计算
     * 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
     *
     * @return jsTicket或错误信息
     */
    public ResponseData getJsTicket() {
        // 从持久化存储中读取
        //String ticket = getFromCache("jsticket", "ticket");
        String ticket = getFromDb("jsticket");
        if (StringUtils.isNotBlank(ticket)) {
            return ResponseData.makeResponseData(ticket);
        }

        String accessToken;
        ResponseData tokenSr = getAccessToken();
        if (tokenSr.getCode() != 0) {
            return ResponseData.makeErrorMessage(tokenSr.getCode(), tokenSr.getMessage());
        }
        accessToken = tokenSr.getData().toString();

        DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_JSTICKET);
        OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
        OapiGetJsapiTicketResponse response;
        request.setHttpMethod("GET");
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error("getAccessToken failed", e);
            return ResponseData.makeErrorMessage(Integer.valueOf(e.getErrCode()), e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ResponseData.makeErrorMessage(Integer.valueOf(response.getErrorCode()), response.getErrmsg());
        }
        ticket = response.getTicket();
        //putToCache("jsticket", "ticket", ticket);
        saveTokenTodb("jsticket", ticket, response.getExpiresIn());
        return ResponseData.makeResponseData(ticket);
    }

}
