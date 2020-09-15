/**
 * 
 */
package com.algoTrader.service.fix.filters;

import java.util.Map;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.SecurityExchange;
import quickfix.field.Symbol;

import com.algoTrader.service.fix.quickfix.messagefactory.FIXMessageAugmentor;

/**
 * @author administrator
 * 
 */
public class GTJAMessageModifier implements MessageModifier {

	private Map<String, String> symbolExchangeMap;

	public Map<String, String> getSymbolExchangeMap() {
		return symbolExchangeMap;
	}

	public void setSymbolExchangeMap(Map<String, String> symbolExchangeMap) {
		this.symbolExchangeMap = symbolExchangeMap;
	}

	@Override
	public boolean modifyMessage(Message message, FIXMessageAugmentor augmentor) throws Exception {
		boolean modified = false;
		try {
			String msgType = message.getHeader().getString(MsgType.FIELD);
			if (msgType.equalsIgnoreCase("D")) {
				String symbol = message.getString(Symbol.FIELD);
				if (symbol != null && symbolExchangeMap.containsKey(symbol)) {
					String exchange = symbolExchangeMap.get(symbol);
					message.setField(new SecurityExchange(exchange));
				}
			}
		} catch (FieldNotFound e) {

		}

		return modified;
	}

}
