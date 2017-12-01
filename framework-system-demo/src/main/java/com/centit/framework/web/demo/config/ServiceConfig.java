package com.centit.framework.web.demo.config;

import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.config.SpringSecurityCasConfig;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.hibernate.config.HibernateConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.system.config.SystemBeanConfig;
import com.centit.framework.web.demo.listener.InstantiationServiceBeanPostProcessor;
import org.springframework.context.annotation.*;

/**
 * Created by codefan on 17-7-18.
 */
@Configuration
@Import({HibernateConfig.class,
        SystemBeanConfig.class,
        SpringSecurityDaoConfig.class,
        SpringSecurityCasConfig.class})
@ComponentScan(basePackages = "com.centit.framework",
        excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
public class ServiceConfig {
    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initMsgSenders();
        //notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    @Lazy(value = false)
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl  operationLog =  new TextOperationLogWriterImpl();
        operationLog.init();
        return operationLog;
    }

    @Bean
    public InstantiationServiceBeanPostProcessor instantiationServiceBeanPostProcessor() {
        return new InstantiationServiceBeanPostProcessor();
    }

}
