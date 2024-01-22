package com.centit.framework.users.service;

import com.centit.framework.users.po.Platform;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author zfg
 */
public interface PlatformService {

    List<Platform> listObjects(Map<String, Object> filterMap);

    List<Platform> listObjects(Map<String, Object> filterMap, PageDesc pageDesc);

    Platform getObjectById(String platId);

    Platform getPlatformByProperties(Map<String,Object> paramsMap);

    boolean hasSamePlat(Platform platform);

    void savePlatform(Platform platform);

    void updatePlatform(Platform platform);

    void deletePlatform(Platform platform);
}
