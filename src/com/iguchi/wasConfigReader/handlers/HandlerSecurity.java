package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;


public class HandlerSecurity extends GenericHandler {
	String authMechanism = null;
	boolean parseSSO = false;
	Bean securityBean = null;
	Bean authMechBean = null;
	
	public HandlerSecurity(Bean bean, String context) {
		super(bean, context);
	}

	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		// atributos de segurança
		if (qName.equalsIgnoreCase("security:Security")) {
			securityBean = new Bean("SECURITY");
			bean.addFilho(securityBean);
			
			getAndAddElement(securityBean, attributes, "enabled", "Enable authentication cache");
			getAndAddElement(securityBean, attributes, "cacheTimeout", "Cache timeout");
			getAndAddElement(securityBean, attributes, "initialCacheSize", "Initial cache size");
			getAndAddElement(securityBean, attributes, "maxCacheSize", "Max cache size");
			getAndAddElement(securityBean, attributes, "basicAuthenticationCacheKeys", "Use basic authentication cache keys");
			getAndAddElement(securityBean, attributes, "customCacheKeys", "Use custom cache keys");
			getAndAddElement(securityBean, attributes, "issuePermissionWarning", "Issue Permission Warning");
			getAndAddElement(securityBean, attributes, "activeProtocol", "Active Protocol");
			authMechanism = getAndAddElement(securityBean, attributes, "activeAuthMechanism", "Authentication mechanisms and expiration");
			getAndAddElement(securityBean, attributes, "activeUserRegistry", "User account repository");
			
		} 
		// Cria novo "bean" de mecanismo de autenticação e parseia os atributos
		else if (authMechanism != null && qName.equalsIgnoreCase("authMechanisms")) {
			if (authMechanism.equals(attributes.getValue("xmi:id"))) {
				authMechBean = new Bean("AUTHENTICATION MECHANISM");
				bean.addFilho(authMechBean);
				
				getAndAddElement(authMechBean, attributes, "xmi:id", "Authentication Mechanism ID");
				getAndAddElement(authMechBean, attributes, "xmi:type", "Authentication Mechanism Type");
				
				parseSSO = true;
			}
		}
		// parse dos atributos referentes a SSO
		else if (parseSSO && qName.equalsIgnoreCase("singleSignon")) {
			getAndAddElement(authMechBean, attributes, "enabled", "Single Sign On Enabled");
			getAndAddElement(authMechBean, attributes, "xmi:id", "Single Sign On ID");
			getAndAddElement(authMechBean, attributes, "requiresSSL", "Require SSL");
			getAndAddElement(authMechBean, attributes, "domainName", "Domain Name");
		}
	}
	
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("authMechanisms")) {
			parseSSO = false;
		}
	}

}
