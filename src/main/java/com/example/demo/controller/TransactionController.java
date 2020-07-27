package com.example.demo.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.ControllerContants;
import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.DatalistResponseDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;
import com.example.demo.service.TransactionService;
import com.example.demo.util.LoggerUtil;

@CrossOrigin
@RestController
@RequestMapping(value = ControllerContants.TRANSACTION_RESOURCE)
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	@GetMapping(value = ControllerContants.INDEX, produces = "application/json")
	public ResponseDto checkIndexExists(@Valid @RequestHeader Map<String, String> headers) throws ServiceException {

		LoggerUtil.logInfo("::::: Check Index Controller", log);
		String indexName = transactionService.checkIndexExists();
		return new ResponseDto(HttpStatus.OK.value(), indexName == null ? "No index found" : indexName,
				indexName == null ? false : true);

	}

	@GetMapping(value = ControllerContants.getKeys, produces = "application/json")
	public DatalistResponseDto getIndexKeys(@Valid @RequestHeader Map<String, String> headers) throws ServiceException {

		LoggerUtil.logInfo("::::: get Index keys Controller", log);

		return new DatalistResponseDto(HttpStatus.OK.value(), "Index key List", transactionService.getIndexKeys());

	}

	@PostMapping(value = ControllerContants.INDEX, produces = "application/json", consumes = "application/json")
	public ResponseDto createIndex(@Valid @RequestHeader Map<String, String> headers)
			throws ResourceConflictException, ServiceException {

		LoggerUtil.logInfo("::::: Create Index Controller", log);

		CreateIndexResponseDto data = transactionService.createIndex();

		return new ResponseDto(HttpStatus.OK.value(), "Index Created", data);

	}
	
	
	@DeleteMapping(value = ControllerContants.INDEX, produces = "application/json", consumes = "application/json")
	public ResponseDto deleteIndex(@Valid @RequestHeader Map<String, String> headers)
			throws ResourceConflictException, ServiceException {

		LoggerUtil.logInfo("::::: Delete Index Controller", log);

		String data = transactionService.deleteIndex();

		return new ResponseDto(HttpStatus.OK.value(), "Index Deleted", data);

	}

	@PostMapping(value = ControllerContants.INDEX_DATA, produces = "application/json", consumes = "application/json")
	public ResponseDto getIndex(@Valid @RequestHeader Map<String, String> headers,
			@Valid @RequestBody SearchRequestDTo requestDTo) throws ServiceException {
		LoggerUtil.logInfo("::::: Search Index Controller", log);

		SearchResponseDto docs = transactionService.getIndex(requestDTo);

		return new ResponseDto(HttpStatus.OK.value(), "Success", docs);

	}

}
