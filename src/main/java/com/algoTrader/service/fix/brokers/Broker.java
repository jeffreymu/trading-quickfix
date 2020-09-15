package com.algoTrader.service.fix.brokers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.DataDictionary;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.fix42.Heartbeat;

import com.algoTrader.service.fix.filters.MessageModifierManager;
import com.algoTrader.service.fix.filters.MessageRouteManager;
import com.algoTrader.service.fix.quickfix.FIXDataDictionary;
import com.algoTrader.service.fix.quickfix.FIXMessageFactory;
import com.algoTrader.service.fix.quickfix.FIXVersion;
import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;
import com.algoTrader.service.fix.utils.AnalyzedMessage;
import com.cjhxfund.algo.fix.FIXApplication;

/**
 * The in-memory representation of a single broker.
 * 
 */

public class Broker {

	private static Logger logger = LoggerFactory.getLogger(FIXApplication.class);

	// INSTANCE DATA.

	private final SpringBroker mSpringBroker;

	private final BrokerID mBrokerID;

	private FIXDataDictionary mDataDictionary;

	private boolean mLoggedOn;

	// HeartBeat
	private Heartbeat lastHeartBeat = null;

	// CONSTRUCTORS.

	public Heartbeat getLastHeartBeat() {
		return lastHeartBeat;
	}

	public void setLastHeartBeat(Heartbeat lastHeartBeat) {
		this.lastHeartBeat = lastHeartBeat;
	}

	/**
	 * Creates a new broker based on the given configuration. Its message
	 * modifiers are configured to rely on the given report history services
	 * provider for persistence operations.
	 * 
	 * @param springBroker
	 *            The configuration.
	 */

	public Broker(SpringBroker springBroker) {
		mSpringBroker = springBroker;
		mBrokerID = new BrokerID(getSpringBroker().getId());
	}

	// INSTANCE METHODS.

	/**
	 * Returns the receiver's configuration.
	 * 
	 * @return The configuration.
	 */

	public SpringBroker getSpringBroker() {
		return mSpringBroker;
	}

	/**
	 * Returns the receiver's status.
	 * 
	 * @return The status.
	 */

	public BrokerStatus getStatus() {
		return new BrokerStatus(getName(), getBrokerID(), getLoggedOn());
	}

	/**
	 * Returns the receiver's name.
	 * 
	 * @return The name.
	 */

	public String getName() {
		return getSpringBroker().getName();
	}

	/**
	 * Returns the receiver's broker ID.
	 * 
	 * @return The ID.
	 */

	public BrokerID getBrokerID() {
		return mBrokerID;
	}

	/**
	 * Returns the receiver's QuickFIX/J session ID.
	 * 
	 * @return The ID.
	 */

	public SessionID getSessionID() {
		return getSpringBroker().getDescriptor().getQSessionID();
	}

	/**
	 * Returns the receiver's QuickFIX/J session.
	 * 
	 * @return The session.
	 */

	public Session getSession() {
		return Session.lookupSession(getSessionID());
	}

	/**
	 * Returns the receiver's QuickFIX/J data dictionary.
	 * 
	 * @return The dictionary.
	 */

	public DataDictionary getDataDictionary() {
		return getSession().getDataDictionary();
	}

	/**
	 * Returns the receiver's message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getModifiers() {
		return getSpringBroker().getModifiers();
	}

	/**
	 * Returns the receiver's route manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageRouteManager getRoutes() {
		return getSpringBroker().getRoutes();
	}

	/**
	 * Returns the receiver's pre-sending message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getPreSendModifiers() {
		return getSpringBroker().getPreSendModifiers();
	}

	/**
	 * Returns the receiver's response message modifier manager.
	 * 
	 * @return The manager. It may be null.
	 */

	public MessageModifierManager getResponseModifiers() {
		return getSpringBroker().getResponseModifiers();
	}

	/**
	 * Returns the receiver's FIX version.
	 * 
	 * @return The version.
	 */

	public FIXVersion getFIXVersion() {
		return FIXVersion.getFIXVersion(getSessionID().getBeginString());
	}

	/**
	 * Returns the receiver's FIX message factory.
	 * 
	 * @return The factory.
	 */

	public FIXMessageFactory getFIXMessageFactory() {
		return getFIXVersion().getMessageFactory();
	}

	/**
	 * Returns the receiver's FIX data dictionary.
	 * 
	 * @return The dictionary.
	 */

	public synchronized FIXDataDictionary getFIXDataDictionary() {
		if (mDataDictionary == null) {
			mDataDictionary = new FIXDataDictionary(getDataDictionary());
		}
		return mDataDictionary;
	}

	/**
	 * Returns the receiver's FIX message augmentor.
	 * 
	 * @return The augmentor.
	 */

	public FIXMessageAugmentor getFIXMessageAugmentor() {
		return getFIXMessageFactory().getMsgAugmentor();
	}

	/**
	 * Sets the receiver's logon flag to the given value. This method is
	 * synchronized to ensure that all threads will see the most up-to-date
	 * value for the flag.
	 * 
	 * @param loggedOn
	 *            The flag.
	 */

	public synchronized void setLoggedOn(boolean loggedOn) {
		mLoggedOn = loggedOn;
	}

	/**
	 * Returns the receiver's logon flag. This method is synchronized to ensure
	 * that all threads will see the most up-to-date value for the flag.
	 * 
	 * @return The flag.
	 */

	public synchronized boolean getLoggedOn() {
		return mLoggedOn;
	}

	/**
	 * Logs the given message, analyzed using the receiver's data dictionary, at
	 * the debugging level.
	 * 
	 * @param msg
	 *            The message.
	 */

	public void logMessage(Message msg) {
		logger.debug(new AnalyzedMessage(getDataDictionary(), msg).toString());
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(getBrokerID().getValue()).append(", Name: ").append(getName()).append(", Session: ")
				.append(getSessionID());
		return sb.toString();
	}
}
