package com.iguchi.wasConfigReader.common;

import java.util.SortedSet;
import java.util.TreeSet;

public class Bean implements Comparable<Bean> {

	private String id = "";
	private SortedSet<Campo> campos = null;
	private SortedSet<Filho> filhos = null;
	
	public Bean(String id) {
		this.id = id;
		campos = new TreeSet<Campo>();
		filhos = new TreeSet<Filho>();
	}
	
	public int compareTo(Bean bean) {
		return id.compareTo(bean.getId());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SortedSet<Campo> getCampos() {
		return campos;
	}
	
	public void addCampo(String key, String value){
		campos.add(new Campo(key, value));
	}

	public SortedSet<Filho> getFilhos() {
		return filhos;
	}
	
	public void addFilho(Bean bean) {
		filhos.add(new Filho(bean.getId(), bean));
	}
	
	public void addFilho(String id, Bean bean) {
		filhos.add(new Filho(id, bean));
	}

}
