package com.example.demo.dto;

import org.elasticsearch.common.unit.TimeValue;

public class CreateIndexResponseDto {
	
	private int recordsProcessed;
	
	private String indexName;
	
	private TimeValue executionTime;
	
	private boolean hasFailures;
	
	

	public CreateIndexResponseDto(int recordsProcessed, TimeValue executionTime, String indexName ,boolean hasFailures) {
		super();
		this.recordsProcessed = recordsProcessed;
		this.executionTime = executionTime;
		this.indexName = indexName;
		this.hasFailures = hasFailures;
	}

	public int getRecordsProcessed() {
		return recordsProcessed;
	}

	public void setRecordsProcessed(int recordsProcessed) {
		this.recordsProcessed = recordsProcessed;
	}

	public TimeValue getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(TimeValue executionTime) {
		this.executionTime = executionTime;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public boolean isHasFailures() {
		return hasFailures;
	}

	public void setHasFailures(boolean hasFailures) {
		this.hasFailures = hasFailures;
	}

	@Override
	public String toString() {
		return "CreateIndexResponseDto [recordsProcessed=" + recordsProcessed + ", indexName=" + indexName
				+ ", executionTime=" + executionTime + ", hasFailures=" + hasFailures + "]";
	}
	
}
