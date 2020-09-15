package com.algoTrader.service.fix.filters;

import quickfix.Message;

public class OrderFilterNoop implements OrderFilter {
	@Override
	public void assertAccepted(Message message) {
	}
}
