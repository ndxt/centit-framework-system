package com.centit.framework.users.service;

import com.centit.framework.users.po.AccessToken;

/**
 * @author zfg
 */
public interface AccessTokenService {

    AccessToken getObjectById(String appId);

    void saveAccessToke(AccessToken accessToken);

    void updateAccessToken(AccessToken accessToken);
}
