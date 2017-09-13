package com.centit.framework.web.demo.listener;

import com.centit.framework.components.OperationLogCenter;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.system.service.SysRoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by codefan on 17-7-6.
 */
public class InstantiationServiceBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent>
{
    @Autowired
    protected NotificationCenter notificationCenter;

    @Autowired(required = false)
    private OperationLogWriter optLogManager;

    @Autowired(required = false)
    private MessageSender innerMessageManager;

    @Autowired
    private SysRoleManager sysRoleManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        sysRoleManager.loadRoleSecurityMetadata();

        if(innerMessageManager!=null)
            notificationCenter.registerMessageSender("innerMsg", innerMessageManager);
        if(optLogManager!=null)
            OperationLogCenter.registerOperationLogWriter(optLogManager);
    }

}
