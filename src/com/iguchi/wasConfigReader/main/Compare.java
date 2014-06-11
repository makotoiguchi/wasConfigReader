package com.iguchi.wasConfigReader.main;

import java.io.File;
import java.util.ArrayList;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.comparator.CompareBeans;
import com.iguchi.wasConfigReader.discovery.DiscoverAll;
import com.iguchi.wasConfigReader.printer.Printer;

public class Compare {

	public static void main(String[] args) {
		DiscoverAll discoverAll = new DiscoverAll();
		CompareBeans compareBeans = new CompareBeans();
		Printer printer = new Printer();

		if (args.length == 3) {
			File pathSRC = new File(args[0]);
			File pathDST = new File(args[1]);
			File path = new File(args[2]);

			Bean origem = discoverAll.parse(pathSRC);
			Bean destino = discoverAll.parse(pathDST); 
			Bean common = new Bean("COMMON");
			
			ArrayList<Bean> beans = compareBeans.compare(origem, destino, common);
			
			File outSRC = null;
			File outDST = null;
			File diffSRC = null;
			File diffDST = null;
			File outCommon = null;
			if (path.isDirectory()) {
				outSRC = new File(path, "config-" + pathSRC.getName() + ".txt");
				outDST = new File(path, "config-" + pathDST.getName() + ".txt");
				diffSRC = new File(path, "diff-" + pathSRC.getName() + ".txt");
				diffDST = new File(path, "diff-" + pathDST.getName() + ".txt");
				outCommon = new File(path, "common-" + pathSRC.getName() + "-" + pathDST.getName() + ".txt");				
			} else {
				outSRC = new File("config-" + pathSRC.getName() + ".txt");
				outDST = new File("config-" + pathDST.getName() + ".txt");
				diffSRC = new File("diff-" + pathSRC.getName() + ".txt");
				diffDST = new File("diff-" + pathDST.getName() + ".txt");
				outCommon = new File("common-" + pathSRC.getName() + "-" + pathDST.getName() + ".txt");
			}
			
			// print nos originais
			printer.setPrintStream(outSRC);
			printer.printBean(origem);
			printer.setPrintStream(outDST);
			printer.printBean(destino);
			// print nos diff's
			printer.setPrintStream(diffSRC);
			printer.printBean(beans.get(0));
			printer.setPrintStream(diffDST);
			printer.printBean(beans.get(1));
			printer.setPrintStream(outCommon);
			printer.printBean(beans.get(2));
			printer.close();
			
		} else {
			System.out.println("Primeiro argumento deve ser a pasta de configuração da célula \"Origem\"");
			System.out.println("Segundo argumento deve ser a pasta de configuração da célula \"Destino\".");
		}
	}
}
