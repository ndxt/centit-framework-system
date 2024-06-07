package com.centit.framework.ip.config;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.config.SpringSecurityCasConfig;
import com.centit.framework.config.SpringSecurityDaoConfig;
import com.centit.framework.dubbo.config.DubboConfig;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.security.StandardPasswordEncoderImpl;
import com.centit.framework.system.config.SystemBeanConfig;
import com.centit.msgpusher.plugins.EMailMsgPusher;
import com.centit.msgpusher.plugins.SystemUserEmailSupport;
import com.centit.search.service.ESServerConfig;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.security.SecurityOptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Created by codefan on 17-7-18.
 */
@Configuration
@PropertySource("classpath:system.properties")
@ComponentScan(basePackages = "com.centit",
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import({DubboConfig.class,
    SystemBeanConfig.class,
    SpringSecurityCasConfig.class,
    SpringSecurityDaoConfig.class,
    JdbcConfig.class})
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "${nacos.server-addr}"))
@NacosPropertySource(dataId = "${nacos.system-dataid}",groupId = "CENTIT", autoRefreshed = true)
public class ServiceConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@Autowired Environment environment) {
        if (environment != null) {
            this.environment = environment;
        }
    }

    @Bean
    public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor(){
        return new AutowiredAnnotationBeanPostProcessor();
    }

    /**
     * 这个bean必须要有
     * @return CentitPasswordEncoder 密码加密算法
     */
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }

    @Bean
    public NotificationCenter notificationCenter(@Autowired PlatformEnvironment platformEnvironment) {
        EMailMsgPusher messageManager = new EMailMsgPusher();
        messageManager.setEmailServerHost("mail.centit.com");
        messageManager.setEmailServerPort(25);
        messageManager.setEmailServerUser("alertmail2@centit.com");
        messageManager.setEmailServerPwd(SecurityOptUtils.decodeSecurityString("cipher:o6YOHiUOg8jBZFkQtGW/9Q=="));
        messageManager.setUserEmailSupport(new SystemUserEmailSupport());
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        //notificationCenter.initDummyMsgSenders();
        notificationCenter.setPlatformEnvironment(platformEnvironment);
        notificationCenter.registerMessageSender("email", messageManager);
        notificationCenter.appointDefaultSendType("email");
        return notificationCenter;
    }

    @Bean
    public InstantiationServiceBeanPostProcessor instantiationServiceBeanPostProcessor() {
        return new InstantiationServiceBeanPostProcessor();
    }

    @Bean
    public ESServerConfig esServerConfig(){
        ESServerConfig config = new ESServerConfig();
        config.setServerHostIp(environment.getProperty("elasticsearch.server.ip"));
        config.setServerHostPort(environment.getProperty("elasticsearch.server.port"));
        config.setUsername(environment.getProperty("elasticsearch.server.username"));
        config.setPassword(environment.getProperty("elasticsearch.server.password"));
        config.setClusterName(environment.getProperty("elasticsearch.server.cluster"));
        config.setOsId(environment.getProperty("elasticsearch.osId"));
        config.setMinScore(NumberBaseOpt.parseFloat(environment.getProperty("elasticsearch.filter.minScore"), 0.5f));
        return config;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setUseCodeAsDefaultMessage(true);
        //"classpath:org/springframework/security/messages"
        ms.setBasenames("classpath:i18n/messages", "classpath:org/springframework/security/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
