package com.algoTrader.service.fix.quickfix;

import java.io.InputStream;

import quickfix.ConfigError;
import quickfix.DataDictionary;

/**
 * Converts the standard FIX field (integers) to their english names This is
 * mostly for better output/debugging purposes since we don't want to memorize
 * field numbers
 * 
 */
public class FIXDataDictionary {
	public static final String FIX_SYSTEM_BEGIN_STRING = "FIX.0.0"; //$NON-NLS-1$
	public static final String FIX_4_0_BEGIN_STRING = "FIX.4.0"; //$NON-NLS-1$
	public static final String FIX_4_1_BEGIN_STRING = "FIX.4.1"; //$NON-NLS-1$
	public static final String FIX_4_2_BEGIN_STRING = "FIX.4.2"; //$NON-NLS-1$
	public static final String FIX_4_3_BEGIN_STRING = "FIX.4.3"; //$NON-NLS-1$
	public static final String FIX_4_4_BEGIN_STRING = "FIX.4.4"; //$NON-NLS-1$

	// used for passing strategy name in the fix messages between client and ors
	public static final int STRATEGY_TAG = 7888;
	/**
	 * added fix-tag for request & response msgs bounded with data feed in MDF
	 */
	public static final int DATAPROVIDER_TAG = 7889;

	public static final int ORS_REJECT = 7887;

	private final DataDictionary mDictionary;

	public FIXDataDictionary(DataDictionary dictionary) {
		mDictionary = dictionary;
	}

	/**
	 * Load a {@link DataDictionary} from the specified resource
	 * 
	 * @param fixDataDictionaryPath
	 *            Path to the location of the data dictionary file
	 * @throws FIXFieldConverterNotAvailable
	 */
	public FIXDataDictionary(String fixDataDictionaryPath) throws FIXFieldConverterNotAvailable {
		DataDictionary theDict;
		try {
			theDict = new DataDictionary(fixDataDictionaryPath);
		} catch (DataDictionary.Exception ddex) {
			InputStream input = FIXDataDictionary.class.getClassLoader().getResourceAsStream(fixDataDictionaryPath);
			try {
				theDict = new DataDictionary(input);
			} catch (ConfigError configError1) {
				throw new FIXFieldConverterNotAvailable("Could not create a FIX data dictionary.", ddex);
			}
		} catch (ConfigError configError) {
			throw new FIXFieldConverterNotAvailable("Could not create a FIX data dictionary.", configError);
		}

		mDictionary = theDict;
	}

	public String getHumanFieldName(int fieldNumber) {
		return mDictionary.getFieldName(fieldNumber);
	}

	/**
	 * Send in the field number and field value you want to translate Example:
	 * Side.FIELD and Side.BUY results in "BUY" Replaces all the _ with a space
	 * 
	 * @param fieldNumber
	 * @param value
	 * @return human-readable conversion of a FIX constant, or NULL if the value
	 *         was not found
	 */
	public String getHumanFieldValue(int fieldNumber, String value) {
		return getHumanFieldValue(mDictionary, fieldNumber, value);
	}

	public DataDictionary getDictionary() {
		return mDictionary;
	}

	/**
	 * Set the default version of FIX to use in the rest of the methods on this
	 * class
	 * 
	 * @param fixDataDictionaryPath
	 *            Path to the location of the data dictionary file
	 * @throws FIXFieldConverterNotAvailable
	 */
	public static FIXDataDictionary initializeDataDictionary(String fixDataDictionaryPath) throws Exception {
		return new FIXDataDictionary(fixDataDictionaryPath);
	}

	/**
	 * Send in the field number and field value you want to translate Example:
	 * Side.FIELD and Side.BUY results in "BUY" Replaces all the _ with a space
	 * 
	 * @param dict
	 * @param fieldNumber
	 * @param value
	 * @return human-readable conversion of a FIX constant, or NULL if the value
	 *         was not found
	 */
	public static String getHumanFieldValue(DataDictionary dict, int fieldNumber, String value) {
		String result = dict.getValueName(fieldNumber, value);
		return (result == null) ? result : result.replace('_', ' ');
	}
}
