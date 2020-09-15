package com.algoTrader.service.fix.filters;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * Removes fields if present from FIX messages.
 * 
 */
public class FieldRemoverMessageModifier implements MessageModifier {
	private static Logger logger = LoggerFactory.getLogger(FieldRemoverMessageModifier.class);

	/**
	 * Create a new FieldRemoverMessageModifier instance.
	 */
	public FieldRemoverMessageModifier() {
		this(null);
	}

	/**
	 * Create a new FieldRemoverMessageModifier instance.
	 * 
	 * @param inField
	 *            a <code>String</code> value containing the field to remove in
	 *            the form <code>tag(msg-type)</code>
	 * @throws IllegalArgumentException
	 *             if the field specification is <code>null</code> or
	 *             non-conforming
	 */
	public FieldRemoverMessageModifier(String inField) {
		inField = StringUtils.trimToNull(inField);
		if (inField == null) {
			field = -1;
			msgType = null;
		} else {
			Validate.isTrue(messageDescriptor.matcher(inField).matches(),
					"Field specifier must be of the form tag(msg-type)");
			int splitIndex = inField.indexOf('(');
			// the field to remove is everything left of splitIndex
			field = Integer.parseInt(inField.substring(0, splitIndex));
			msgType = inField.substring(splitIndex + 1, inField.length() - 1);
		}
	}

	@Override
	public boolean modifyMessage(Message inMessage, FIXMessageAugmentor inAugmentor) throws Exception {
		boolean isModified = false;
		if (msgType != null && inMessage.isSetField(field)) {
			logger.debug("Message contains field " + field);
			if (msgType.equals(allMessageIndicator)) {
				logger.debug("Message type specifier is 'all messages', removing field");
				inMessage.removeField(field);
				isModified = true;
			} else {
				MsgType thisMessageType = new MsgType();
				try {
					inMessage.getHeader().getField(thisMessageType);
				} catch (FieldNotFound e) {
					throw new Exception(e);
				}
				if (thisMessageType.valueEquals(msgType)) {
					logger.debug("Message type specified matches message, removing field");
					inMessage.removeField(field);
					isModified = true;
				}
			}
		}
		if (isModified) {
			logger.debug("Modified message is " + inMessage);
		}
		return isModified;
	}

	/**
	 * Get the msgType value.
	 * 
	 * @return a <code>String</code> value
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * Get the field value.
	 * 
	 * @return a <code>int</code> value
	 */
	public int getField() {
		return field;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("MsgType", msgType).append("Field", field).toString();
	}

	/**
	 * FIX message code of the messages to modify
	 */
	private final String msgType;
	/**
	 * FIX message specifier of the field to remove
	 */
	private final int field;
	/**
	 * indicates that the field should be removed from all messages
	 */
	private static final String allMessageIndicator = "*"; //$NON-NLS-1$
	/**
	 * pattern used to identify conforming specifications
	 */
	private static final Pattern messageDescriptor = Pattern
			.compile("[0-9]{1,5}\\((\\" + allMessageIndicator + "?|[A-Z]{1,3})\\)"); //$NON-NLS-1$ //$NON-NLS-2$
}
