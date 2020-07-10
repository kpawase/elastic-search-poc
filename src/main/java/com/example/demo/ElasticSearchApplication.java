package com.example.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.demo.util.LoggerUtil;

@SpringBootApplication(scanBasePackages = { "com.example.demo" })
@EnableAsync
@EnableConfigurationProperties
public class ElasticSearchApplication {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchApplication.class, args);
	}

	@Bean
	public RestHighLevelClient restHighLevelClient() {
		
		RestClientBuilder builder = RestClient
				.builder(new HttpHost( System.getenv("elasticsearch.host")==null ? host : System.getenv("elasticsearch.host") , 9200));

		RestHighLevelClient client = new RestHighLevelClient(builder);

		return client;

	}
}
