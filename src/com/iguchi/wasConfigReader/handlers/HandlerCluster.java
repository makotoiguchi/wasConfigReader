package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerCluster extends GenericHandler {
	
	public HandlerCluster(Bean bean, String context) {
		super(bean, context);
	}
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// Parse dos atributos do cluster
		if (qName.equals("members")) {
			getAndAddElement(bean, attributes, "nodeName", "Node Name");
			getAndAddElement(bean, attributes, "memberName", "JVM Name");
			getAndAddElement(bean, attributes, "weight", "Weight");
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
	}
	
}
