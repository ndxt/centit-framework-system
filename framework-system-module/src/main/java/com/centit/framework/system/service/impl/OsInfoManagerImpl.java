package com.centit.framework.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.framework.system.dao.OsInfoDao;
import com.centit.framework.system.po.OsInfo;
import com.centit.framework.system.service.OsInfoManager;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("osInfoManager")
@Transactional
public class OsInfoManagerImpl extends BaseEntityManagerImpl<OsInfo, String, OsInfoDao>
        implements OsInfoManager, CodeRepositoryCache.EvictCacheExtOpt {

    //private static final SysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog();
    private String refreshUrl = "/system/environment/reload/refreshall";
    private String refreshByNameUrl = "/system/environment/reload/refresh/%s";

    @Override
    @Autowired
    @Qualifier("osInfoDao")
    public void setBaseDao(OsInfoDao baseDao) {
        super.baseDao = baseDao;
    }

    @Override
    public List<OsInfo> listObjects(Map<String, Object> map){
        return baseDao.listObjects(map);
    }

    @Override
    public List<OsInfo> listObjects(Map<String, Object> map, PageDesc pageDesc){
        return baseDao.listObjectsByProperties(map, pageDesc);
    }

    @Override
    public JSONArray listOsInfoAsJson(Map<String, Object> filterMap, PageDesc pageDesc){
        return baseDao.listObjectsAsJson(filterMap, pageDesc);
    }

    /**
     * 刷新单个系统的全部缓存
     * @param osInfo 系统信息
     * @return boolean
     */
    @Override
    public boolean refreshSingle(OsInfo osInfo) {
        boolean flag = true;
        try (CloseableHttpClient httpClient = HttpExecutor.createHttpClient()) {
            HttpExecutor.simpleGet(HttpExecutorContext.create(httpClient),osInfo.getOsUrl() + refreshUrl);
        }catch (IOException e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 刷新所有系统的全部缓存
     * @return boolean
     */
    @Override
    public boolean refreshAll() {
        List<OsInfo> osInfoList = this.listObjects(new HashMap<>());
        if(osInfoList != null && osInfoList.isEmpty()){
            return true;
        }

        boolean flag = true;
        for(OsInfo osInfo : osInfoList){
            try (CloseableHttpClient httpClient = HttpExecutor.createHttpClient()) {
                HttpExecutor.simpleGet(HttpExecutorContext.create(httpClient),osInfo.getOsUrl() + refreshUrl);
            }catch (IOException e){
                logger.error(e.getLocalizedMessage());
                //e.printStackTrace();
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 刷新所有系统的某个缓存
     * @param cacheName 缓存名
     * @param mapKey 对应额key
     */
    @Override
    public void evictCache(String cacheName, String mapKey) {
        this.evictCache(cacheName);
    }

    /**
     * 刷新所有系统的某个缓存
     * @param cacheName 缓存名
     */
    @Override
    public void evictCache(String cacheName) {
        /*List<OsInfo> osInfoList = this.listObjects(new HashMap<>());
        if(osInfoList != null && osInfoList.isEmpty()){
            return;
        }
        for(OsInfo osInfo : osInfoList) {
            try (CloseableHttpClient httpClient = HttpExecutor.createHttpClient()) {
                HttpExecutor.simpleGet(HttpExecutorContext.create(httpClient),
                    osInfo.getOsUrl() + String.format(refreshByNameUrl,cacheName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * 刷新所有系统的全部缓存
     */
    @Override
    public void evictAllCache() {
        refreshAll();
    }
}

