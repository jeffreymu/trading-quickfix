package com.algoTrader.service.fix.quickfix;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.BeginSeqNo;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.CxlRejReason;
import quickfix.field.CxlRejResponseTo;
import quickfix.field.EndSeqNo;
import quickfix.field.HandlInst;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SecurityListRequestType;
import quickfix.field.SecurityReqID;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.SessionRejectReason;
import quickfix.field.TargetCompID;
import quickfix.field.Text;
import quickfix.field.TransactTime;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * Factory class that creates a particular beginString of the FIX message based
 * on the beginString specified. Uses an instance of the
 * {@link FIXMessageAugmentor} to add version-specific fields to each newly
 * created message.
 * 
 */
public class FIXMessageFactory {

	private MessageFactory msgFactory;
	private FIXMessageAugmentor msgAugmentor;
	private String beginString;
	/* package */static final char SOH_REPLACE_CHAR = '|';
	private static final char SOH_CHAR = '\001';

	public FIXMessageFactory(String beginString, MessageFactory inFactory, FIXMessageAugmentor augmentor) {
		this.beginString = beginString;
		msgFactory = inFactory;
		msgAugmentor = augmentor;
	}

	/**
	 * Creates a message representing an ExecutionReport (type
	 * {@link MsgType#ORDER_CANCEL_REJECT}
	 * 
	 * @return appropriately versioned message object
	 */
	public Message newOrderCancelReject() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REJECT);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	public Message newCancelReplaceShares(String orderID, String origOrderID, BigDecimal quantity) {
		Message aMessage = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REPLACE_REQUEST);
		addTransactionTimeIfNeeded(aMessage);
		aMessage.setField(new ClOrdID(orderID));
		aMessage.setField(new OrigClOrdID(origOrderID));
		aMessage.setField(new OrderQty(quantity.doubleValue()));
		addHandlingInst(aMessage);
		return aMessage;
	}

	protected void addHandlingInst(Message inMessage) {
		inMessage.setField(new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE));
	}

	public Message newCancelReplacePrice(String orderID, String origOrderID, BigDecimal price) {
		Message aMessage = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REPLACE_REQUEST);
		addTransactionTimeIfNeeded(aMessage);
		aMessage.setField(new ClOrdID(orderID));
		aMessage.setField(new OrigClOrdID(origOrderID));
		aMessage.setField(new Price(price.doubleValue()));
		addHandlingInst(aMessage);
		return aMessage;
	}

	public Message newCancelFromMessage(Message oldMessage) throws FieldNotFound {
		return newCancelHelper(MsgType.ORDER_CANCEL_REQUEST, oldMessage, false);
	}

	public Message newCancelReplaceFromMessage(Message oldMessage) throws FieldNotFound {
		Message cancelMessage = newCancelHelper(MsgType.ORDER_CANCEL_REPLACE_REQUEST, oldMessage, false);
		if (oldMessage.isSetField(Price.FIELD)) {
			cancelMessage.setField(oldMessage.getField(new Price()));
		}
		addHandlingInst(cancelMessage);
		return cancelMessage;
	}

	public Message newCancelHelper(String msgType, Message oldMessage, boolean onlyCopyRequiredFields)
			throws FieldNotFound {
		Message cancelMessage = msgFactory.create(beginString, msgType);
		cancelMessage.setField(new OrigClOrdID(oldMessage.getString(ClOrdID.FIELD)));
		fillFieldsFromExistingMessage(oldMessage, onlyCopyRequiredFields, cancelMessage);
		if (oldMessage.isSetField(OrderQty.FIELD)) {
			cancelMessage.setField(oldMessage.getField(new OrderQty()));
		}
		addTransactionTimeIfNeeded(cancelMessage);
		addSendingTime(cancelMessage);
		return cancelMessage;

	}

	protected void addSendingTime(Message inCancelMessage) {
		inCancelMessage.getHeader().setField(new SendingTime(new Date())); // non-i18n
	}

	protected void fillFieldsFromExistingMessage(Message oldMessage, boolean onlyCopyRequiredFields,
			Message inCancelMessage) {
		FIXMessageUtil.fillFieldsFromExistingMessage(inCancelMessage, oldMessage, onlyCopyRequiredFields);
	}

	@SuppressWarnings("unused")
	private final int TOP_OF_BOOK_DEPTH = 1;

	/**
	 * Generates a <code>Security List Request</code> FIX message.
	 * 
	 * @param inReqID
	 *            a <code>String</code> value containing a unique request ID
	 * @return a <code>Message</code> value
	 */
	public Message newSecurityListRequest(String inReqID) {
		FIXVersion thisVersion = FIXVersion.getFIXVersion(beginString);
		if (thisVersion.equals(FIXVersion.FIX43) || thisVersion.equals(FIXVersion.FIX44)) {
			Message request = msgFactory.create(beginString, MsgType.SECURITY_LIST_REQUEST);
			request.setField(new SecurityReqID(inReqID));
			request.setField(new SecurityListRequestType(SecurityListRequestType.SYMBOL));
			return request;
		}
		throw new IllegalStateException();
	}

	/**
	 * Generates a <code>Derivative Security List Request</code> FIX message.
	 * 
	 * @param inReqID
	 *            a <code>String</code> value containing a unique request ID
	 * @return a <code>Message</code> value
	 */
	public Message newDerivativeSecurityListRequest(String inReqID) {
		FIXVersion thisVersion = FIXVersion.getFIXVersion(beginString);
		if (thisVersion.equals(FIXVersion.FIX43) || thisVersion.equals(FIXVersion.FIX44)) {
			Message request = msgFactory.create(beginString, MsgType.DERIVATIVE_SECURITY_LIST_REQUEST);
			request.setField(new SecurityReqID(inReqID));
			request.setField(new SecurityListRequestType(SecurityListRequestType.SYMBOL));
			return request;
		}
		throw new IllegalStateException();
	}

	/**
	 * Creates a new BusinessMessageReject message based on passed-in parameters
	 */
	public Message newBusinessMessageReject(String refMsgType, int rejReason, String textReason) {
		Message bmReject = createMessage(MsgType.BUSINESS_MESSAGE_REJECT);
		bmReject.setField(new RefMsgType(refMsgType));
		bmReject.setField(new BusinessRejectReason(rejReason));
		bmReject.setField(new Text(textReason));
		return bmReject;
	}

	/**
	 * Creates a {@link MsgType#ORDER_CANCEL_REJECT} message
	 * 
	 * @param rejectReasonText
	 *            Text explanation for why reject is sent
	 */
	public Message newOrderCancelReject(OrderID orderID, ClOrdID clOrdID, OrigClOrdID origClOrdID,
			String rejectReasonText, CxlRejReason cxlRejReason) {
		Message reject = newOrderCancelReject();
		reject.setField(orderID);
		reject.setField(clOrdID);
		reject.setField(origClOrdID);
		reject.setField(new OrdStatus(OrdStatus.REJECTED));
		reject.setField(new CxlRejResponseTo(CxlRejResponseTo.ORDER_CANCEL_REQUEST));
		reject.setString(Text.FIELD, rejectReasonText.replace(SOH_CHAR, SOH_REPLACE_CHAR));
		if (cxlRejReason != null) {
			reject.setField(cxlRejReason);
		}

		return reject;
	}

	/**
	 * Creates a new order message and poopulates it with current
	 * {@link TransactTime}
	 * 
	 * @return new order single
	 */
	public Message newBasicOrder() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_SINGLE);
		addHandlingInst(msg);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	/** Creates a group based on the specified container message and group id */
	public Group createGroup(String msgType, int groupID) {
		return msgFactory.create(beginString, msgType, groupID);
	}

	/** Creates a message baed on the specified message type */
	public Message createMessage(String msgType) {
		Message msg = msgFactory.create(beginString, msgType);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	public String getBeginString() {
		return beginString;
	}

	/**
	 * Returns the underlying Quickfix/J {@link MessageFactory} that is used to
	 * create all messages.
	 */
	public MessageFactory getUnderlyingMessageFactory() {
		return msgFactory;
	}

	public FIXMessageAugmentor getMsgAugmentor() {
		return msgAugmentor;
	}

	/** Only add the transaction time if it's necessary for this message */
	public void addTransactionTimeIfNeeded(Message msg) {
		if (msgAugmentor.needsTransactTime(msg)) {
			msg.setField(new TransactTime(new Date())); // non-i18n
		}
	}

	public Message newResendRequest(BigInteger beginSeqNo, BigInteger endSeqNo) {
		Message rr = msgFactory.create(beginString, MsgType.RESEND_REQUEST);
		if (beginSeqNo == null) {
			// from 0
			rr.setField(new BeginSeqNo(0));
		} else {
			rr.setField(new StringField(BeginSeqNo.FIELD, beginSeqNo.toString()));// i18n_number
		}
		if (endSeqNo == null) {
			// to infinity
			rr.setField(new EndSeqNo(0));
		} else {
			rr.setField(new StringField(EndSeqNo.FIELD, endSeqNo.toString()));// i18n_number
		}
		return rr;
	}

	/**
	 * Creates a new session-level reject with the given reason to return to the
	 * sender of the incomingMsg
	 * 
	 * @param incomingMsg
	 *            Message that generated this session-level reject
	 * @param rejectReason
	 *            Reason for reject
	 * @return Session-level reject message to send out
	 * @throws FieldNotFound
	 * @throws SessionNotFound
	 */
	public Message createSessionReject(Message incomingMsg, int rejectReason) throws FieldNotFound, SessionNotFound {
		Message reply = createMessage(MsgType.REJECT);
		reverseRoute(incomingMsg, reply);
		String refSeqNum = incomingMsg.getHeader().getString(MsgSeqNum.FIELD);
		reply.setString(RefSeqNum.FIELD, refSeqNum);
		reply.setString(RefMsgType.FIELD, incomingMsg.getHeader().getString(MsgType.FIELD));
		reply.setInt(SessionRejectReason.FIELD, rejectReason);
		return reply;
	}

	/**
	 * Reverses the sender/target compIDs from reply and sets them in the
	 * outgoing outgoingMsg
	 */
	public void reverseRoute(Message outgoingMsg, Message reply) throws FieldNotFound {
		reply.getHeader().setString(SenderCompID.FIELD, outgoingMsg.getHeader().getString(TargetCompID.FIELD));
		reply.getHeader().setString(TargetCompID.FIELD, outgoingMsg.getHeader().getString(SenderCompID.FIELD));
	}

	public Message newCancelReplaceEmpty() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REPLACE_REQUEST);
		addTransactionTimeIfNeeded(msg);
		addHandlingInst(msg);
		return msg;
	}

	public Message newCancelEmpty() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REQUEST);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	public Message newOrderEmpty() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_SINGLE);
		addHandlingInst(msg);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	public Message newOrderCancelRejectEmpty() {
		Message msg = msgFactory.create(beginString, MsgType.ORDER_CANCEL_REJECT);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}

	public Message newExecutionReportEmpty() {
		Message msg = msgFactory.create(beginString, MsgType.EXECUTION_REPORT);
		addTransactionTimeIfNeeded(msg);
		return msg;
	}
}
