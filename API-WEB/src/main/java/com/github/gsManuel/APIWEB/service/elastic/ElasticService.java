package com.github.gsManuel.APIWEB.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.gsManuel.APIWEB.model.Movie;

import java.io.IOException;
import java.util.List;
public interface ElasticService {
    String getElasticInfo();
    /**
     * Creates an index using the elastic client
     *
     * @throws IOException - if the index cannot be created
     */
    void createIndex() throws IOException;

    /**
     * Puts the settings of the index
     *
     * @throws IOException - If the settings cannot be loaded
     */
    void putSettings() throws IOException;

    /**
     * Puts the mapping of the index
     *
     * @throws IOException If the mapping cannot be loaded
     */
    void putMapping() throws IOException;

    /**
     * Indexes a movie document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(Movie movie) throws IOException;

    /**
     * Creates a multi match query
     *
     * @param query  - query to search
     * @param fields - fields to search
     * @return Query to be executed
     */
    Query multiMatch(String query, String[] fields);

    /**
     * Performs a term query
     *
     * @param field - field to search
     * @param value - value to search
     * @return Query to be executed
     */
    Query termQuery(String value, String field);

    /**
     * Performs a terms query
     *
     * @param values Values to search
     * @param field  Field to search
     * @return Query to be executed
     */
    Query termsQuery(String[] values, String field);

    /**
     * Makes a query to elasticsearch
     *
     * @param query Query to make
     * @return List of movies that match the query
     */
    List<Movie> performQuery(Query query);

    /**
     * Indexes a list of movies
     *
     * @param movies Movies to index
     * @return True if the movies were indexed, false otherwise
     */
    boolean indexBulk(List<Movie> movies);
}
