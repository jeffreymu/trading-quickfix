package com.algoTrader.service.fix.quickfix.messagefactory;

import java.util.Arrays;

import quickfix.Message;
import quickfix.field.MsgType;

public class FIXMessageAugmentor_42 extends FIXMessageAugmentor_41 {
	private static String[] TT_APPLICABLE_MESSAGE_CODES = new String[] { MsgType.ORDER_CANCEL_REJECT,
			MsgType.ORDER_SINGLE, MsgType.ORDER_CANCEL_REQUEST, MsgType.ORDER_CANCEL_REPLACE_REQUEST,
			MsgType.LIST_CANCEL_REQUEST, MsgType.LIST_EXECUTE, MsgType.LIST_STATUS, MsgType.SECURITY_STATUS,
			MsgType.ORDER_LIST, MsgType.QUOTE_REQUEST, MsgType.MASS_QUOTE };

	@Override
	public Message newOrderSingleAugment(Message inMessage) {
		return inMessage;
	}

	@Override
	public Message cancelReplaceRequestAugment(Message inMessage) {
		return inMessage;
	}

	public FIXMessageAugmentor_42() {
		applicableMsgTypes.addAll(Arrays.asList(TT_APPLICABLE_MESSAGE_CODES));
	}
}
