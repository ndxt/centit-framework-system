package com.centit.framework.hibernate.listener;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
/**
 * 
 * 代替Hibernate中的配置
 * 
 * @author codefan@sina.com
 * 2015年2月4日
 */
public class HibernateEventWiring {

    private SessionFactory sessionFactory;

    private PoDataChangesListener logListener;

    public void registerListeners() {
        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
                .getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(
                logListener);
        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(
                logListener);

        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(
                logListener);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(
                logListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(
                logListener);
    }
}
