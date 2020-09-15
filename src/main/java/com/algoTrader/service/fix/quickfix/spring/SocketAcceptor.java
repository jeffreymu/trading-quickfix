package com.algoTrader.service.fix.quickfix.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionFactory;

/**
 * Wrapper around the @{link quickfix.SocketAcceptor} to be used for creation
 * via Spring that adds the @{link InitializingBean} and @{link DisposableBean}
 * behaviour.
 * 
 */
public class SocketAcceptor extends quickfix.SocketAcceptor implements InitializingBean, DisposableBean {

	public SocketAcceptor(Application application, MessageStoreFactory messageStoreFactory,
			quickfix.SessionSettings settings, LogFactory logFactory, MessageFactory messageFactory) throws ConfigError {
		super(application, messageStoreFactory, settings, logFactory, messageFactory);
	}

	public SocketAcceptor(SessionFactory sessionFactory, SessionSettings settings) throws ConfigError {
		super(sessionFactory, settings);
	}

	public void afterPropertiesSet() throws Exception {
		start();
	}

	public void destroy() throws Exception {
		stop(true);
	}
}
