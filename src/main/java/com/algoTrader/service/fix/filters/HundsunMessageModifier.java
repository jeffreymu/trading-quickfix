/**
 * 
 */
package com.algoTrader.service.fix.filters;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.SenderSubID;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * @author administrator
 * 
 */
public class HundsunMessageModifier implements MessageModifier {

	private String senderSubID;

	public String getSenderSubID() {
		return senderSubID;
	}

	public void setSenderSubID(String senderSubID) {
		this.senderSubID = senderSubID;
	}

	@Override
	public boolean modifyMessage(Message message, FIXMessageAugmentor augmentor) throws Exception {
		boolean modified = false;

		try {
			String msgType = message.getHeader().getString(MsgType.FIELD);
			if (msgType.equalsIgnoreCase("D")) {
				message.setField(new SenderSubID(senderSubID));
				modified = true;
			}

		} catch (FieldNotFound e) {

		}

		return modified;
	}

}
