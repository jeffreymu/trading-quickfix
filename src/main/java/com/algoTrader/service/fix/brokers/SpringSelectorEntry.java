package com.algoTrader.service.fix.brokers;

import org.springframework.beans.factory.InitializingBean;

/**
 * The Spring-based configuration of a selector entry.
 * 
 */

public class SpringSelectorEntry implements InitializingBean {

	// INSTANCE DATA.

	private String mTargetType;
	private SpringBroker mBroker;
	private boolean mSkipIfUnavailable;

	// INSTANCE METHODS.

	/**
	 * Sets the receiver's target type to the given string form value.
	 * 
	 * @param targetType
	 *            The target type.
	 */

	public void setTargetType(String targetType) {
		mTargetType = targetType;
	}

	/**
	 * Returns the receiver's target type, in string form.
	 * 
	 * @return The target type.
	 */

	public String getTargetType() {
		return mTargetType;
	}

	/**
	 * Sets the receiver's broker to the given one.
	 * 
	 * @param broker
	 *            The broker.
	 */

	public void setBroker(SpringBroker broker) {
		mBroker = broker;
	}

	/**
	 * Returns the receiver's broker.
	 * 
	 * @return The broker.
	 */

	public SpringBroker getBroker() {
		return mBroker;
	}

	/**
	 * Sets the receiver's skip-if-unavailable flag to the given value.
	 * 
	 * @param skipIfUnavailable
	 *            The flag.
	 */

	public void setSkipIfUnavailable(boolean skipIfUnavailable) {
		mSkipIfUnavailable = skipIfUnavailable;
	}

	/**
	 * Returns the receiver's skip-if-unavailable flag.
	 * 
	 * @return The flag.
	 */

	public boolean getSkipIfUnavailable() {
		return mSkipIfUnavailable;
	}

	// InitializingBean.

	@Override
	public void afterPropertiesSet() throws Exception {
		if (getTargetType() == null) {
			throw new Exception("No target type was set");
		}
		if (getBroker() == null) {
			throw new Exception("No broker was set");
		}
	}
}
