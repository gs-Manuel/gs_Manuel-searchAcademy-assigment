package com.github.gsManuel.APIWEB.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.gsManuel.APIWEB.service.ElasticEngine;
import com.github.gsManuel.APIWEB.service.ElasticService;
import com.github.gsManuel.APIWEB.service.QueriesEngine;
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
    public QueriesService getSearchService(ElasticEngine elasticEngine) {
        return new QueriesServiceImpl(elasticEngine);
    }
    @Bean
    public QueriesEngine queriesEngineEngine(ElasticEngine elasticEngine){
        return new ElasticEngine(elasticEngine);
    }

    @Bean
    public ElasticEngine searchEngine(SearchEngine searchEngine) {
        return new ElasticService(searchEngine);
    }
}
