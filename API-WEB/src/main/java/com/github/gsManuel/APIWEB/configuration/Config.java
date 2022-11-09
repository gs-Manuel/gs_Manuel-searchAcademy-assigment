package com.github.gsManuel.APIWEB.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.gsManuel.APIWEB.controller.SearchController;
import com.github.gsManuel.APIWEB.service.ElasticEngine;
import com.github.gsManuel.APIWEB.service.ElasticService;
import com.github.gsManuel.APIWEB.service.QueryEngine;
import com.github.gsManuel.APIWEB.service.QueryService;
import io.micrometer.core.instrument.search.Search;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This is the configuration class where we'll define our beans (objects whose lifecycle is managed by Spring)
@Configuration
public class Config {

    @Bean
    public ElasticsearchClient getElasticSearchClient(){
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
    @Bean
    public RestClient getRestClient(){
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }
    @Bean
    public ElasticService getElasticService(RestClient restClient) {
        return new ElasticEngine(restClient);
    }
    @Bean
    public QueryService getSearchService(ElasticService elasticService) {
        return new QueryEngine(elasticService);
    }
    @Bean
    public SearchController getSearchController(QueryService queryService) {
        return new SearchController(queryService);
    }

}
