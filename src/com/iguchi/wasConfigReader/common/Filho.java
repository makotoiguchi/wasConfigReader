package com.iguchi.wasConfigReader.common;

public class Filho implements Comparable<Filho>{
	private String key;
	private Bean value;

	public Filho(String key, Bean value) {
		super();
		this.key = key;
		this.value = value;
	}

	public int compareTo(Filho o) {
		return key.equals(o.getKey())? value.compareTo(o.getValue()) : key.compareTo(o.getKey());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Bean getValue() {
		return value;
	}

	public void setValue(Bean value) {
		this.value = value;
	}
}
