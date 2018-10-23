package com.centit.framework.system.config;

import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.security.model.CentitSessionRegistry;
import com.centit.framework.security.model.CentitUserDetailsService;
import com.centit.framework.security.model.MemorySessionRegistryImpl;
import com.centit.framework.system.security.DaoUserDetailsService;
import com.centit.framework.system.service.impl.DBPlatformEnvironment;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.annotation.Resource;

@PropertySource("classpath:system.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SystemBeanConfig implements EnvironmentAware {

    private Environment env;

    @Resource
    @Override
    public void setEnvironment(Environment environment) {
        if(environment!=null) {
            this.env = environment;
        }
    }

    @Bean
    public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
        return new AutowiredAnnotationBeanPostProcessor();
    }

/*  这bean从框架中移除，由开发人员自行定义
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }
*/

    @Bean
    public PlatformEnvironment platformEnvironment(){
        DBPlatformEnvironment platformEnvironment = new DBPlatformEnvironment();
        return platformEnvironment;
    }

    @Bean
    public CentitUserDetailsService centitUserDetailsService() {
        DaoUserDetailsService userDetailsService = new DaoUserDetailsService();
        return userDetailsService;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    @Bean
    public CentitSessionRegistry centitSessionRegistry(){
        return new MemorySessionRegistryImpl();
    }

    /*
     * 缓存配置信息
     */
    /*@Bean
    public EhCacheManagerFactoryBean cacheManagerFactory() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }*/

}
