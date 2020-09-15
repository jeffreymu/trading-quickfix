package com.algoTrader.service.fix.quickfix;

/**
 * An unsupported QuickFix message was received.
 * 
 */
public class UnsupportedMessageException extends Exception {
	private static final long serialVersionUID = -5489749428430923539L;

	/**
	 * Create a new UnsupportedMessageException instance.
	 * 
	 * @param inMessage
	 *            an <code>I18NBoundMessage</code> value
	 */
	public UnsupportedMessageException(String inMessage) {
		super(inMessage);
	}

	/**
	 * Create a new UnsupportedMessageException instance.
	 * 
	 * @param inNested
	 *            a <code>Throwable</code> value
	 */
	public UnsupportedMessageException(Throwable inNested) {
		super(inNested);
	}

}
