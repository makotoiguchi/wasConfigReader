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
import com.iguchi.wasConfigReader.handlers.HandlerResources;
import com.iguchi.wasConfigReader.handlers.HandlerServer;
import com.iguchi.wasConfigReader.handlers.HandlerVariables;

public class DiscoverServers {
	Bean bean = null;
	Bean beanJVM = null;

	public DiscoverServers(Bean root) {
		// Cria "bean" para agrupar configurações do contexto de JVM
		bean = new Bean("CONTEXT - 4. JVM");
		root.addFilho(bean);
	}


	public void parse(File path) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser;
		XMLReader xmlReader;
		File xmlFile;

		// Cell/nodes
		File nodesPath = new File(path, "nodes"); 
		if (nodesPath.isDirectory()) {

			String[] nodeDirs = listDirs(nodesPath);

			// Cell/nodes/nodeDirs[]
			for(String nodeDir : nodeDirs) {

				// Cell/nodes/<nodeDir>/servers/
				File nodeDirPath = new File(nodesPath, nodeDir);
				File serversPath = new File(nodeDirPath, "servers");

				if (serversPath.isDirectory()) {
					String[] serverDirs = listDirs(serversPath);

					// Cell/nodes/<nodeDir>/servers/serverDirs[]
					for(String serverDir : serverDirs) {
						// Cell/nodes/<nodeDir>/servers/<serverDir>

						// Para cada server (JVM) é criado um novo "bean"
						String jvmID = nodeDir.toUpperCase() + "/" + serverDir.toUpperCase();
						beanJVM = new Bean("JVM - " + jvmID);
						bean.addFilho(beanJVM);	

						File serverPath = new File(serversPath, serverDir);

						try {
							// Server
							xmlFile = new File(serverPath, "server.xml");
							if (xmlFile.isFile()) {
								saxParser = spf.newSAXParser();
								xmlReader = saxParser.getXMLReader();
								xmlReader.setContentHandler(new HandlerServer(beanJVM, "Server-"+serverDir));
								xmlReader.parse( xmlFile.getAbsolutePath() );
							}

							// Resources
							xmlFile = new File(serverPath, "resources.xml");
							if (xmlFile.isFile()) {
								saxParser = spf.newSAXParser();
								xmlReader = saxParser.getXMLReader();
								xmlReader.setContentHandler(new HandlerResources(beanJVM, "Server-"+serverDir));
								xmlReader.parse( xmlFile.getAbsolutePath() );
							}

							// variables
							xmlFile = new File(serverPath, "variables.xml");
							if (xmlFile.isFile()) {
								saxParser = spf.newSAXParser();
								xmlReader = saxParser.getXMLReader();
								xmlReader.setContentHandler(new HandlerVariables(beanJVM, "Server-"+serverDir));
								xmlReader.parse( xmlFile.getAbsolutePath() );
							}

						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Lista os diretórios existentes na pasta indicada
	 * @param path pasta alvo
	 * @return
	 */
	private String[] listDirs(File path) {
		String[] directories = path.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});

		return directories;
	}

}
