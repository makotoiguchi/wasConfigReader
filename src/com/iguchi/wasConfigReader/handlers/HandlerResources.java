package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerResources extends GenericHandler {
	Bean resourcesBean = null;
	Bean providerBean = null;
	Bean factoryBean = null;
	Bean propertiesBean = null;
	Bean connPoolBean = null;
	
	boolean parsingJDBCProvider = false;
	boolean parsingProviderClasspath = false;
	boolean parsingFactory = false;
	
	
	public HandlerResources(Bean bean, String context) {
		super(bean, context);
		// cria novo "bean" para só agrupar todos os resources
		resourcesBean = new Bean("RESOURCES");
		bean.addFilho(resourcesBean);
	}

	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// TODO outros Providers
		
		// Cria um novo "bean" por JDBC Provider e parseia seus atributos
		if (qName.equals("resources.jdbc:JDBCProvider")) {
			parsingJDBCProvider = true;
			
			String providerName = attributes.getValue("name");
			providerBean = new Bean("JDBC Provider - " + providerName);
			resourcesBean.addFilho(providerBean);
			
			addKeyValue(providerBean, "Provider Name", providerName);
			addKeyValue(providerBean, "Context", context);
			getAndAddElement(providerBean, attributes, "providerType", "Provider Type");
			getAndAddElement(providerBean, attributes, "implementationClassName", "Provider Classname");
			getAndAddElement(providerBean, attributes, "xa", "Provider XA");
		}
		if (parsingJDBCProvider && qName.equals("classpath")) {
			parsingProviderClasspath = true;
		}
		// Cria um novo "bean" por Factory (Datasource) e parseia seus atributos
		if (parsingJDBCProvider && qName.equals("factories")) {
			parsingFactory = true;
			String factoryName = attributes.getValue("name");
			factoryBean = new Bean("Factory - " + factoryName);
			providerBean.addFilho(factoryBean);
			
			addKeyValue(factoryBean, "Factory Name", factoryName);
			addKeyValue(factoryBean, "Context", context);
			getAndAddElement(factoryBean, attributes, "xmi:type", "Type");
			getAndAddElement(factoryBean, attributes, "providerType", "Provider Type");
			getAndAddElement(factoryBean, attributes, "jndiName", "JNDI Name");
			getAndAddElement(factoryBean, attributes, "datasourceHelperClassname", "Helper Classname");
			getAndAddElement(factoryBean, attributes, "authDataAlias", "Authentication Data Alias");
			getAndAddElement(factoryBean, attributes, "name", "Factory Name");
			
			propertiesBean = new Bean("Properties");
			factoryBean.addFilho(propertiesBean);
		}
		// parse das properties do Factory (Datasource)
		else if (parsingFactory && qName.equals("resourceProperties")) {
			String key = attributes.getValue("name");
			String value = attributes.getValue("value");
			addKeyValue(propertiesBean, key, value);
		}
		// Cria novo "bean" e parseia o Connection Pool do Factory (Datasource)
		else if (parsingFactory && qName.equals("connectionPool")) {
			connPoolBean = new Bean("Connection Pool");
			factoryBean.addFilho(connPoolBean);
			
			getAndAddElement(connPoolBean, attributes, "connectionTimeout", "Connection Timeout");
			getAndAddElement(connPoolBean, attributes, "maxConnections", "Max Connections");
			getAndAddElement(connPoolBean, attributes, "minConnections", "Min Connections");
			getAndAddElement(connPoolBean, attributes, "unusedTimeout", "Unused Timeout");
			getAndAddElement(connPoolBean, attributes, "reapTime", "Reap Time");
			getAndAddElement(connPoolBean, attributes, "agedTimeout", "Aged Timeout");
			getAndAddElement(connPoolBean, attributes, "purgePolicy", "Purge Policy");
			getAndAddElement(connPoolBean, attributes, "numberOfSharedPoolPartitions");
			getAndAddElement(connPoolBean, attributes, "numberOfUnsharedPoolPartitions");
			getAndAddElement(connPoolBean, attributes, "numberOfFreePoolPartitions");
			getAndAddElement(connPoolBean, attributes, "freePoolDistributionTableSize");
			getAndAddElement(connPoolBean, attributes, "surgeThreshold");
			getAndAddElement(connPoolBean, attributes, "surgeCreationInterval");
			getAndAddElement(connPoolBean, attributes, "testConnection");
			getAndAddElement(connPoolBean, attributes, "testConnectionInterva");
			getAndAddElement(connPoolBean, attributes, "stuckTimerTime");
			getAndAddElement(connPoolBean, attributes, "stuckTime");
			getAndAddElement(connPoolBean, attributes, "stuckThreshold");
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException { 
		// parsear o Classpath, que está armazenado como conteúdo de tag 
		if (parsingProviderClasspath) {
			String text = new String(ch, start, length);
			text = text.trim();
			if (!text.isEmpty()) {
				addKeyValue(providerBean, "Provider Classpath", text);
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("resources.jdbc:JDBCProvider")) {
			parsingJDBCProvider = false;
		}
		else if (parsingJDBCProvider && qName.equals("classpath")) {
			parsingProviderClasspath = false;
		}
		else if (qName.equals("factories")) {
			parsingFactory = false;
		}
	}
}
