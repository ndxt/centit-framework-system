package com.centit.prodcts.demoapp.config;

import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityCasConfig;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.security.StandardPasswordEncoderImpl;
import com.centit.framework.session.SimpleMapSessionRepository;
import com.centit.framework.system.config.SystemBeanConfig;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.IndexerSearcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * Created by codefan on 17-7-18.
 */
@Configuration
@PropertySource("classpath:system.properties")
@ComponentScan(basePackages = {"com.centit", "com.otherpackage"},
    excludeFilters = @ComponentScan.Filter(
        value = org.springframework.stereotype.Controller.class))
@Import({SystemBeanConfig.class,
    SpringSecurityCasConfig.class,
    SpringSecurityDaoConfig.class,
    JdbcConfig.class})
@EnableSpringHttpSession
public class ServiceConfig {

    @Bean
    public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor(){
        return new AutowiredAnnotationBeanPostProcessor();
    }

    /**
     * 这个bean必须要有 可以配置不同策略的加密方式
     * @return CentitPasswordEncoder 密码加密算法
     */
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }

    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        //notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    public DataScopePowerManager dataScopePowerManager(){
        return new DataScopePowerManagerImpl();
    }

    /*@Bean
    @Lazy(value = false)
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl operationLog =  new TextOperationLogWriterImpl();
        operationLog.init();
        return operationLog;
    }*/

    @Bean
    public InstantiationServiceBeanPostProcessor instantiationServiceBeanPostProcessor() {
        return new InstantiationServiceBeanPostProcessor();
    }

    @Bean
    public FindByIndexNameSessionRepository sessionRepository() {
        return new SimpleMapSessionRepository();
    }


    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return //new LazyCsrfTokenRepository(
            new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public SessionRegistry sessionRegistry(
        @Autowired FindByIndexNameSessionRepository sessionRepository){
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    @Bean
    public ESServerConfig esServerConfig() {
        return IndexerSearcherFactory.loadESServerConfigFormProperties(
            SysParametersUtils.loadProperties()
        );
    }

}
