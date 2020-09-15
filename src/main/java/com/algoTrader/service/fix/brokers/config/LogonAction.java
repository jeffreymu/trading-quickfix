package com.algoTrader.service.fix.brokers.config;

import com.algoTrader.service.fix.brokers.Broker;
import com.algoTrader.service.fix.quickfix.IQuickFIXSender;

/**
 * Handles logon actions to a broker.
 * 
 */
public interface LogonAction {
	/**
	 * Called upon successful logon to the given broker.
	 * 
	 * @param inBroker
	 *            a <code>Broker</code> value
	 * @param inSender
	 *            an <code>IQuickFIXSender</code> value
	 */
	public void onLogon(Broker inBroker, IQuickFIXSender inSender);
}
