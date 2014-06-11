package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerSystemApps extends GenericHandler {
	Bean sysAppsBean = null;
	
	public HandlerSystemApps(Bean bean, String context) {
		super(bean, context);
	}


	public void startDocument() { 
		// cria novo "bean" para as aplicações do node
		sysAppsBean = new Bean("SYSTEM APPS");
		bean.addFilho(sysAppsBean);
		addKeyValue(sysAppsBean, "Context" , context);
	}
	
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// cada server entry (jvm) é armazenado como atributo 
		if (qName.equals("serverEntries")) {
			String name = attributes.getValue("serverName");
			String type = attributes.getValue("serverType");
			
			addKeyValue(sysAppsBean, "Server", name + " - " + type);
			
		}
	}
}
