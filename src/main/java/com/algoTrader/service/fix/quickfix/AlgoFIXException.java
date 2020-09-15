package com.algoTrader.service.fix.quickfix;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * Exception with multiple constructors designed to handle and create messages
 * for various FIX-related errors
 * 
 */
public class AlgoFIXException extends Exception {
	private static final long serialVersionUID = -2532357335856882275L;

	public AlgoFIXException(String message) {
		super(message);
	}

	public AlgoFIXException(Throwable nested) {
		super(nested);
	}

	public AlgoFIXException(String message, Throwable nested) {
		super(message, nested);
	}

	public static AlgoFIXException createFieldNotFoundException(FieldNotFound fnf) {
		String fieldName = CurrentFIXDataDictionary.getCurrentFIXDataDictionary().getHumanFieldName(fnf.field);
		return new AlgoFIXException("Could not find field " + fieldName + " in message.", fnf);
	}

	public static AlgoFIXException createFieldNotFoundException(FieldNotFound fnf, Message message) {
		String fieldName = CurrentFIXDataDictionary.getCurrentFIXDataDictionary().getHumanFieldName(fnf.field);
		return new AlgoFIXException("Could not find field " + fieldName + " in message: " + message, fnf);
	}
}
