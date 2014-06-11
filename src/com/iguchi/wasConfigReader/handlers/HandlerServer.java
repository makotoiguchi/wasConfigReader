package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.iguchi.wasConfigReader.common.Bean;

public class HandlerServer extends GenericHandler {
	Bean beanAtual = null;
	Bean subBeanAtual = null;
	String serviceType = "";
	String componentType = "";

	int level = 0;
	boolean parseProcessDefinition = false;

	public HandlerServer(Bean bean, String context) {
		super(bean, context);
	}

	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		
		/* System Logs */
		
		// Cria um "bean" para o SystemErr e parseia os atributos
		if (level == 1 && qName.equals("errorStreamRedirect")) {
			parseProcessDefinition = true;
			beanAtual = new Bean("System Err");
			bean.addFilho(beanAtual);
			
			getAndAddElement(beanAtual, attributes, "fileName");
			getAndAddElement(beanAtual, attributes, "rolloverType");
			getAndAddElement(beanAtual, attributes, "maxNumberOfBackupFiles");
			getAndAddElement(beanAtual, attributes, "rolloverSize");
			getAndAddElement(beanAtual, attributes, "baseHour");
			getAndAddElement(beanAtual, attributes, "rolloverPeriod");
			getAndAddElement(beanAtual, attributes, "formatWrites");
			getAndAddElement(beanAtual, attributes, "messageFormatKind");
			getAndAddElement(beanAtual, attributes, "suppressWrites");
			getAndAddElement(beanAtual, attributes, "suppressStackTrace");
		}
		
		// Cria um "bean" para o SystemOut e parseia os atributos
		else if (level == 1 && qName.equals("outputStreamRedirect")) {
			parseProcessDefinition = true;
			beanAtual = new Bean("System Out");
			bean.addFilho(beanAtual);
			
			getAndAddElement(beanAtual, attributes, "fileName");
			getAndAddElement(beanAtual, attributes, "rolloverType");
			getAndAddElement(beanAtual, attributes, "maxNumberOfBackupFiles");
			getAndAddElement(beanAtual, attributes, "rolloverSize");
			getAndAddElement(beanAtual, attributes, "baseHour");
			getAndAddElement(beanAtual, attributes, "rolloverPeriod");
			getAndAddElement(beanAtual, attributes, "formatWrites");
			getAndAddElement(beanAtual, attributes, "messageFormatKind");
			getAndAddElement(beanAtual, attributes, "suppressWrites");
			getAndAddElement(beanAtual, attributes, "suppressStackTrace");
		}
		
		/* Componentes */
		// TODO components
		
		
		/* processDefinitions */
		
		// Cria um "bean" para o Process Definitions e parseia os atributos
		else if (level == 1 && qName.equals("processDefinitions")) {
			parseProcessDefinition = true;
			beanAtual = new Bean("Process Definitions");
			bean.addFilho(beanAtual);
		}
		else if (parseProcessDefinition && qName.equals("execution")) {
			getAndAddElement(beanAtual, attributes, "processPriority");
			getAndAddElement(beanAtual, attributes, "runAsUser");
			getAndAddElement(beanAtual, attributes, "runAsGroup");
		}
		else if (parseProcessDefinition && qName.equals("ioRedirect")) {
			getAndAddElement(beanAtual, attributes, "stdoutFilename");
			getAndAddElement(beanAtual, attributes, "stderrFilename");
		}
		else if (parseProcessDefinition && qName.equals("monitoringPolicy")) {
			getAndAddElement(beanAtual, attributes, "maximumStartupAttempts");
			getAndAddElement(beanAtual, attributes, "pingInterval");
			getAndAddElement(beanAtual, attributes, "pingTimeout");
			getAndAddElement(beanAtual, attributes, "autoRestart");
			getAndAddElement(beanAtual, attributes, "nodeRestartState");
		}
		else if (parseProcessDefinition && qName.equals("jvmEntries")) {
			getAndAddElement(beanAtual, attributes, "verboseModeClass");
			getAndAddElement(beanAtual, attributes, "verboseModeGarbageCollection");
			getAndAddElement(beanAtual, attributes, "verboseModeJNI");
			getAndAddElement(beanAtual, attributes, "initialHeapSize");
			getAndAddElement(beanAtual, attributes, "maximumHeapSize");
			getAndAddElement(beanAtual, attributes, "runHProf");
			getAndAddElement(beanAtual, attributes, "debugMode");
			getAndAddElement(beanAtual, attributes, "debugArgs");
			getAndAddElement(beanAtual, attributes, "genericJvmArguments");
			getAndAddElement(beanAtual, attributes, "executableJarFileName");
			getAndAddElement(beanAtual, attributes, "disableJIT");
		}
		
		/* services */
		
		// Cria um "bean" para o PMI e parseia os atributos
		else if (level == 1 && qName.equals("services") && attributes.getValue("xmi:type").equals("pmiservice:PMIService")) {	
			beanAtual = new Bean("PMI Service");
			bean.addFilho(beanAtual);
			getAndAddElement(beanAtual, attributes, "enable");
			getAndAddElement(beanAtual, attributes, "initialSpecLevel");
			getAndAddElement(beanAtual, attributes, "statisticSet");
			getAndAddElement(beanAtual, attributes, "synchronizedUpdate");
		}
		// Para os outros tipos de service, cria um novo "bean"
		else if (level == 1 && qName.equals("services")) {
			boolean ok = false;

			serviceType = attributes.getValue("xmi:type");
			
			// só aceita os services abaixo
			ok = ok || serviceType.equals("threadpoolmanager:ThreadPoolManager");
			ok = ok || serviceType.equals("loggingservice.http:HTTPAccessLoggingService");

			if (ok) {
				beanAtual = new Bean(serviceType);
				bean.addFilho(beanAtual);
			} else {
				serviceType = "";
			}	
		}
		// parse dos services aceitos 
		else if (serviceType.equals("threadpoolmanager:ThreadPoolManager") && qName.equals("threadPools")) {
			String threadPoolName = attributes.getValue("name");
			subBeanAtual = new Bean("ThreadPool - " + threadPoolName);
			beanAtual.addFilho(subBeanAtual);
			
			getAndAddElement(subBeanAtual, attributes, "name");
			getAndAddElement(subBeanAtual, attributes, "minimumSize");
			getAndAddElement(subBeanAtual, attributes, "maximumSize");
			getAndAddElement(subBeanAtual, attributes, "inactivityTimeout");
			getAndAddElement(subBeanAtual, attributes, "isGrowable");
		}
		else if (serviceType.equals("loggingservice.http:HTTPAccessLoggingService")) {
			subBeanAtual = new Bean("HTTP Log - " + qName);
			beanAtual.addFilho(subBeanAtual);
			
			getAndAddElement(subBeanAtual, attributes, "filePath", qName + ".filePath");
			getAndAddElement(subBeanAtual, attributes, "maximumSize", qName + ".maximumSize");
		}

		level++;
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("services")) {
			serviceType = "";
		}
		else if (qName.equals("components")) {
			componentType = "";
		}
		else if (qName.equals("processDefinitions")) {
			parseProcessDefinition = false;
		}
		
		level--;
	}
}
