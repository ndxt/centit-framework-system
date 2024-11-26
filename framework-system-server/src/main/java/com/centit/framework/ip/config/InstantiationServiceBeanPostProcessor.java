package com.centit.framework.ip.config;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.support.json.JSONOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by codefan on 17-7-6.
 */
public class InstantiationServiceBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private OperationLogWriter optLogManager;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Value("${http.exception.notAsHttpError:false}")
    protected boolean httpExceptionNotAsHttpError;

    @Autowired
    protected CodeRepositoryCache.EvictCacheExtOpt osInfoManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        JSONOpt.fastjsonGlobalConfig();
        CodeRepositoryCache.setAllCacheFreshPeriod(CodeRepositoryCache.CACHE_FRESH_PERIOD_SECONDS);

        WebOptUtils.setExceptionNotAsHttpError(httpExceptionNotAsHttpError);
        if (optLogManager != null) {
            OperationLogCenter.registerOperationLogWriter(optLogManager);
        }
        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
    }

}
