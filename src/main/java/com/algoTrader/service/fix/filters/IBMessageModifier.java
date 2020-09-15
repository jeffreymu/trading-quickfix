/**
 * 
 */
package com.algoTrader.service.fix.filters;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.MsgType;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * @author administrator
 * 
 */
public class IBMessageModifier implements MessageModifier {

	@Override
	public boolean modifyMessage(Message message, FIXMessageAugmentor augmentor) throws Exception {
		boolean modified = false;
		try {
			String msgType = message.getHeader().getString(MsgType.FIELD);
			if (msgType.equalsIgnoreCase("D")) {
				if (message.isSetField(Account.FIELD)) {
					message.removeField(Account.FIELD);
				}
			}
		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return modified;
	}

}
