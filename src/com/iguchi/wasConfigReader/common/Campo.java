package com.iguchi.wasConfigReader.common;

public class Campo implements Comparable<Campo>{
	private String key;
	private String value;

	public Campo(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public int compareTo(Campo o) {
		return key.equals(o.getKey())? value.compareTo(o.getValue()) : key.compareTo(o.getKey());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
