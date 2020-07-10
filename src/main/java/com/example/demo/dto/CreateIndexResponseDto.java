package com.example.demo.dto;

import org.elasticsearch.common.unit.TimeValue;

public class CreateIndexResponseDto {
	
	private int recordsProcessed;
	
	private TimeValue executionTime;
	
	private boolean hasFailures;
	
	

	public CreateIndexResponseDto(int recordsProcessed, TimeValue executionTime, boolean hasFailures) {
		super();
		this.recordsProcessed = recordsProcessed;
		this.executionTime = executionTime;
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

	public boolean isHasFailures() {
		return hasFailures;
	}

	public void setHasFailures(boolean hasFailures) {
		this.hasFailures = hasFailures;
	}
	
	
	 


	
	

}
