package com.centit.prodcts.demoapp.config;

import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.system.service.impl.DBPlatformEnvironment;
import com.centit.support.json.JSONOpt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.validation.constraints.NotNull;

/**
 * Created by codefan on 17-7-6.
 */
public class InstantiationServiceBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware
{
    @Autowired
    protected NotificationCenter notificationCenter;

    @Autowired(required = false)
    private OperationLogWriter optLogManager;


    @Autowired
    @NotNull
    private PlatformEnvironment platformEnvironment;

    @Value("${app.support.tenant:false}")
    protected boolean supportTenant;
    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        JSONOpt.fastjsonGlobalConfig();
        CodeRepositoryCache.setPlatformEnvironment(platformEnvironment);
        WebOptUtils.setIsTenant(supportTenant);
        if(optLogManager!=null) {
            OperationLogCenter.registerOperationLogWriter(optLogManager);
        }
        DBPlatformEnvironment dbPlatformEnvironment = applicationContext.getBean("platformEnvironment", DBPlatformEnvironment.class);
        dbPlatformEnvironment.setSupportTenant(supportTenant);
        /*if(operationLogWriter!=null) {
            OperationLogCenter.registerOperationLogWriter(operationLogWriter);
        }*/
    }

}
