package com.example.demo.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatalistResponseDto {

	
	@JsonProperty("statusCode")
	private int statusCode;

	@JsonProperty("message")
	private String message;

	@JsonProperty("data")
	private Set<String> data;
	
	

	public DatalistResponseDto(int statusCode, String message, Set<String> data) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<String> getData() {
		return data;
	}

	public void setData(Set<String> data) {
		this.data = data;
	}
	
	
	
}
