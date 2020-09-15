package com.algoTrader.service.fix.quickfix;

import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;

/**
 * Marker class for objects that send out FIX messages Essentially used for
 * classes that need to be subclassed in unit tests when we want to just capture
 * the message instead of sending it out.
 * 
 */

public class FIXMessageSender {
	/** To be overridden by unit tests for capturing outgoing messages */
	public void sendOutgoingMessage(Message inMsg, SessionID targetID) throws SessionNotFound {
		Session.sendToTarget(inMsg, targetID);
	}

}
