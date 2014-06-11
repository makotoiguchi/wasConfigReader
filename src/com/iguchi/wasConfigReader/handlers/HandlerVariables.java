package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerVariables extends GenericHandler {
	Bean varBean = null;
	
	public HandlerVariables(Bean bean, String context) {
		super(bean, context);
	}
	
	public void startDocument() {
		// cria novo "bean" para as variáveis
		varBean = new Bean("VARIABLES");
		bean.addFilho(varBean);
		
		addKeyValue(varBean, "Context" , context);
	}
	
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// cada variável é armazenada como um atributo do bean "Variables"
		if (qName.equals("entries")) {
			String key = attributes.getValue("symbolicName");
			String value = attributes.getValue("value");
			addKeyValue(varBean, key, value);
		}
	}
}
