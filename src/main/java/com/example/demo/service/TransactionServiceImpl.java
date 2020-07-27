package com.example.demo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.demo.constants.ElasticSearchConstants;
import com.example.demo.dto.CreateIndexResponseDto;
import com.example.demo.dto.SearchRequestDTo;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.dto.TransactionDto;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ServiceException;
import com.example.demo.util.LoggerUtil;
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

	@Value("${index.name}")
	private String indexName;

	@Value("${index.number_of_shards}")
	private String noOfShards;

	@Value("${index.number_of_replicas}")
	private String noOfReplicas;

	private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

	/*
	 * @PostConstruct public void createIndexAuto() throws
	 * ResourceConflictException, ServiceException {
	 * LoggerUtil.logInfo("Creating Index", log); this.createIndex(); }
	 */

	@Override
	public CreateIndexResponseDto createIndex() throws ResourceConflictException, ServiceException {
		LoggerUtil.logInfo("::::: Create index Service", log);
		
		
		indexName = System.getenv("index.name") == null ? indexName : System.getenv("index.name");
		noOfReplicas = System.getenv("index.number_of_replicas") == null ? noOfReplicas
				: System.getenv("index.number_of_replicas");
		noOfShards = System.getenv("index.number_of_shards") == null ? noOfShards
				: System.getenv("index.number_of_shards");
		try {

			if (client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT)) {
				LoggerUtil.logInfo("::::::Index Already Present, Skipping index creation", log);
				return null;

			}
			CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
			indexRequest.settings(Settings.builder().put(ElasticSearchConstants.NO_OF_SHARDS, noOfShards)
					.put(ElasticSearchConstants.NO_OF_REPLICAS, noOfReplicas));

			String indexMapping = getMappings();

			indexRequest.mapping(indexMapping, XContentType.JSON);

			CreateIndexResponse indexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);

			LoggerUtil.logInfo("::::: index created with id " + indexResponse.index(), log);

			BulkRequest bulkRequest = new BulkRequest();

			getCsvRecords(new ClassPathResource("data/transaction.csv").getInputStream()).stream()
					.forEach(records -> bulkRequest.add(new IndexRequest(indexName).id(UUID.randomUUID().toString())
							.source(records, XContentType.JSON)));

			LoggerUtil.logInfo("::::: Bulk upload started at :" + System.currentTimeMillis(), log);

			BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

			LoggerUtil.logInfo("::::: Bulk upload Completed at :" + System.currentTimeMillis(), log);

			return new CreateIndexResponseDto(bulkResponse.getItems().length, bulkResponse.getTook(), indexName,
					bulkResponse.hasFailures());

		} catch (ResourceConflictException e) {

			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ResourceConflictException(e.getMessage());
		} catch (IOException e) {
			this.deleteIndex();
			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			this.deleteIndex();
			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		}

	}

	private String getMappings() throws IOException {

		LoggerUtil.logInfo("::::: Accessing Index Mapping files", log);

		ClassPathResource classPathResource = new ClassPathResource("config/mappings.json");
		InputStream inputStream = classPathResource.getInputStream();
		String mappingString = new String(FileCopyUtils.copyToByteArray(inputStream));
		return mappingString;

	}

	private List<String> getCsvRecords(InputStream in) throws FileNotFoundException, Exception {

		LoggerUtil.logInfo("::::: Processing Csv file", log);

		File file = File.createTempFile("temp", "csv");
		Files.copy(in, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("Account No", "accountNo");
		mapping.put("DATE", "date");
		mapping.put("TRANSACTION DETAILS", "transactionDetails");
		mapping.put("CHQ.NO.", "chequeNo");
		mapping.put("VALUE DATE", "valueDate");
		mapping.put("WITHDRAWAL AMT", "withdrawalAmount");
		mapping.put("DEPOSIT AMT", "depositAmount");
		mapping.put("BALANCE AMT", "balance");

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

		LoggerUtil.logInfo("::::: Csv file Processed with records : " + jsonList.size(), log);

		return jsonList;

	}

	@Override
	public String deleteIndex() throws ServiceException {

		LoggerUtil.logInfo(":::::: Deleting index..."+indexName , log);
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
			AcknowledgedResponse response = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
			LoggerUtil.logInfo("::::::  Index Deleted..."+indexName , log);
			return response.toString();

		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public SearchResponseDto getIndex(SearchRequestDTo requestDTo) throws ServiceException {

		try {

			LoggerUtil.logInfo("::::: Search index Service", log);
			LoggerUtil.logInfo(":::::: SearchRequestDto :" + requestDTo.toString(), log);

			BoolQueryBuilder builder = new BoolQueryBuilder();

			if (requestDTo.getCriteria().equalsIgnoreCase("should_match"))
				builder.should(new MatchQueryBuilder(requestDTo.getKey(), requestDTo.getValue()));

			else if (requestDTo.getCriteria().equalsIgnoreCase("must_match")) {
				builder.must(new MatchQueryBuilder(requestDTo.getKey(), requestDTo.getValue()));
			}

			else if (requestDTo.getCriteria().equalsIgnoreCase("must_not")) {
				builder.mustNot(new MatchQueryBuilder(requestDTo.getKey(), requestDTo.getValue()));
			} else {
				builder.should(QueryBuilders.matchAllQuery());

			}

			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

			if (requestDTo.getLimit() > 0)
				sourceBuilder.size(requestDTo.getLimit());

			SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder.query(builder));

			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			SearchResponseDto responseDto = new SearchResponseDto();

			responseDto.setStaus(searchResponse.status());

			responseDto.setExecutionTime(searchResponse.getTook());

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

			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public String checkIndexExists() throws ServiceException {

		LoggerUtil.logInfo(":::::: Checking index's presence", log);

		try {

			if (client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT))
				return indexName;
		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getIndexKeys() throws ServiceException {

		LoggerUtil.logInfo(":::::: Get index keys service", log);
		try {
			
			if(!client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT))
				return new HashSet<String>();
			
			GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
			getMappingsRequest.indices(indexName);

			GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest,
					RequestOptions.DEFAULT);
			Map<String, MappingMetaData> allMappings = getMappingsResponse.mappings();

			System.out.println("indexName" + indexName);

			MappingMetaData indexMapping = allMappings.get(indexName);
			Map<String, Object> mapping = indexMapping.sourceAsMap();
			LinkedHashMap<String, Object> maps = (LinkedHashMap<String, Object>) mapping.get("properties");

			return maps.keySet();

		} catch (Exception e) {
			LoggerUtil.logError(e.getMessage(), e, log);
			throw new ServiceException(e.getMessage());
		}

	}

}
