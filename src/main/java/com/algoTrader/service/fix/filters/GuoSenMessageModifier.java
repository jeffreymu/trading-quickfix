/**
 * 
 */
package com.algoTrader.service.fix.filters;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.StringField;
import quickfix.field.MsgType;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;
import com.google.common.base.Strings;

/**
 * @author administrator
 * 
 */
public class GuoSenMessageModifier implements MessageModifier {

	private String tag96;
	private String username;
	private String password;

	private final static int TAG96 = 96;
	private final static int TAG553 = 553;
	private final static int TAG554 = 554;

	public GuoSenMessageModifier() {
	}

	public String getTag96() {
		return tag96;
	}

	public void setTag96(String tag96) {
		this.tag96 = tag96;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean modifyMessage(Message message, FIXMessageAugmentor augmentor) throws Exception {
		String msgType = null;
		boolean modified = false;

		try {
			msgType = message.getHeader().getString(MsgType.FIELD);
			if (msgType.equalsIgnoreCase("A")) {
				if (!Strings.isNullOrEmpty(getTag96())) {
					message.setField(new StringField(TAG96, getTag96()));
				}
				if (!Strings.isNullOrEmpty(getUsername())) {
					message.setField(new StringField(TAG553, getUsername()));
				}
				if (!Strings.isNullOrEmpty(getPassword())) {
					message.setField(new StringField(TAG554, getPassword()));
				}
				modified = true;
			}
		} catch (FieldNotFound fieldNotFound) {
			// ignore
		}
		return modified;
	}
}
