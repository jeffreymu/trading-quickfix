package com.algoTrader.service.fix.filters;

import java.util.Set;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;

public class MessageTypeFilter implements MessageFilter {
	private Set<String> mAccepted;
	private boolean mAcceptUnknown;

	public void setAcceptedMessages(Set<String> accepted) {
		mAccepted = accepted;
	}

	public Set<String> getAcceptedMessages() {
		return mAccepted;
	}

	public void setAcceptUnknown(boolean acceptUnknown) {
		mAcceptUnknown = acceptUnknown;
	}

	public boolean getAcceptUnknown() {
		return mAcceptUnknown;
	}

	@Override
	public boolean isAccepted(Message message) {
		try {
			return getAcceptedMessages().contains(message.getHeader().getString(MsgType.FIELD));
		} catch (FieldNotFound ex) {
			return getAcceptUnknown();
		}
	}
}
