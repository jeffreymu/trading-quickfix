package com.algoTrader.service.fix.brokers.config;

import com.algoTrader.service.fix.brokers.Broker;
import com.algoTrader.service.fix.quickfix.IQuickFIXSender;

/**
 * Handles logout actions to a broker.
 * 
 */
public interface LogoutAction {
	/**
	 * Called upon logout from the given broker.
	 * 
	 * @param inBroker
	 *            a <code>Broker</code> value
	 * @param inSender
	 *            an <code>IQuickFIXSender</code> value
	 */
	public void onLogout(Broker inBroker, IQuickFIXSender inSender);
}
