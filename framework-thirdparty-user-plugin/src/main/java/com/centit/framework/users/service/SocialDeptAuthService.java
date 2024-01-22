package com.centit.framework.users.service;

import com.centit.framework.users.po.SocialDeptAuth;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface SocialDeptAuthService {

    SocialDeptAuth getObjectById(String unitCode);

    SocialDeptAuth getSocialDeptAuthByProperties(Map<String, Object> paramsMap);

    List<SocialDeptAuth> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    void mergeObject(SocialDeptAuth socialDeptAuth);

    void deleteObjectById(String unitCode);

    void saveSocialDeptAuth(SocialDeptAuth socialDeptAuth);

    void updateSocialDeptAuth(SocialDeptAuth socialDeptAuth);

    void deleteObject(SocialDeptAuth socialDeptAuth);
}
