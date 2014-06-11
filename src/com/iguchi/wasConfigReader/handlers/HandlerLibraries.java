package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerLibraries extends GenericHandler {
	Bean librariesBean = null;
	Bean libraryBean = null;
	boolean parseClasspath = false;
	
	public HandlerLibraries(Bean bean, String context) {
		super(bean, context);
		// cria novo "bean" para só agrupar todos as shared libraries
		librariesBean = new Bean("SHARED LIBRARIES");
		bean.addFilho(librariesBean);
	}

	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// Cria um novo "bean" por shared library e parseia seus atributos
		if (qName.equals("libraries:Library")) {
			String libraryName = attributes.getValue("name");
			libraryBean = new Bean("Library - " + libraryName);
			librariesBean.addFilho(libraryBean);
			
			addKeyValue(libraryBean, "Library Name", libraryName);
			addKeyValue(libraryBean, "Context", context);
		}
		else if (qName.equals("classPath")) {
			parseClasspath = true;
		}
	}
	
	
	public void characters(char[] ch, int start, int length) throws SAXException { 
		// parsear o Classpath, que está armazenado como conteúdo de tag 
		if (parseClasspath) {
			String text = new String(ch, start, length);
			text = text.trim();
			if (!text.isEmpty()) {
				addKeyValue(libraryBean, "Classpath", text);
			}
		}
	}
	
	
	public void endElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		if (qName.equals("classPath")) {
			parseClasspath = false;
		}
	}
}
