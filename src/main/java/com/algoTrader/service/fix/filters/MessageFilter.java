package com.algoTrader.service.fix.filters;

import quickfix.Message;

public interface MessageFilter {
	boolean isAccepted(Message message);
}
