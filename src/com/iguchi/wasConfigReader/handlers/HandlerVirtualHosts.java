package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerVirtualHosts extends GenericHandler {
	Bean vhostsBean = null;
	Bean vhostBean = null;
	boolean parseAlias = false;
	
	public HandlerVirtualHosts(Bean bean, String context) {
		super(bean, context);
		// cria novo "bean" para só agrupar todos os virtual hosts
		vhostsBean = new Bean("VIRTUAL HOSTS");
		bean.addFilho(vhostsBean);
	}
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// Cria um novo "bean" por virtual host e parseia seus atributos
		if (qName.equals("host:VirtualHost")) {
			parseAlias = true;
			
			String vhostName = attributes.getValue("name");
			vhostBean = new Bean("Virtual Host - " + vhostName);
			vhostsBean.addFilho(vhostBean);
			
			addKeyValue(vhostBean, "Host Name", vhostName);
			addKeyValue(vhostBean, "Context", context);
			getAndAddElement(vhostBean, attributes, "xmi:id", "Host ID");
		}
		// os "Aliases" são armazenados como atributo do virtual host 
		else if (parseAlias && qName.equals("aliases")) {
			String hostname = attributes.getValue("hostname");
			String port = attributes.getValue("port");
			addKeyValue(vhostBean, "Alias", hostname + ":" + port);
		}
	}
	
	public void endElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		if (qName.equals("host:VirtualHost")) {
			parseAlias = false;
		}
	}
}
