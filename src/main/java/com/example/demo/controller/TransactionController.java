package com.example.demo.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ControllerContants;
import com.example.demo.dto.CreateIndexRequestDto;
import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;
import com.example.demo.service.TransactionService;
import com.example.demo.util.LoggerUtil;

@RestController
@RequestMapping(value = ControllerContants.TRANSACTION_RESOURCE)
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@PostMapping(value = ControllerContants.INDEX, produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseDto createIndex(@Valid @RequestHeader Map<String, String> headers,
			@Valid @ModelAttribute CreateIndexRequestDto indexRequestDto)
			throws ResourceConflictException, ServiceException {

		LoggerUtil.logInfo("::::: Create Index Controller");

		CreateIndexResponseDto data = transactionService.createIndex(indexRequestDto);

		return new ResponseDto(HttpStatus.OK.value(), "Index Created", data);

	}

	@PostMapping(value = ControllerContants.INDEX_DATA, produces = "application/json", consumes = "application/json")
	public ResponseDto getIndex(@Valid @RequestHeader Map<String, String> headers,
			@Valid  @RequestBody SearchRequestDTo requestDTo) throws ServiceException {
		LoggerUtil.logInfo("::::: Get Index Controller");

		SearchResponseDto docs = transactionService.getIndex(requestDTo);

		return new ResponseDto(HttpStatus.OK.value(), "Success", docs);

	}
}