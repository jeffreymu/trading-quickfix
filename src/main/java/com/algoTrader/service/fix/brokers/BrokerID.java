package com.algoTrader.service.fix.brokers;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlValue;

/* $License$ */
/**
 * Instances of this class uniquely identify a FIX Broker to which an order can
 * be sent or from which a response to a request can be received.
 * 
 */
public class BrokerID implements Serializable {

	/**
	 * Returns the text value of the ID.
	 * 
	 * @return the text value of the ID.
	 */
	public String getValue() {
		return mValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BrokerID that = (BrokerID) o;

		return mValue.equals(that.mValue);

	}

	@Override
	public int hashCode() {
		return mValue.hashCode();
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * Creates an instance.
	 * 
	 * @param inValue
	 *            the string ID value. Cannot be null.
	 */
	public BrokerID(String inValue) {
		if (inValue == null) {
			throw new NullPointerException();
		}
		mValue = inValue;
	}

	/**
	 * Creates a new ID. This empty constructor is intended for use by JAXB.
	 */

	protected BrokerID() {
		mValue = null;
	}

	@XmlValue
	private final String mValue;
	private static final long serialVersionUID = 1L;
}
