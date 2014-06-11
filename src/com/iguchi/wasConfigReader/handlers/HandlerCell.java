package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerCell extends GenericHandler {
		
	public HandlerCell(Bean bean, String context) {
		super(bean, context);
	}

	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// atributos de célula
		if (qName.equals("topology.cell:Cell")) {
			getAndAddElement(bean, attributes, "xmi:id", "Cell ID");
			getAndAddElement(bean, attributes, "name", "Cell Name");
			getAndAddElement(bean, attributes, "cellType", "Cell Type");
		}
	}
	
}
