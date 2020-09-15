package com.algoTrader.service.fix.brokers;

import java.util.List;

/**
 * The collective web service representation of the status of all brokers.
 * 
 */

public class BrokersStatus {

	// INSTANCE DATA.

	private final List<BrokerStatus> mBrokers;

	// CONSTRUCTORS.

	/**
	 * Creates a new collective status representation, given the status of the
	 * brokers.
	 * 
	 * @param brokers
	 *            The status.
	 */

	public BrokersStatus(List<BrokerStatus> brokers) {
		mBrokers = brokers;
	}

	/**
	 * Creates a new collective status representation. This empty constructor is
	 * intended for use by JAXB.
	 */

	protected BrokersStatus() {
		mBrokers = null;
	}

	// INSTANCE METHODS.

	/**
	 * Returns the status of the receiver's brokers. The returned list is not
	 * modifiable.
	 * 
	 * @return The status.
	 */

	public List<BrokerStatus> getBrokers() {
		return mBrokers;
	}
}
