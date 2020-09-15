package com.algoTrader.service.fix.filters;

import java.util.LinkedList;
import java.util.List;

import quickfix.Message;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * Object that encapsulates a collection of message modifiers and provides
 * functions to apply all the modifiers to a particular message.
 * 
 */

public class MessageModifierManager {
	private List<MessageModifier> messageModifiers;

	public void setMessageModifiers(List<MessageModifier> mods) {
		messageModifiers = new LinkedList<MessageModifier>();
		for (MessageModifier mod : mods) {
			messageModifiers.add(mod);
		}
		messageModifiers.add(new TransactionTimeInsertMessageModifier());
	}

	/** Apply all the order modifiers to this message */
	public void modifyMessage(Message inMessage, FIXMessageAugmentor augmentor) throws Exception {
		for (MessageModifier oneModifier : messageModifiers) {
			oneModifier.modifyMessage(inMessage, augmentor);
		}
	}
}
