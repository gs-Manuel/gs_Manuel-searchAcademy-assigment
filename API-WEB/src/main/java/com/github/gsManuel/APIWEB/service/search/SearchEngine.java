package com.github.gsManuel.APIWEB.service.search;

import co.elastic.clients.elasticsearch.sql.QueryResponse;
import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.service.query.QueryService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;

public class SearchEngine implements SearchService {

    private final ElasticService elasticService;

    private final QueryService queryService;

    public SearchEngine(ElasticService elasticService, QueryService queryService) {
        this.elasticService = elasticService;
        this.queryService = queryService;
    }
    /**
     * Performs a multi match query to the movies index
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return List of movies
     */
    @Override
    public List<Movie> multiMatch(String query, String fields) throws IOException {
        String[] fieldsArray = fields.split(",");
        return elasticService.performQuery(queryService.multiMatch(query, fieldsArray));
    }

    /**
     * Performs a term query to the movies index
     *
     * @param value - value to search
     * @param field - field to search
     * @return List of movies
     */
    @Override
    public List<Movie> termQuery(String value, String field) throws IOException {
        return elasticService.performQuery(queryService.termQuery(value, field));
    }

    /**
     * Performs a terms query to the movies index
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of movies
     */
    @Override
    public List<Movie> termsQuery(String values, String field) throws IOException {
        String[] valuesArray = values.split(",");
        return elasticService.performQuery(queryService.termsQuery(valuesArray, field));
    }
}

