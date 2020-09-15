package com.algoTrader.service.fix.quickfix;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.NoRelatedSym;
import quickfix.field.SubscriptionRequestType;

/**
 * 
 * <p>
 * This class hierarchy translates <em>QuickFIX</em> messages to other formats.
 * This class specifically provides common routines to parse a <em>QuickFIX</em>
 * message.
 * 
 */
public abstract class AbstractMessageTranslator<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractMessageTranslator.class);

	/**
	 * Indicates what FIX version to use
	 */
	public static final FIXVersion sMessageVersion = FIXVersion.FIX44;
	/**
	 * Indicates what FIX factory to use
	 */
	public static final FIXMessageFactory sMessageFactory = sMessageVersion.getMessageFactory();

	/**
	 * Gets the <code>Symbol</code> specified in the given <code>Group</code>.
	 * 
	 * @param inGroup
	 *            a <code>Group</code> value
	 * @return an <code>Equity</code> value containing the symbol
	 * @throws Exception
	 *             if the symbol could not be extracted
	 */
	// public static Equity getSymbol(Group inGroup) throws Exception {
	// String securityType;
	// try {
	// securityType = inGroup.getString(SecurityType.FIELD);
	// } catch (FieldNotFound e) {
	// securityType = SecurityType.COMMON_STOCK;
	// }
	// Equity symbol;
	// try {
	// if (SecurityType.OPTION.equals(securityType) &&
	// inGroup.isSetField(NoUnderlyings.FIELD)) {
	// Group underlyingGroup =
	// sMessageFactory.createGroup(MsgType.MARKET_DATA_REQUEST,
	// NoUnderlyings.FIELD);
	// inGroup.getGroup(1, underlyingGroup);
	// symbol = new Equity(underlyingGroup.getString(UnderlyingSymbol.FIELD));
	// } else {
	// symbol = new Equity(inGroup.getString(Symbol.FIELD));
	// }
	// } catch (FieldNotFound e) {
	// throw new Exception(
	// "The system was unable to extract a symbol from the given FIX message.  Correct the FIX message and try again.",
	// e);
	// }
	// return symbol;
	// }

	/**
	 * Get all the {@link Group} objects associated with the given
	 * <code>Message</code>.
	 * 
	 * @param inMessage
	 *            a <code>Message</code> value
	 * @return a <code>List&lt;Group&gt;</code> value
	 * @throws Exception
	 */
	public static List<Group> getGroups(Message inMessage) throws Exception {
		int totalSymbols = AbstractMessageTranslator.determineTotalSymbols(inMessage);
		List<Group> groups = new ArrayList<Group>();
		// the message header is valid, make a best effort to parse each group,
		// discarding as few as possible
		for (int symbolCounter = 1; symbolCounter <= totalSymbols; symbolCounter++) {
			try {
				Group relatedSymGroup = sMessageFactory.createGroup(MsgType.MARKET_DATA_REQUEST, NoRelatedSym.FIELD);
				inMessage.getGroup(symbolCounter, relatedSymGroup);
				// relatedSymGroup is a chunk in the message that encapsulates
				// the Symbol
				// securityType now holds a description of the symbol (fix tag
				// 167)
				groups.add(relatedSymGroup);
			} catch (Throwable e) {
				logger.debug("Group number " + symbolCounter + " could not be translated, skipping", e);
			}
		}

		return groups;
	}

	/**
	 * Retrieves the subscription type from the given message.
	 * 
	 * @param inMessage
	 *            a <code>Message</code> value
	 * @return a <code>char</code> value
	 */
	public static char determineSubscriptionRequestType(Message inMessage) {
		try {
			return inMessage.getChar(SubscriptionRequestType.FIELD);
		} catch (FieldNotFound e) {
			logger.warn("An error occurred trying to determine the FIX message''s requested subscription type, assuming no subscription.");
			return SubscriptionRequestType.SNAPSHOT;
		}
	}

	/**
	 * Retrieves the total number of symbols in the given message.
	 * 
	 * @param inMessage
	 *            a <code>Message</code> value
	 * @return an <code>int</code> value
	 * @throws Exception
	 *             if the number of symbols could not be determined
	 */
	public static int determineTotalSymbols(Message inMessage) throws Exception {
		try {
			return inMessage.getInt(NoRelatedSym.FIELD);
		} catch (FieldNotFound e) {
			throw new Exception(e);
		}
	}
}
