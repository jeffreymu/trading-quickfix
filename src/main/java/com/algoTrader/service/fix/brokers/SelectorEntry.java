package com.algoTrader.service.fix.brokers;

import com.cjhxfund.common.algo.api.ext.SecurityType;

/**
 * The in-memory representation of a selector entry.
 * 
 */

public class SelectorEntry {

	// INSTANCE DATA.

	private final SpringSelectorEntry mSpringSelectorEntry;
	private final SecurityType mTargetType;
	private final BrokerID mBrokerID;

	// CONSTRUCTORS.

	/**
	 * Creates a new entry based on the given configuration.
	 * 
	 * @param springSelectorEntry
	 *            The configuration.
	 */

	public SelectorEntry(SpringSelectorEntry springSelectorEntry) {
		mSpringSelectorEntry = springSelectorEntry;
		mTargetType = SecurityType.getInstanceForFIXValue(getSpringSelectorEntry().getTargetType());
		if (SecurityType.Unknown.equals(getTargetType())) {
			throw new RuntimeException("Security type " + getSpringSelectorEntry().getTargetType() + " is unknown");
		}
		mBrokerID = new BrokerID(getSpringSelectorEntry().getBroker().getId());
	}

	// INSTANCE METHODS.

	/**
	 * Returns the receiver's configuration.
	 * 
	 * @return The configuration.
	 */

	public SpringSelectorEntry getSpringSelectorEntry() {
		return mSpringSelectorEntry;
	}

	/**
	 * Returns the receiver's target type.
	 * 
	 * @return The target type, which will guaranteed to be known.
	 */

	public SecurityType getTargetType() {
		return mTargetType;
	}

	/**
	 * Returns the receiver's broker ID.
	 * 
	 * @return The ID.
	 */

	public BrokerID getBroker() {
		return mBrokerID;
	}

	/**
	 * Returns the receiver's skip-if-unavailable flag.
	 * 
	 * @return The flag.
	 */

	public boolean getSkipIfUnavailable() {
		return getSpringSelectorEntry().getSkipIfUnavailable();
	}
}
