package com.example.demo.service;

import com.example.demo.dto.CreateIndexRequestDto;
import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;

public interface TransactionService {

	public CreateIndexResponseDto createIndex(CreateIndexRequestDto createIndexRequestDto) throws ResourceConflictException, ServiceException;
	
	public SearchResponseDto getIndex(SearchRequestDTo requestDTo) throws ServiceException;
	
}
