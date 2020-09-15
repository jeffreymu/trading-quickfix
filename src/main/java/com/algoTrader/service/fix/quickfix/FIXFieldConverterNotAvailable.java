package com.algoTrader.service.fix.quickfix;

/**
 * Throws when the {@link FIXDataDictionary} is not found for the specified
 * version of FIX Protocol
 * 
 */
public class FIXFieldConverterNotAvailable extends Exception {
	private static final long serialVersionUID = -6676153748962599532L;

	public FIXFieldConverterNotAvailable(Throwable nested) {
		super(nested);
	}

	public FIXFieldConverterNotAvailable(String message) {
		super(message);
	}

	public FIXFieldConverterNotAvailable(String message, Throwable nested) {
		super(message, nested);
	}

}
