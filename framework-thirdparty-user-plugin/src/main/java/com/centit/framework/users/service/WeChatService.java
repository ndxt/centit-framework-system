package com.centit.framework.users.service;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface WeChatService {

    WxMpUser getWxUser(String code);

    WxMpOAuth2AccessToken getAccessToken(String code);
}
