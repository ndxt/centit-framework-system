package com.centit.framework.users.service.impl;

import com.centit.framework.users.dao.DingTalkSuiteDao;
import com.centit.framework.users.po.DingTalkSuite;
import com.centit.framework.users.service.DingTalkSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author zfg
 */
@Service("dingTalkSuiteService")
public class DingTalkSuiteServiceImpl implements DingTalkSuiteService {

    @Autowired
    private DingTalkSuiteDao dingTalkSuiteDao;

    @Override
    public DingTalkSuite getDingTalkSuiteByProperty(Map<String,Object> paramsMap) {
        return dingTalkSuiteDao.getObjectByProperties(paramsMap);
    }

    @Override
    @Transactional
    public void saveDingTalkSuiteInfo(DingTalkSuite dingTalkSuite) {
        dingTalkSuiteDao.saveNewObject(dingTalkSuite);
    }

    @Override
    @Transactional
    public void updateDingTalkSuiteInfo(DingTalkSuite dingTalkSuite) {
        dingTalkSuiteDao.updateObject(dingTalkSuite);
    }
}
