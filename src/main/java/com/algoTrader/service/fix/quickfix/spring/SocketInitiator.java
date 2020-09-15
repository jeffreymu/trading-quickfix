package com.algoTrader.service.fix.quickfix.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionFactory;
import quickfix.SessionSettings;

/**
 * Straight subclass of {@link quickfix.SocketInitiator} to be used from Spring
 * config files that adds the {@link InitializingBean} and
 * {@link DisposableBean} behaviour.
 */
public class SocketInitiator extends quickfix.SocketInitiator implements InitializingBean, DisposableBean {

	public SocketInitiator(Application application, MessageStoreFactory messageStoreFactory, SessionSettings settings,
			LogFactory logFactory, MessageFactory messageFactory) throws ConfigError {
		super(application, messageStoreFactory, settings, logFactory, messageFactory);
	}

	public SocketInitiator(Application application, MessageStoreFactory messageStoreFactory, SessionSettings settings,
			MessageFactory messageFactory) throws ConfigError {
		super(application, messageStoreFactory, settings, messageFactory);
	}

	public SocketInitiator(SessionFactory sessionFactory, SessionSettings settings) throws ConfigError {
		super(sessionFactory, settings, 10000);
	}

	public void afterPropertiesSet() throws Exception {
		start();
	}

	public void destroy() throws Exception {
		stop(true);
	}

}
