package com.centit.framework.users.service.impl;

import com.centit.framework.users.dao.AccessTokenDao;
import com.centit.framework.users.po.AccessToken;
import com.centit.framework.users.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zfg
 */
@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    private AccessTokenDao accessTokenDao;

    @Override
    public AccessToken getObjectById(String appId) {
        return accessTokenDao.getObjectById(appId);
    }

    @Override
    public void saveAccessToke(AccessToken accessToken) {
        accessTokenDao.mergeObject(accessToken);
    }

    @Override
    public void updateAccessToken(AccessToken accessToken) {
        accessTokenDao.updateObject(accessToken);
    }
}
