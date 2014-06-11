package com.iguchi.wasConfigReader.discovery;

import java.io.File;

import com.iguchi.wasConfigReader.common.Bean;

public class DiscoverAll {

	/**
	 * A partir da pasta indicada, dispara o parse dos arquivos XML de configuração e cria a estrutura de árvore
	 * @param path caminho da pasta de configuração da célula
	 * @return
	 */
	public Bean parse(File path) {
		Bean root = null;
		
		if ( path.isDirectory() ) {
			root = new Bean("ROOT");
			
//			System.out.println("# " + path.toString());
			
			// CONTEXT CELL
			DiscoverCell discoverCell = new DiscoverCell(root);
			discoverCell.parse(path);
			
			// TODO cus
			// TODO blas
			// TODO nodegroup
			// TODO coregroup

			// CONTEXT CLUSTER
			DiscoverCluster discoverCluster = new DiscoverCluster(root);
			discoverCluster.parse(path);

			// CONTEXT NODES
			DiscoverNodes discoverNodes = new DiscoverNodes(root);
			discoverNodes.parse(path);

			// CONTEXT SERVERS (JVM)
			DiscoverServers discoverServers = new DiscoverServers(root); 
			discoverServers.parse(path);
			
		} else {
			System.out.println("# diretorio invalido");
		}
		
		return root;
	}
	
}
