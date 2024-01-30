package com.centit.framework.users.service.impl;

import com.centit.framework.common.ResponseData;
import com.centit.framework.users.po.AccessToken;
import com.centit.framework.users.po.AuthCallback;
import com.centit.framework.users.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * @author zfg
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public String authorize(String state) {
        return null;
    }

    @Override
    public ResponseData login(AuthCallback authCallback) {
        return null;
    }

    @Override
    public ResponseData revoke(AccessToken accessToken) {
        return null;
    }

    @Override
    public ResponseData refresh(AccessToken accessToken) {
        return null;
    }
}
