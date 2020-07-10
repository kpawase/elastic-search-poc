package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

public class CreateIndexRequestDto {

	private String indexName;

	private MultipartFile csvFile;

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public MultipartFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}

	@Override
	public String toString() {
		return "CreateIndexRequestDto [indexName=" + indexName + ", csvFile=" + csvFile + "]";
	}

}
