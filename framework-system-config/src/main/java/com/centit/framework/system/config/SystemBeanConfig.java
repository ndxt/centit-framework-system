package com.centit.framework.system.config;

import com.centit.framework.model.security.CentitUserDetailsService;
import com.centit.framework.system.security.DaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@PropertySource("classpath:system.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SystemBeanConfig implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(@Autowired Environment environment) {
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
    public CentitUserDetailsService centitUserDetailsService() {
        DaoUserDetailsService userDetailsService = new DaoUserDetailsService();
        return userDetailsService;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

}
