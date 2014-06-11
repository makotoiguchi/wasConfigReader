package com.iguchi.wasConfigReader.comparator;

import java.util.ArrayList;
import java.util.Arrays;

import com.iguchi.wasConfigReader.common.Bean;
import com.iguchi.wasConfigReader.common.Campo;
import com.iguchi.wasConfigReader.common.Filho;

public class CompareBeans {
	
	/**
	 * Compara duas estruturas de Beans e retorna um arrayList com 3 Beans:
	 * <ul>
	 * <li>Bean 0: elementos diferentes na origem;</li>
	 * <li>Bean 1: elementos diferentes no destino;</li>
	 * <li>Bean 2: elementos comuns.</li>
	 * </ul>
	 * @param beanSRC
	 * @param beanDST
	 * @param common 
	 * @return
	 */
	public ArrayList<Bean> compare(Bean beanSRC, Bean beanDST, Bean common) {
		Bean beanL = new Bean(beanSRC.getId());
		Bean beanR = new Bean(beanDST.getId());
		Bean beanC = common;
		
		/* Comparar CAMPOS */
		
		ArrayList<Campo> camposSRC = new ArrayList<Campo>(beanSRC.getCampos());
		ArrayList<Campo> camposDST = new ArrayList<Campo>(beanDST.getCampos()); 
				
		int sizeSRC = camposSRC.size();
		int sizeDST = camposDST.size();
		
		int indexSRC = 0;
		int indexDST = 0;		
		
		// Compara os dois
		while ((indexSRC < sizeSRC) && (indexDST < sizeDST)) {
			Campo campoSRC = camposSRC.get(indexSRC);
			Campo campoDST = camposDST.get(indexDST);
			
			int compare = campoSRC.compareTo(campoDST);
			
			if (compare < 0) {
				// "antes"
				beanL.addCampo(campoSRC.getKey(), campoSRC.getValue());
				indexSRC++;
			}
			else if (compare > 0) {
				// "depois"
				beanR.addCampo(campoDST.getKey(), campoDST.getValue());
				indexDST++;
			}
			else /*if (compare == 0)*/ {
				// "iguais"
				beanC.addCampo(campoSRC.getKey(), campoSRC.getValue());
				indexSRC++;
				indexDST++;
			}
			
		}
		
		// se sobrar em SRC, adicionar direto no beanL 
		while (indexSRC < sizeSRC) {
			Campo campoSRC = camposSRC.get(indexSRC);
			beanL.addCampo(campoSRC.getKey(), campoSRC.getValue());
			indexSRC++;
		}

		// se sobrar em DST, adicionar direto no beanR
		while (indexDST < sizeDST) {
			Campo campoDST = camposDST.get(indexDST);
			beanL.addCampo(campoDST.getKey(), campoDST.getValue());
			indexDST++;
		}
		
		/* Comparar FILHOS */
		
		ArrayList<Filho> filhosSRC = new ArrayList<Filho>(beanSRC.getFilhos());
		ArrayList<Filho> filhosDST = new ArrayList<Filho>(beanDST.getFilhos()); 
				
		sizeSRC = filhosSRC.size();
		sizeDST = filhosDST.size();
		
		indexSRC = 0;
		indexDST = 0;		
		
		// Compara os dois
		while ((indexSRC < sizeSRC) && (indexDST < sizeDST)) {
			Filho filhoSRC = filhosSRC.get(indexSRC);
			Filho filhoDST = filhosDST.get(indexDST);
			
			int compare = filhoSRC.compareTo(filhoDST);
			
			if (compare < 0) {
				// "antes"
				beanL.addFilho(filhoSRC.getKey(), filhoSRC.getValue());
				indexSRC++;
			}
			else if (compare > 0) {
				// "depois"
				beanR.addFilho(filhoDST.getKey(), filhoDST.getValue());
				indexDST++;
			}
			else /*if (compare == 0)*/ {
				// "iguais"
				
				// compara filhos
				Bean commonFilho = new Bean(filhoSRC.getKey());
				ArrayList<Bean> filhos = compare(filhoSRC.getValue(), filhoDST.getValue(), commonFilho);
				
				// adiciona nos 3!
				beanL.addFilho(filhoSRC.getKey(), filhos.get(0));
				beanR.addFilho(filhoDST.getKey(), filhos.get(1));
				beanC.addFilho(filhoSRC.getKey(), commonFilho);
				indexSRC++;
				indexDST++;
			}
			
		}
		
		// se sobrar em SRC, adicionar direto no beanL
		while (indexSRC < sizeSRC) {
			Filho filhoSRC = filhosSRC.get(indexSRC);
			beanL.addFilho(filhoSRC.getKey(), filhoSRC.getValue());
			indexSRC++;
		}

		// se sobrar em DST, adicionar direto no beanR
		while (indexDST < sizeDST) {
			Filho filhoDST = filhosDST.get(indexDST);
			beanR.addFilho(filhoDST.getKey(), filhoDST.getValue());
			indexDST++;
		}
		
		return new ArrayList<Bean> (Arrays.asList(beanL, beanR, beanC));
	}
}