package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerServerIndex extends GenericHandler {
	Bean serverIndexBean = null;
	Bean serverBean = null;
	boolean parsingServerEntry = false;
	boolean parsingDeployedApp = false;
	
	String endPointName = "";
	
	public HandlerServerIndex(Bean bean, String context) {
		super(bean, context);
	}


	public void startDocument() { 
		// cria novo "bean" para o Server Index
		serverIndexBean = new Bean("SERVER INDEX");
		bean.addFilho(serverIndexBean);
	}
	
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// para cada server entry (jvm) é criado um novo bean
		if (qName.equals("serverEntries")) {
			parsingServerEntry = true;
			
			String name = attributes.getValue("serverName");
			String type = attributes.getValue("serverType");
			String serverID = name + " - " + type;
			serverBean = new Bean("Server - " + serverID);
			serverIndexBean.addFilho(serverBean);
			
			addKeyValue(serverBean, "Server", serverID);
			
		}
		// os sub-elementos são armazenados como atributos do server entry
		else if (parsingServerEntry && qName.equals("deployedApplications")) {
			parsingDeployedApp = true;
		}
		else if (parsingServerEntry && qName.equals("deployedApplications")) {
			getAndAddElement(serverBean, attributes, "endPointName", "Endpoint Name");
		}
		else if (parsingServerEntry && qName.equals("specialEndpoints")) {
			endPointName = attributes.getValue("endPointName");
		}
		else if (parsingServerEntry && qName.equals("endPoint")) {
			String host = attributes.getValue("host");
			String port = attributes.getValue("port");
			
			addKeyValue(serverBean, "Endpoint", endPointName + " - " + host + ":" + port);
		}
		
	}
	
	
	public void characters(char[] ch, int start, int length) throws SAXException { 
		// parsear as aplicações deployadas, que está armazenado como conteúdo de tag 
		if (parsingDeployedApp) {
			String text = new String(ch, start, length);
			text = text.trim();
			if (!text.isEmpty()) {
				addKeyValue(serverBean, "deployedApplications", text);
			}
		}
	}
	
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("serverEntries")) {
			parsingServerEntry = false;
		}
		else if (qName.equals("deployedApplications")) {
			parsingDeployedApp = false;
		}
		else if ( qName.equals("specialEndpoints")) {
			endPointName = "";
		}
	}
}
