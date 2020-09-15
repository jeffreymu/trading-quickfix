package com.algoTrader.service.fix.brokers;

import java.util.ArrayList;
import java.util.List;

import com.cjhxfund.algodb.entity.Order;
import com.cjhxfund.common.algo.api.ext.SecurityType;

/**
 * The in-memory representation of the selector.
 * 
 */

public class Selector {

	// INSTANCE DATA.

	private final Brokers mBrokers;
	private final SpringSelector mSpringSelector;
	private List<SelectorEntry> mEntries;
	private final BrokerID mDefaultBrokerID;

	// CONSTRUCTORS.

	/**
	 * Creates a new selector based on the given configuration.
	 * 
	 * @param springSelector
	 *            The configuration.
	 */

	public Selector(Brokers brokers, SpringSelector springSelector) {
		mBrokers = brokers;
		mSpringSelector = springSelector;
		if (getSpringSelector().getEntries() != null) {
			mEntries = new ArrayList<SelectorEntry>(getSpringSelector().getEntries().size());
			for (SpringSelectorEntry se : getSpringSelector().getEntries()) {
				mEntries.add(new SelectorEntry(se));
			}
		}
		if (getSpringSelector().getDefaultBroker() != null) {
			mDefaultBrokerID = new BrokerID(getSpringSelector().getDefaultBroker().getId());
		} else {
			mDefaultBrokerID = null;
		}
	}

	// INSTANCE METHODS.

	private Brokers getBrokers() {
		return mBrokers;
	}

	/**
	 * Returns the receiver's configuration.
	 * 
	 * @return The configuration.
	 */

	public SpringSelector getSpringSelector() {
		return mSpringSelector;
	}

	/**
	 * Returns the receiver's entries.
	 * 
	 * @return The entries. It may be null.
	 */

	public List<SelectorEntry> getEntries() {
		return mEntries;
	}

	/**
	 * Returns the receiver's default broker ID.
	 * 
	 * @return The ID. It may be null.
	 */

	public BrokerID getDefaultBroker() {
		return mDefaultBrokerID;
	}

	/**
	 * Returns the ID of the broker the receiver selects for the given order.
	 * 
	 * @param order
	 *            The order.
	 * 
	 * @return The ID of the selected broker, or null if the selector cannot
	 *         make a selection.
	 */

	public BrokerID chooseBroker(Order order) {
		// Broker was explicit.

		if (order.getBrokerID() != null && !order.getBrokerID().isEmpty()) {
			BrokerID bID = new BrokerID(order.getBrokerID());
			return bID;
		}

		// Search through entries (if any) for one that matches the
		// order type (provided the order has a known type).
		SecurityType orderType = order.getSecurityType();
		if ((orderType != null) && (orderType != SecurityType.Unknown) && (getEntries() != null)) {
			for (SelectorEntry e : getEntries()) {
				if (e.getSkipIfUnavailable() && !getBrokers().getBroker(e.getBroker()).getLoggedOn()) {
					continue;
				}
				if (e.getTargetType().equals(orderType)) {
					return e.getBroker();
				}
			}
		}

		// Return the default, if any.

		if (getDefaultBroker() != null) {
			return getDefaultBroker();
		}

		// No match.

		return null;
	}
}
