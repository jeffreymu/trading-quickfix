package com.algoTrader.service.fix.quickfix.messagefactory;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * Dummy noop implementation of the {@link FIXMessageAugmentor}
 * 
 */

public class NoOpFIXMessageAugmentor implements FIXMessageAugmentor {
	public Message newOrderSingleAugment(Message inMessage) {
		return inMessage;
	}

	public Message executionReportAugment(Message inMessage) throws FieldNotFound {
		return inMessage;
	}

	public Message cancelRejectAugment(Message inMessage) {
		return inMessage;
	}

	public Message cancelReplaceRequestAugment(Message inMessage) {
		return inMessage;
	}

	public Message cancelRequestAugment(Message inMessage) {
		return inMessage;
	}

	public boolean needsTransactTime(Message inMsg) {
		return false;
	}

}
