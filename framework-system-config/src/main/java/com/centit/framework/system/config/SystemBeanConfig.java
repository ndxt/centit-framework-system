package com.centit.framework.system.config;

import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.security.CentitUserDetailsService;
import com.centit.framework.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SystemBeanConfig implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(@Autowired Environment environment) {
        if(environment!=null) {
            this.env = environment;
        }
    }

/*  这bean从框架中移除，由开发人员自行定义
    @Bean("passwordEncoder")
    public StandardPasswordEncoderImpl passwordEncoder() {
        return  new StandardPasswordEncoderImpl();
    }
*/
    @Bean
    public CentitUserDetailsService centitUserDetailsService(@Autowired PlatformEnvironment platformEnvironment) {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setPlatformEnvironment(platformEnvironment);
        return userDetailsService;
    }

}
