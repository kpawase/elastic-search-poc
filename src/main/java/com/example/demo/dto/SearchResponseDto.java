package com.example.demo.dto;

import java.util.List;

import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;

public class SearchResponseDto {

	private RestStatus staus;

	private TimeValue executionTime;

	private int successfulShards;

	private int failedShards;

	private int numberOfSearchedHints;

	private List<TransactionDto> searchResult;

	public SearchResponseDto() {
	}

	public SearchResponseDto(RestStatus staus, TimeValue executionTime, int successfulShards, int failedShards,
			int numberOfSearchedHints, List<TransactionDto> searchResult) {
		super();
		this.staus = staus;
		this.executionTime = executionTime;
		this.successfulShards = successfulShards;
		this.failedShards = failedShards;
		this.numberOfSearchedHints = numberOfSearchedHints;
		this.searchResult = searchResult;
	}

	public RestStatus getStaus() {
		return staus;
	}

	public void setStaus(RestStatus staus) {
		this.staus = staus;
	}

	public TimeValue getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(TimeValue executionTime) {
		this.executionTime = executionTime;
	}

	public int getSuccessfulShards() {
		return successfulShards;
	}

	public void setSuccessfulShards(int successfulShards) {
		this.successfulShards = successfulShards;
	}

	public int getFailedShards() {
		return failedShards;
	}

	public void setFailedShards(int failedShards) {
		this.failedShards = failedShards;
	}

	public int getNumberOfSearchedHints() {
		return numberOfSearchedHints;
	}

	public void setNumberOfSearchedHints(int numberOfSearchedHints) {
		this.numberOfSearchedHints = numberOfSearchedHints;
	}

	public List<TransactionDto> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(List<TransactionDto> searchResult) {
		this.searchResult = searchResult;
	}

}
