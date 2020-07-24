package com.example.demo.service;

import java.util.Set;

import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;

public interface TransactionService {

	public CreateIndexResponseDto createIndex()
			throws ResourceConflictException, ServiceException;

	public String checkIndexExists() throws ServiceException;
	
	public Set<String> getIndexKeys() throws ServiceException;

	public SearchResponseDto getIndex(SearchRequestDTo requestDTo) throws ServiceException;

	String deleteIndex() throws ServiceException;

}
	