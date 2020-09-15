package com.algoTrader.service.fix.brokers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.algoTrader.service.fix.utils.SpringSessionDescriptor;
import com.algoTrader.service.fix.utils.SpringSessionSettings;

/**
 * The collective Spring-based configuration of all brokers.
 * 
 */

public class SpringBrokers implements InitializingBean {

	// INSTANCE DATA.

	private SpringSessionSettings mSessionSettings;
	private List<SpringBroker> mBrokers;

	// INSTANCE METHODS.

	/**
	 * Sets the configuration of the receiver's QuickFIX/J session settings to
	 * the given one.
	 * 
	 * @param sessionSettings
	 *            The configuration.
	 */

	public void setSettings(SpringSessionSettings sessionSettings) {
		mSessionSettings = sessionSettings;
	}

	/**
	 * Returns the configuration of the receiver's QuickFIX/J session settings.
	 * 
	 * @return The configuration.
	 */

	public SpringSessionSettings getSettings() {
		return mSessionSettings;
	}

	/**
	 * Sets the configurations of the receiver's brokers to the given ones.
	 * 
	 * @param brokers
	 *            The configurations.
	 */

	public void setBrokers(List<SpringBroker> brokers) {
		mBrokers = brokers;
	}

	/**
	 * Returns the configurations of the receiver's brokers.
	 * 
	 * @return The configurations.
	 */

	public List<SpringBroker> getBrokers() {
		return mBrokers;
	}

	// InitializingBean.

	@Override
	public void afterPropertiesSet() throws Exception {
		if (getSettings() == null) {
			throw new Exception("No setting were set");
		}
		if (getBrokers() == null) {
			throw new Exception("No brokers were set");
		}
		List<SpringSessionDescriptor> list = new ArrayList<SpringSessionDescriptor>(getBrokers().size());
		for (SpringBroker b : getBrokers()) {
			list.add(b.getDescriptor());
		}
		getSettings().setDescriptors(list);
	}
}
