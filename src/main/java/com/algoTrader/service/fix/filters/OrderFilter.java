package com.algoTrader.service.fix.filters;

import quickfix.Message;

public interface OrderFilter {
	void assertAccepted(Message message) throws Exception;
}
