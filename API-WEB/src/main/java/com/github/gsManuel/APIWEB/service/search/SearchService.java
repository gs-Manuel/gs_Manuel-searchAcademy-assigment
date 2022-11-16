package com.github.gsManuel.APIWEB.service.search;

import co.elastic.clients.elasticsearch.sql.QueryResponse;
import com.github.gsManuel.APIWEB.model.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    List<Movie> multiMatch(String query, String fields) throws IOException;

    /**
     * Performs a term query to the movies index
     *
     * @param value - value to search
     * @param field - field to search
     * @return List of movies
     */
    List<Movie> termQuery(String value, String field) throws IOException;

    /**
     * Performs a terms query to the movies index
     *
     * @param values - values to search
     * @param field  - field to search
     * @return List of movies
     */
    List<Movie> termsQuery(String values, String field) throws IOException;
}
