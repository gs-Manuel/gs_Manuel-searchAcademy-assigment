package com.github.gsManuel.APIWEB.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class SearchEngineImpl implements SearchEngine {

    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);

    /**
     * This method makes a request to Elasticsearch to retrieve search results
     * @param query , the query to search
     * @return Length of the query
     */
    @Override
    public int search(String query) {
        if (query == null) {
            throw new RuntimeException("Query is mandatory");
        }
        return query.length();

    }
}
