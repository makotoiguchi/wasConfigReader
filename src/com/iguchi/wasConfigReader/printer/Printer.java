package com.iguchi.wasConfigReader.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.common.Campo;
import com.iguchi.wasConfigReader.common.Constants;
import com.iguchi.wasConfigReader.common.Filho;

public class Printer {
	static String tab = Constants.SEPARATOR;
	static PrintStream printStream = System.out;
	
	public Printer() {
		super();
	}
	
	public Printer(File file) {
		super();
		setPrintStream(file);
	}
	
	public void setPrintStream(File file) {
		if (printStream != null && !printStream.equals(System.out)) {
			printStream.close();
		}
		
		try {
			printStream = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		printStream.close();
	}
	
	/**
	 * Imprimir linha de texto
	 * @param text linha a ser impressa
	 * @return
	 */
	public String printLine(String text) {
		StringBuffer line = new StringBuffer(100);

		line.append(text);
		
		return line.toString();
	}
	
	/**
	 * Imprime estrutura de "beans" (árvore)
	 * @param bean
	 */
	public void printBean(Bean bean){
		for (Filho filho : bean.getFilhos()) {
			printStream.println(Constants.SESSION_LINE);
			printBeanFilho(filho.getValue(), 0);
		}
	}
	
	/**
	 * Método recursivo para imprimir os filhos
	 * @param bean
	 * @param level
	 */
	private static void printBeanFilho(Bean bean, int level){
		StringBuffer tabsHeader = new StringBuffer();
		StringBuffer tabsBody = new StringBuffer();
		
		tabsBody.append(tab);
		for (int i = 0; i < level; i++) {
			tabsBody.append(tab);
			tabsHeader.append(tab);
		}
		
		printStream.println(tabsHeader + Constants.SESSION_BEGIN + bean.getId() + Constants.SESSION_END);
		
		for (Campo campo : bean.getCampos()) {
		    printStream.println(tabsBody + campo.getKey() + tab + campo.getValue());
		}
		
		for (Filho filho : bean.getFilhos()) {
			printBeanFilho(filho.getValue(), level + 1);
		}
	}
}
