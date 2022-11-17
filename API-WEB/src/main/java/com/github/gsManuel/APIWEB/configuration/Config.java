package com.github.gsManuel.APIWEB.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.gsManuel.APIWEB.service.elastic.ElasticEngine;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.service.query.QueryEngine;
import com.github.gsManuel.APIWEB.service.query.QueryService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class Config {
    @Bean
    public ElasticsearchClient getElasticSearchClient() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200),
                new HttpHost("elasticsearch", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
    @Bean
    public RestClient getRestClient() {
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }
    @Bean
    public ElasticEngine getElasticEngine(ElasticsearchClient client) {
        return new ElasticEngine(client);
    }
    @Bean
    public QueryService getQueryService() {
        return new QueryEngine();
    }
}
