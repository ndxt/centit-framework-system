package com.centit.framework.users.service.impl;

import com.centit.framework.users.dao.SocialDeptAuthDao;
import com.centit.framework.users.po.SocialDeptAuth;
import com.centit.framework.users.service.SocialDeptAuthService;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("socialDeptAuthServiceImpl")
public class SocialDeptAuthServiceImpl implements SocialDeptAuthService {


    @Autowired
    private SocialDeptAuthDao socialDeptAuthDao;

    @Override
    public void mergeObject(SocialDeptAuth socialDeptAuth) {
        socialDeptAuthDao.mergeObject(socialDeptAuth);
    }

    @Override
    public SocialDeptAuth getObjectById(String unitCode) {
        return socialDeptAuthDao.getObjectById(unitCode);
    }

    @Override
    public SocialDeptAuth getSocialDeptAuthByProperties(Map<String, Object> paramsMap) {
        return socialDeptAuthDao.getObjectByProperties(paramsMap);
    }

    @Override
    public List<SocialDeptAuth> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return socialDeptAuthDao.listObjectsByProperties(filterMap, pageDesc);
    }

    @Override
    public void deleteObjectById(String unitCode) {
        socialDeptAuthDao.deleteObjectById(unitCode);
    }

    @Override
    public void saveSocialDeptAuth(SocialDeptAuth socialDeptAuth) {
        socialDeptAuthDao.saveNewObject(socialDeptAuth);
    }

    @Override
    public void updateSocialDeptAuth(SocialDeptAuth socialDeptAuth) {
        socialDeptAuthDao.updateObject(socialDeptAuth);
    }

    @Override
    public void deleteObject(SocialDeptAuth socialDeptAuth) {
        socialDeptAuthDao.deleteObject(socialDeptAuth);
    }
}
