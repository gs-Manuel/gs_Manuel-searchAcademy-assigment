package com.github.gsManuel.APIWEB.service.elastic;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.github.gsManuel.APIWEB.model.Movie;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticEngine implements ElasticService {

    private RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200),
            new HttpHost("elasticsearch", 9200)).build();
    private static final String INDEX_NAME = "imdb";
    public ElasticEngine(RestClient client) {
        this.restClient = restClient;
    }
    @Override
    public String getElasticInfo() {
        Request request = new Request("GET", "/");
        try {
            Response response = restClient.performRequest(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createIndex() throws IOException {
        try {
            restClient.indices().delete(d -> d.index(INDEX_NAME));
        } catch (Exception e) {
            // Ignore
        }

        restClient.indices().create(c -> c.index(INDEX_NAME));
    }
    @Override
    public void putSettings() throws IOException {
        restClient.indices().close(c -> c.index(INDEX_NAME));
        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("custom_analyzer.json");
        restClient.indices().putSettings(p -> p.index(INDEX_NAME).withJson(analyzer));
        restClient.indices().open(o -> o.index(INDEX_NAME));
    }

    @Override
    public void putMapping() throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("mapping.json");
        restClient.indices().putMapping(p -> p.index(INDEX_NAME).withJson(mapping));
    }

    @Override
    public void indexDocument(Movie movie) throws IOException {
        restClient.index(i -> i
                .index(INDEX_NAME)
                .id(movie.getTconst())
                .document(movie));
    }

    @Override
    public Query multiMatch(String query, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }

    @Override
    public Query termQuery(String value, String field) {
        Query termQuery = TermQuery.of(t -> t
                .value(value)
                .field(field))._toQuery();

        return termQuery;
    }

    @Override
    public Query termsQuery(String[] values, String field) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .field(field)
                .terms(termsQueryField))._toQuery();

        return termsQuery;
    }

    @Override
    public List<Movie> performQuery(Query query) {
        try {
            SearchResponse<Movie> response = restClient.search(s -> s
                    .index(INDEX_NAME)
                    .query(query), Movie.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean indexBulk(List<Movie> movies) {
        BulkRequest.Builder request = new BulkRequest.Builder();

        movies.forEach(movie -> request.operations(op -> op
                .index(i -> i
                        .index(INDEX_NAME)
                        .id(movie.getTconst())
                        .document(movie))));

        try {
            BulkResponse bulkResponse = restClient.bulk(request.build());
            return !bulkResponse.errors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
