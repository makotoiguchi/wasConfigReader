package com.iguchi.wasConfigReader.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.iguchi.wasConfigReader.common.Bean;

public class GenericHandler extends DefaultHandler {
	Bean bean = null;
	String context = "";
	
	public GenericHandler() {
		this.context = "";	
	}
	
	/**
	 * Construtor
	 * @param bean nó raiz
	 * @param context string de contexto 
	 */
	public GenericHandler(Bean bean, String context) {
		this.bean = bean;
		this.context = context;
	}

	/**
	 * Pega o atributo pela chave para imprimir
	 * @param qtdTab deslocamento
	 * @param attributes array de atributos
	 * @param key chave do atributo
	 * @param text label do atributo
	 * @return
	 */
	public String getAndAddElement(Bean bean, Attributes attributes, String key, String text) {
		String value = attributes.getValue(key);
		if (value != null && !value.isEmpty()) {
			addKeyValue(bean, text, value);
		}

		return value;
	}
	
	/**
	 * Pega o atributo pela chave para imprimir
	 * @param qtdTab deslocamento
	 * @param attributes array de atributos
	 * @param key chave do atributo também utilizado como label
	 * @return
	 */
	public String getAndAddElement(Bean bean, Attributes attributes, String key) {
		String value = attributes.getValue(key);
		if (value != null && !value.isEmpty()) {
			addKeyValue(bean, key, value);
		}

		return value;
	}
	
	/**
	 * Imprimir linha de texto
	 * @param qtdTab deslocamento
	 * @param text linha a ser impressa
	 * @return
	 */
//	public String printLine(String text) {
//		return Printer.printLine(text);
//	}
	
	/**
	 * Imprimir linha de texto com chave-valor
	 * @param qtdTab
	 * @param key
	 * @param value
	 * @return
	 */
	public static void addKeyValue(Bean bean, String key, String value) {
		bean.addCampo(key, value);
	}
}
