package com.centit.framework.system.service.impl;

import com.centit.framework.system.dao.PlatformDao;
import com.centit.framework.model.basedata.Platform;
import com.centit.framework.system.service.PlatformService;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zfg
 */
@Service("platformService")
@Transactional
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformDao platformDao;

    @Override
    public List<Platform> listObjects(Map<String, Object> filterMap) {
        return platformDao.listObjectsByProperties(filterMap);
    }

    @Override
    public List<Platform> listObjects(Map<String, Object> filterMap, PageDesc pageDesc) {
        return platformDao.listObjectsByProperties(filterMap, pageDesc);
    }

    @Override
    public Platform getObjectById(String platId) {
        return platformDao.getObjectById(platId);
    }

    @Override
    public Platform getPlatformByProperties(Map<String, Object> paramsMap) {
        return platformDao.getObjectByProperties(paramsMap);
    }

    @Override
    public boolean hasSamePlat(Platform platform) {
        return platformDao.isUniquePlat(platform.getPlatSourceCode(), platform.getPlatId());
    }

    @Override
    public void savePlatform(Platform platform) {
        platformDao.saveNewObject(platform);
    }

    @Override
    public void updatePlatform(Platform platform) {
        platformDao.updateObject(platform);
    }

    @Override
    public void deletePlatform(Platform platform) {
        platformDao.deleteObjectById(platform.getPlatId());
    }
}
