package com.centit.framework.users.service.impl;

import com.centit.framework.users.service.WeChatService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("weChatService")
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private WxMpService wxOpenService;

    @Override
    public WxMpUser getWxUser(String code) {
        WxMpUser wxMpUser = new WxMpUser();
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = this.getAccessToken(code);
            wxMpUser = wxOpenService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return wxMpUser;
    }

    @Override
    public WxMpOAuth2AccessToken getAccessToken(String code) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            //通过code获取access_token
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return wxMpOAuth2AccessToken;
    }
}
