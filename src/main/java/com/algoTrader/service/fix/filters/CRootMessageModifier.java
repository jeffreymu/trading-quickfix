/**
 * 
 */
package com.algoTrader.service.fix.filters;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Currency;
import quickfix.field.HandlInst;
import quickfix.field.MsgType;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * @author administrator
 * 
 */
public class CRootMessageModifier implements MessageModifier {

	private char handlInstValue;

	public char getHandlInstValue() {
		return handlInstValue;
	}

	public void setHandlInstValue(char handlInstValue) {
		this.handlInstValue = handlInstValue;
	}

	@Override
	public boolean modifyMessage(Message message, FIXMessageAugmentor augmentor) throws Exception {
		boolean modified = false;
		try {
			String msgType = message.getHeader().getString(MsgType.FIELD);
			if (msgType.equalsIgnoreCase("D")) {
				if (!message.isSetField(Currency.FIELD)) {
					message.setField(new Currency("CNY"));
				}
				message.setField(new HandlInst(handlInstValue));
			}

		} catch (FieldNotFound e) {

		}

		return modified;
	}
}
