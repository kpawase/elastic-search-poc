package com.example.demo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constants.ExceptionConstants;
import com.example.demo.dto.CreateIndexRequestDto;
import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.dto.TransactionDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;
import com.example.demo.util.LoggerUtil;
import com.example.demo.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	RestHighLevelClient client;

	@Override
	public CreateIndexResponseDto createIndex(CreateIndexRequestDto createIndexRequestDto)
			throws ResourceConflictException, ServiceException {

		LoggerUtil.logInfo("::::: Create index Service");

		String indexName = createIndexRequestDto.getIndexName();

		try {
			if (!client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT)) {
				CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
				indexRequest.settings(
						Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));

				String indexMapping = getMappings();

				indexRequest.mapping(indexMapping, XContentType.JSON);

				CreateIndexResponse indexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);

				LoggerUtil.logInfo("::::: index created with id " + indexResponse.index());

				BulkRequest bulkRequest = new BulkRequest();

				getCsvRecords(createIndexRequestDto.getCsvFile()).stream()
						.forEach(records -> bulkRequest.add(new IndexRequest(indexName).id(UUID.randomUUID().toString())
								.source(records, XContentType.JSON)));

				LoggerUtil.logInfo("::::: Bulk upload started at :" + System.currentTimeMillis());

				BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

				LoggerUtil.logInfo("::::: Bulk upload Completed at :" + System.currentTimeMillis());

				return new CreateIndexResponseDto(bulkResponse.getItems().length, bulkResponse.getTook(),
						bulkResponse.hasFailures());

			} else {
				throw new ResourceConflictException(ExceptionConstants.INDEX_ALREADY_EXISTS);
			}

		} catch (ResourceConflictException e) {

			LoggerUtil.logError(e.getMessage(), e);
			throw new ResourceConflictException(e.getMessage());
		} catch (IOException e) {

			LoggerUtil.logError(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {

			LoggerUtil.logError(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	private String getMappings() throws IOException {

		LoggerUtil.logInfo("::::: Accessing Index Mapping files");

		ClassPathResource classPathResource = new ClassPathResource("config/mappings.json");
		InputStream inputStream = classPathResource.getInputStream();
		String mappingString = new String(FileCopyUtils.copyToByteArray(inputStream));
		return mappingString;

	}

	private List<String> getCsvRecords(MultipartFile csvFile) throws FileNotFoundException, Exception {

		LoggerUtil.logInfo("::::: Processing Csv file");

		File file = Utils.convertMultiPartToFile(csvFile);

		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("street", "street");
		mapping.put("city", "city");
		mapping.put("zip", "zip");
		mapping.put("state", "state");
		mapping.put("beds", "beds");
		mapping.put("baths", "baths");
		mapping.put("sq__ft", "sq__ft");
		mapping.put("type", "type");
		mapping.put("sale_date", "sale_date");
		mapping.put("price", "price");
		mapping.put("latitude", "latitude");
		mapping.put("longitude", "longitude");

		HeaderColumnNameTranslateMappingStrategy<TransactionDto> strategy = new HeaderColumnNameTranslateMappingStrategy<TransactionDto>();

		strategy.setType(TransactionDto.class);
		strategy.setColumnMapping(mapping);

		List<String> jsonList = new ArrayList<String>();
		Reader csvReader = new FileReader(file);

		CsvToBean<TransactionDto> toBean = new CsvToBeanBuilder<TransactionDto>(csvReader)
				.withType(TransactionDto.class).withMappingStrategy(strategy).build();

		List<TransactionDto> list = toBean.parse();
		ObjectMapper mapper = new ObjectMapper();

		list.stream().forEach(dto -> {
			try {
				jsonList.add(mapper.writeValueAsString(dto));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});

		LoggerUtil.logInfo("::::: Csv file Processed");

		return jsonList;

	}

	@Override
	public SearchResponseDto getIndex(SearchRequestDTo requestDTo) throws ServiceException {

		try {

			LoggerUtil.logInfo("::::: get index Service");

			MatchQueryBuilder builder = new MatchQueryBuilder(requestDTo.getKey(), requestDTo.getValue());

			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

			if (requestDTo.getLimit() > 0)
				sourceBuilder.size(requestDTo.getLimit());

			SearchRequest searchRequest = new SearchRequest(requestDTo.getIndexName())
					.source(sourceBuilder.query(builder));

			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			SearchResponseDto responseDto = new SearchResponseDto();

			responseDto.setStaus(searchResponse.status());

			responseDto.setExecutionTime(searchResponse.getTook());

			responseDto.setFailedShards(searchResponse.getFailedShards());

			responseDto.setSuccessfulShards(searchResponse.getSuccessfulShards());

			responseDto.setNumberOfSearchedHints(searchResponse.getHits().getHits().length);

			ObjectMapper mapper = new ObjectMapper();

			List<TransactionDto> hitLitst = new ArrayList<TransactionDto>();
			Arrays.stream(searchResponse.getHits().getHits()).forEach(hits -> {
				try {
					hitLitst.add(mapper.readValue(hits.getSourceAsString(), TransactionDto.class));
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			});

			responseDto.setSearchResult(hitLitst);
			return responseDto;

		} catch (Exception e) {

			LoggerUtil.logError(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

}
