package com.algoTrader.service.fix.filters;

import quickfix.Message;

public class MessageFilterNoop implements MessageFilter {
	@Override
	public boolean isAccepted(Message message) {
		return true;
	}
}
