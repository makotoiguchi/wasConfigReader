package com.iguchi.wasConfigReader.discovery;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.handlers.GenericHandler;
import com.iguchi.wasConfigReader.handlers.HandlerCluster;
import com.iguchi.wasConfigReader.handlers.HandlerLibraries;
import com.iguchi.wasConfigReader.handlers.HandlerResources;
import com.iguchi.wasConfigReader.handlers.HandlerVariables;

public class DiscoverCluster {
	Bean bean = null;
	Bean cluster = null;

	public DiscoverCluster(Bean root) {
		// Cria "bean" para agrupar configurações do contexto de Clusters
		bean = new Bean("CONTEXT - 2. CLUSTERS");
		root.addFilho(bean);
	}

	/**
	 * A partir da pasta indicada, dispara o parse dos arquivos XML de configuração e cria a estrutura de árvore
	 * @param path caminho da pasta de configuração da célula
	 */
	public void parse(File path) {
		File newPath = new File(path, "clusters"); 

		if (newPath.isDirectory()) {
			// listagem dos clusters
			String[] directories = listClusters(newPath);

			for(String dir : directories) {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setNamespaceAware(true);
				SAXParser saxParser;
				XMLReader xmlReader;
				File xmlFile;
				try {
					// cria um novo "bean" para cada Cluster
					cluster = new Bean(" Cluster - " + dir.toUpperCase());
					bean.addFilho(cluster);

					File clusterPath = new File(newPath, dir);

					// Cluster
					xmlFile = new File(clusterPath, "cluster.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerCluster(cluster, ""));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Libraries
					xmlFile = new File(clusterPath, "libraries.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerLibraries(cluster, "Cluster-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Resources
					xmlFile = new File(clusterPath, "resources.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerResources(cluster, "Cluster-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Variables
					xmlFile = new File(clusterPath, "variables.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerVariables(cluster, "Cluster-"+dir));
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
		} else {
			System.out.println("# 'clusters' nao encontrado");
		}
	}

	/**
	 * Lista os Clusters existentes na pasta 'clusters' e cria um nó da árvore 
	 * @param path pasta 'clusters'
	 * @return
	 */
	private String[] listClusters(File path) {
		String[] directories = path.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});

		// adiciona os clusters como atributo
		for(String dir : directories) {
			GenericHandler.addKeyValue(bean, "Name", dir);
		}
		return directories;
	}

}
