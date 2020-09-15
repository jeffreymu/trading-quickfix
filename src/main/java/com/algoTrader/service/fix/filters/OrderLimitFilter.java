package com.algoTrader.service.fix.filters;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Symbol;

import com.algoTrader.service.fix.quickfix.FIXMessageUtil;

public class OrderLimitFilter implements OrderFilter {
	private static Logger logger = LoggerFactory.getLogger(OrderLimitFilter.class);

	private boolean mDisallowMarketOrders;
	private BigDecimal mMaxQuantityPerOrder;
	private BigDecimal mMaxNotionalPerOrder;
	private BigDecimal mMinPrice;
	private BigDecimal mMaxPrice;

	public void setDisallowMarketOrders(boolean disallowMarketOrders) {
		mDisallowMarketOrders = disallowMarketOrders;
	}

	public boolean getDisallowMarketOrders() {
		return mDisallowMarketOrders;
	}

	public void setMaxQuantityPerOrder(BigDecimal maxQuantityPerOrder) {
		mMaxQuantityPerOrder = maxQuantityPerOrder;
	}

	public BigDecimal getMaxQuantityPerOrder() {
		return mMaxQuantityPerOrder;
	}

	public void setMaxNotionalPerOrder(BigDecimal maxNotionalPerOrder) {
		mMaxNotionalPerOrder = maxNotionalPerOrder;
	}

	public BigDecimal getMaxNotionalPerOrder() {
		return mMaxNotionalPerOrder;
	}

	public void setMinPrice(BigDecimal minPrice) {
		mMinPrice = minPrice;
	}

	public BigDecimal getMinPrice() {
		return mMinPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		mMaxPrice = maxPrice;
	}

	public BigDecimal getMaxPrice() {
		return mMaxPrice;
	}

	@Override
	public void assertAccepted(Message msg) throws Exception {
		if (!FIXMessageUtil.isOrderSingle(msg) && !FIXMessageUtil.isCancelReplaceRequest(msg)) {
			return;
		}
		String symbol = null;
		try {
			symbol = msg.getString(Symbol.FIELD);
		} catch (FieldNotFound ex) {
			logger.warn("Symbol is missing", ex);
			// Later exceptions will use null symbol.
		}
		try {
			if (getDisallowMarketOrders() && (OrdType.MARKET == msg.getChar(OrdType.FIELD))) {
				throw new Exception("Market orders are not allowed for " + symbol);
			}
		} catch (FieldNotFound ex) {
			logger.warn("Order type is missing", ex);
		}
		BigDecimal p = null;
		try {
			p = new BigDecimal(msg.getString(Price.FIELD));
		} catch (FieldNotFound ex) {
			logger.warn("Price is missing", ex);
		}
		BigDecimal q = null;
		try {
			q = new BigDecimal(msg.getString(OrderQty.FIELD));
		} catch (FieldNotFound ex) {
			logger.warn("Quantity is missing", ex);
		}
		if ((p == null) && (q == null)) {
			return;
		}
		if ((q != null) && (getMaxQuantityPerOrder() != null) && (getMaxQuantityPerOrder().compareTo(q) < 0)) {
			throw new Exception("Quantity of " + q + " exceeds order limit of " + getMaxQuantityPerOrder()
					+ " max quantity for " + symbol);
		}
		if ((p != null) && (q != null) && (getMaxNotionalPerOrder() != null)) {
			BigDecimal n = p.multiply(q);
			if (getMaxNotionalPerOrder().compareTo(n) < 0) {
				throw new Exception("Notional of " + n + " exceeds order limit of " + getMaxNotionalPerOrder()
						+ " max notional for " + symbol);
			}
		}
		if ((p != null) && (getMinPrice() != null) && (getMinPrice().compareTo(p) > 0)) {
			throw new Exception("Price of " + p + " is below the order limit of " + getMinPrice() + " min price for "
					+ symbol);
		}
		if ((p != null) && (getMaxPrice() != null) && (getMaxPrice().compareTo(p) < 0)) {
			throw new Exception("Price of " + p + " is above order limit of " + getMaxPrice() + " max price for "
					+ symbol);
		}
	}
}
