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
import com.iguchi.wasConfigReader.handlers.HandlerLibraries;
import com.iguchi.wasConfigReader.handlers.HandlerNode;
import com.iguchi.wasConfigReader.handlers.HandlerResources;
import com.iguchi.wasConfigReader.handlers.HandlerServerIndex;
import com.iguchi.wasConfigReader.handlers.HandlerSystemApps;
import com.iguchi.wasConfigReader.handlers.HandlerVariables;

public class DiscoverNodes {
	Bean bean = null;
	Bean node = null;

	public DiscoverNodes(Bean root) {
		// Cria "bean" para agrupar configurações do contexto de Nodes
		bean = new Bean("CONTEXT - 3. NODES");
		root.addFilho(bean);
	}

	/**
	 * A partir da pasta indicada, dispara o parse dos arquivos XML de configuração e cria a estrutura de árvore
	 * @param path caminho da pasta de configuração da célula
	 */
	public void parse(File path) {
		File newPath = new File(path, "nodes"); 

		if (newPath.isDirectory()) {
			// listagem dos nodes
			String[] directories = listNodes(newPath);

			for(String dir : directories) {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setNamespaceAware(true);
				SAXParser saxParser;
				XMLReader xmlReader;
				File xmlFile;
				try {
					// cria um novo "bean" para cada Node
					node = new Bean("Node - " + dir.toUpperCase());
					bean.addFilho(node);

					File nodePath = new File(newPath, dir);

					// Node
					xmlFile = new File(nodePath, "node.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerNode(node, ""));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Libraries
					xmlFile = new File(nodePath, "libraries.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerLibraries(node, "Cluster-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Resources
					xmlFile = new File(nodePath, "resources.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerResources(node, "Cluster-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// Variables
					xmlFile = new File(nodePath, "variables.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerVariables(node, "Node-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// SystemApps
					xmlFile = new File(nodePath, "systemapps.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerSystemApps(node, "Node-"+dir));
						xmlReader.parse( xmlFile.getAbsolutePath() );
					}

					// ServerIndex
					xmlFile = new File(nodePath, "serverindex.xml");
					if (xmlFile.isFile()) {
						saxParser = spf.newSAXParser();
						xmlReader = saxParser.getXMLReader();
						xmlReader.setContentHandler(new HandlerServerIndex(node, "Node-"+dir));
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
			System.out.println("# 'nodes' nao encontrado");
		}
	}

	/**
	 * Lista os Nodes existentes na pasta 'nodes' e cria um nó da árvore 
	 * @param path pasta 'nodes'
	 * @return
	 */
	private String[] listNodes(File path) {
		String[] directories = path.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});

		// adiciona os nodes como atributo
		for(String dir : directories) {
			GenericHandler.addKeyValue(bean, "Name", dir);
		}
		return directories;
	}

}
