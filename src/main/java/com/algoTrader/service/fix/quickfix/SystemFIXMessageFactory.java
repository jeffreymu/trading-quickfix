package com.algoTrader.service.fix.quickfix;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.AvgPx;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecTransType;
import quickfix.field.ExecType;
import quickfix.field.HandlInst;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.field.TransactTime;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor_44;
import com.algoTrader.service.fix.quickfix.messagefactory.SystemMessageFactory;

/* $License$ */
/**
 * A message factory for creating {@link FIXVersion#FIX_SYSTEM System} FIX
 * Messages. Using this message factory ensures that we do not add any fields on
 * the messages that should not be added by a client application.
 * 
 */
public class SystemFIXMessageFactory extends FIXMessageFactory {
	/**
	 * Creates an instance.
	 */
	public SystemFIXMessageFactory() {
		super(FIXDataDictionary.FIX_SYSTEM_BEGIN_STRING, new SystemMessageFactory(), new FIXMessageAugmentor_44());
	}

	@Override
	public void addTransactionTimeIfNeeded(Message msg) {
		// Do not add transact time field.
	}

	@Override
	protected void addHandlingInst(Message inMessage) {
		// Do not add handling instruction field.
	}

	@Override
	protected void fillFieldsFromExistingMessage(Message oldMessage, boolean onlyCopyRequiredFields,
			Message inCancelMessage) {
		Set<Integer> inclusionSet = null;
		if (FIXMessageUtil.isCancelRequest(inCancelMessage)) {
			inclusionSet = ORDER_CANCEL_FIELDS;
		} else if (FIXMessageUtil.isCancelReplaceRequest(inCancelMessage)) {
			inclusionSet = ORDER_REPLACE_FIELDS;
		}
		FIXMessageUtil.fillFieldsFromExistingMessage(inCancelMessage, oldMessage, onlyCopyRequiredFields, inclusionSet);
	}

	@Override
	protected void addSendingTime(Message inCancelMessage) {
		// Do not add sending time.
	}

	public static final Set<Integer> ORDER_SINGLE_FIELDS;
	public static final Set<Integer> ORDER_CANCEL_FIELDS;
	public static final Set<Integer> ORDER_REPLACE_FIELDS;
	public static final Set<Integer> EXECUTION_REPORT_FIELDS;
	public static final Set<Integer> CANCEL_REPLACE_EXCLUSION_FIELDS;

	static {
		Set<Integer> tmp = new HashSet<Integer>();
		tmp.addAll(Arrays.asList(ClOrdID.FIELD, Account.FIELD, Text.FIELD, OrdType.FIELD, Price.FIELD, OrderQty.FIELD,
				quickfix.field.Side.FIELD, Symbol.FIELD, quickfix.field.SecurityType.FIELD,
				quickfix.field.TimeInForce.FIELD, quickfix.field.OrderCapacity.FIELD,
				quickfix.field.PositionEffect.FIELD));
		ORDER_SINGLE_FIELDS = Collections.unmodifiableSet(tmp);

		// Create the ER fields - everything that goes into OrderSingle, and
		// some fields that come back
		tmp = new HashSet<Integer>();
		tmp.addAll(Arrays.asList(ExecID.FIELD, ExecTransType.FIELD, ExecType.FIELD, OrdStatus.FIELD, LeavesQty.FIELD,
				CumQty.FIELD, LastShares.FIELD, LastPx.FIELD, OrderID.FIELD, OrigClOrdID.FIELD, HandlInst.FIELD,
				TransactTime.FIELD, AvgPx.FIELD));
		tmp.addAll(ORDER_SINGLE_FIELDS);
		EXECUTION_REPORT_FIELDS = Collections.unmodifiableSet(tmp);

		// create a CxR exclusion set - same as ER but preserving HandlInst
		HashSet<Integer> forCancelExclusion = new HashSet<Integer>(tmp);
		forCancelExclusion.remove(HandlInst.FIELD);
		CANCEL_REPLACE_EXCLUSION_FIELDS = Collections.unmodifiableSet(forCancelExclusion);

		// list of fields for cancel
		tmp = new HashSet<Integer>();
		tmp.addAll(Arrays.asList(ClOrdID.FIELD, quickfix.field.OrderID.FIELD, Account.FIELD, Text.FIELD,
				OrigClOrdID.FIELD, OrderQty.FIELD, quickfix.field.Side.FIELD, Symbol.FIELD,
				quickfix.field.SecurityType.FIELD));
		ORDER_CANCEL_FIELDS = Collections.unmodifiableSet(tmp);
		tmp = new HashSet<Integer>();
		tmp.addAll(Arrays.asList(ClOrdID.FIELD, quickfix.field.OrderID.FIELD, Account.FIELD, Text.FIELD, OrdType.FIELD,
				OrigClOrdID.FIELD, Price.FIELD, OrderQty.FIELD, quickfix.field.Side.FIELD, Symbol.FIELD,
				quickfix.field.SecurityType.FIELD, quickfix.field.TimeInForce.FIELD,
				quickfix.field.OrderCapacity.FIELD, quickfix.field.PositionEffect.FIELD));
		ORDER_REPLACE_FIELDS = Collections.unmodifiableSet(tmp);
	}
}
