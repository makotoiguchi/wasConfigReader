package com.iguchi.wasConfigReader.main;

import java.io.File;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.discovery.DiscoverAll;
import com.iguchi.wasConfigReader.printer.Printer;

public class Reader {

	public static void main(String[] args) {
		DiscoverAll discoverAll = new DiscoverAll();
		Printer printer = null;

		if (args.length > 0) {
			File path = new File(args[0]);

			// Cria estrutura de arvore com as config
			Bean root = discoverAll.parse(path);

			// se houver mais um argumento, trat�-lo como o nome do arquivo de output
			if (args.length > 1) {
				printer = new Printer(new File(args[1]));
			} else {
				printer = new Printer();
			}

			// Imprime a �rvore
			printer.printBean(root);
			printer.close();
			
		} else {
			System.out.println("Primeiro argumento deve ser a pasta de configura��o da c�lula.");
		}
	}	
}
