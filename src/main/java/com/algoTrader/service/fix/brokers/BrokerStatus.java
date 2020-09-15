package com.algoTrader.service.fix.brokers;

import java.io.Serializable;

/**
 * The web service representation of a single broker's status.
 * 
 */

public class BrokerStatus implements Serializable {

	// CLASS DATA.

	private static final long serialVersionUID = 1L;

	// INSTANCE DATA.

	private final String mName;
	private final BrokerID mId;
	private final boolean mLoggedOn;

	// CONSTRUCTORS.

	/**
	 * Creates a new status representation, given the broker information.
	 * 
	 * @param name
	 *            The broker name.
	 * @param id
	 *            The broker ID.
	 * @param loggedOn
	 *            The logon flag.
	 */

	public BrokerStatus(String name, BrokerID id, boolean loggedOn) {
		mName = name;
		mId = id;
		mLoggedOn = loggedOn;
	}

	/**
	 * Creates a new status representation. This empty constructor is intended
	 * for use by JAXB.
	 */

	protected BrokerStatus() {
		mName = null;
		mId = null;
		mLoggedOn = false;
	}

	// INSTANCE METHODS.

	/**
	 * Returns the receiver's name.
	 * 
	 * @return The name.
	 */

	public String getName() {
		return mName;
	}

	/**
	 * Returns the receiver's broker ID.
	 * 
	 * @return The ID.
	 */

	public BrokerID getId() {
		return mId;
	}

	/**
	 * Returns the receiver's logon flag.
	 * 
	 * @return The flag.
	 */

	public boolean getLoggedOn() {
		return mLoggedOn;
	}

	// Object.

	@Override
	public String toString() {
		return String.format("Broker: %s(%s,%s)", //$NON-NLS-1$
				String.valueOf(getName()), String.valueOf(getId()), getLoggedOn());
	}
}
