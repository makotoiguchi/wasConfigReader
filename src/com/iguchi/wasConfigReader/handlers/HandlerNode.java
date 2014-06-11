package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerNode extends GenericHandler {
	
	public HandlerNode(Bean bean, String context) {
		super(bean, context);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// Parse dos atributos do node
		if (qName.equals("topology.node:Node")) {
			getAndAddElement(bean, attributes, "nodeName", "Node Name");
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
	}
	
}
