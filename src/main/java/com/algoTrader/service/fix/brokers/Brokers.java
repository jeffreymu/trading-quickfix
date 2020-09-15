package com.algoTrader.service.fix.brokers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.SessionID;

import com.algoTrader.service.fix.utils.SpringSessionSettings;
import com.cjhxfund.algo.fix.FIXApplication;

/**
 * The collective in-memory representation of all brokers.
 * 
 */

public class Brokers {
	private static Logger logger = LoggerFactory.getLogger(FIXApplication.class);

	// INSTANCE DATA.

	private final SpringBrokers mSpringBrokers;
	private final List<Broker> mBrokers;
	private final Map<BrokerID, Broker> mBrokerIDMap;
	private final Map<SessionID, Broker> mSessionIDMap;

	// CONSTRUCTORS.

	/**
	 * Creates a new collective representation based on the given broker
	 * configurations. Any message modifiers are configured to rely on the given
	 * report history services provider for persistence operations.
	 * 
	 * @param springBrokers
	 *            The configurations.
	 */

	public Brokers(SpringBrokers springBrokers) {
		mSpringBrokers = springBrokers;
		int capacity = getSpringBrokers().getBrokers().size();
		mBrokers = new ArrayList<Broker>(capacity);
		mBrokerIDMap = new HashMap<BrokerID, Broker>(capacity);
		mSessionIDMap = new HashMap<SessionID, Broker>(capacity);
		for (SpringBroker sb : getSpringBrokers().getBrokers()) {
			Broker b = new Broker(sb);
			mBrokers.add(b);
			mBrokerIDMap.put(b.getBrokerID(), b);
			mSessionIDMap.put(b.getSessionID(), b);
		}
	}

	// INSTANCE METHODS.

	/**
	 * Returns the receiver's broker configurations.
	 * 
	 * @return The configurations.
	 */

	public SpringBrokers getSpringBrokers() {
		return mSpringBrokers;
	}

	/**
	 * Returns the receiver's brokers.
	 * 
	 * @return The brokers.
	 */

	public List<Broker> getBrokers() {
		return mBrokers;
	}

	/**
	 * Returns the status of the receiver's brokers.
	 * 
	 * @return The status.
	 */

	public BrokersStatus getStatus() {
		List<BrokerStatus> list = new ArrayList<BrokerStatus>(getBrokers().size());
		for (Broker b : getBrokers()) {
			list.add(b.getStatus());
		}
		return new BrokersStatus(list);
	}

	/**
	 * Returns the configuration of the receiver's QuickFIX/J session settings.
	 * 
	 * @return The configuration.
	 */

	public SpringSessionSettings getSettings() {
		return getSpringBrokers().getSettings();
	}

	/**
	 * Returns the receiver's broker for the given QuickFIX/J session ID. It
	 * logs an error and returns null if there is no broker for the given ID.
	 * 
	 * @param sessionID
	 *            The ID.
	 * 
	 * @return The broker. It may be null.
	 */

	public Broker getBroker(SessionID sessionID) {
		Broker b = mSessionIDMap.get(sessionID);
		if (b == null) {
			logger.error("Session ID " + sessionID + " is invalid");
		}
		return b;
	}

	/**
	 * Returns the receiver's broker for the given broker ID. It logs an error
	 * and returns null if there is no broker for the given ID.
	 * 
	 * @param brokerID
	 *            The ID.
	 * 
	 * @return The broker. It may be null.
	 */

	public Broker getBroker(BrokerID brokerID) {
		Broker b = mBrokerIDMap.get(brokerID);
		if (b == null) {
			logger.error("Broker ID " + brokerID + " is invalid");
		}
		return b;
	}
}
