package com.centit.framework.dubbo.config;

import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.security.CentitUserDetailsService;
import com.centit.framework.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@ImportResource({"classpath:dubbo-ip-client.xml"})
public class IpServerDubboClientConfig {

    @Bean
    public CentitUserDetailsService centitUserDetailsService(
        @Autowired PlatformEnvironment platformEnvironment) {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setPlatformEnvironment(platformEnvironment);
        return userDetailsService;
    }
}
