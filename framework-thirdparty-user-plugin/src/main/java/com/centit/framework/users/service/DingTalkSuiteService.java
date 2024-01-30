package com.centit.framework.users.service;

import com.centit.framework.users.po.DingTalkSuite;

import java.util.Map;

/**
 * @author zfg
 */
public interface DingTalkSuiteService {

    DingTalkSuite getDingTalkSuiteByProperty(Map<String,Object> paramsMap);

    void saveDingTalkSuiteInfo(DingTalkSuite dingTalkSuite);

    void updateDingTalkSuiteInfo(DingTalkSuite dingTalkSuite);
}
