package com.iguchi.wasConfigReader.discovery;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.handlers.HandlerCell;
import com.iguchi.wasConfigReader.handlers.HandlerLibraries;
import com.iguchi.wasConfigReader.handlers.HandlerResources;
import com.iguchi.wasConfigReader.handlers.HandlerSecurity;
import com.iguchi.wasConfigReader.handlers.HandlerVariables;
import com.iguchi.wasConfigReader.handlers.HandlerVirtualHosts;

public class DiscoverCell {
	Bean bean = null;
	
	public DiscoverCell(Bean root) {
		// Cria "bean" para agrupar configurações do contexto de Célula
		bean = new Bean("CONTEXT - 1. CELL");
		root.addFilho(bean);
	}

	/**
	 * A partir da pasta indicada, dispara o parse dos arquivos XML de configuração e cria a estrutura de árvore
	 * @param path caminho da pasta de configuração da célula
	 */
	public void parse(File path) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser;
		XMLReader xmlReader;
		File xmlFile;
		try {
			// Cell
			xmlFile = new File(path, "cell.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerCell(bean, ""));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}

			// Security
			xmlFile = new File(path, "security.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerSecurity(bean, "Cell"));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}

			// Virtual Hosts
			xmlFile = new File(path, "virtualhosts.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerVirtualHosts(bean, "Cell"));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}

			// Libraries
			xmlFile = new File(path, "libraries.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerLibraries(bean, "Cell"));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}

			// Resources
			xmlFile = new File(path, "resources.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerResources(bean, "Cell"));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}

			// Variables
			xmlFile = new File(path, "variables.xml");
			if (xmlFile.isFile()) {
				saxParser = spf.newSAXParser();
				xmlReader = saxParser.getXMLReader();
				xmlReader.setContentHandler(new HandlerVariables(bean, "Cell"));
				xmlReader.parse( xmlFile.getAbsolutePath() );
			}
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
