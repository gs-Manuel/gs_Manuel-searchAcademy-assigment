package com.github.gsManuel.APIWEB.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticEngine implements ElasticService {

    private RestClient client;

    public ElasticEngine(RestClient client) {
        this.client = client;
    }
    @Override
    public String getElasticInfo() {
        Request request = new Request("GET", "/");
        try {
            Response response = client.performRequest(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
