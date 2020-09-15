package com.algoTrader.service.fix.filters;

import java.util.Date;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.TransactTime;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * Inserts the {@link TransactTime} field into an order if it's not currently
 * present
 * 
 */
public class TransactionTimeInsertMessageModifier implements MessageModifier {
	@Override
	public boolean modifyMessage(Message order, FIXMessageAugmentor augmentor) throws Exception {
		/** Only put the field in if it's not present */
		try {
			// test for presence
			order.getString(TransactTime.FIELD);
			return false;
		} catch (FieldNotFound ex) {
			if (augmentor.needsTransactTime(order)) {
				order.setField(new TransactTime(new Date())); // non-i18n
				return true;
			}
			return false;
		}

	}
}
