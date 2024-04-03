package com.centit.framework.ip.config;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.system.service.impl.DBPlatformEnvironment;
import com.centit.support.json.JSONOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by codefan on 17-7-6.
 */
public class InstantiationServiceBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    protected NotificationCenter notificationCenter;

    @Autowired(required = false)
    private OperationLogWriter optLogManager;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Value("${http.exception.notAsHttpError:false}")
    protected boolean httpExceptionNotAsHttpError;

    @Value("${app.support.tenant:true}")
    protected boolean supportTenant;

    @Autowired
    protected CodeRepositoryCache.EvictCacheExtOpt osInfoManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        JSONOpt.fastjsonGlobalConfig();
        CodeRepositoryCache.setAllCacheFreshPeriod(CodeRepositoryCache.CACHE_FRESH_PERIOD_SECONDS);

        WebOptUtils.setExceptionNotAsHttpError(httpExceptionNotAsHttpError);
        WebOptUtils.setIsTenant(supportTenant);
        if (optLogManager != null) {
            OperationLogCenter.registerOperationLogWriter(optLogManager);
        }
        DBPlatformEnvironment dbPlatformEnvironment = event.getApplicationContext().getBean("platformEnvironment",
            DBPlatformEnvironment.class);
        dbPlatformEnvironment.setSupportTenant(supportTenant);
        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
    }

}
