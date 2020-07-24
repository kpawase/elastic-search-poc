package com.example.demo.dto;

public class SearchRequestDTo {

	private String key;

	private String value;

	private String criteria;

	private int limit;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public SearchRequestDTo() {
		super();
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

	@Override
	public String toString() {
		return "SearchRequestDTo [key=" + key + ", value=" + value + ", criteria=" + criteria + ", limit=" + limit
				+ "]";
	}
	
	

}
