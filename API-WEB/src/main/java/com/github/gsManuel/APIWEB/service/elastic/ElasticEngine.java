package com.github.gsManuel.APIWEB.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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
    private static final String INDEX_NAME = "imdb";
    private final ElasticsearchClient client;

    public ElasticEngine(ElasticsearchClient client) {
        this.client = client;
    }
    @Override
    public void createIndex() throws IOException {
        try {
            client.indices().delete(d -> d.index(INDEX_NAME));
        } catch (Exception e) {
            // Ignore
        }

        client.indices().create(c -> c.index(INDEX_NAME));
    }
    @Override
    public void putSettings() throws IOException {
        client.indices().close(c -> c.index(INDEX_NAME));
        InputStream analyzer = getClass().getClassLoader().getResourceAsStream("custom_analyzer.json");
        client.indices().putSettings(p -> p.index(INDEX_NAME).withJson(analyzer));
        client.indices().open(o -> o.index(INDEX_NAME));
    }

    @Override
    public void putMapping() throws IOException {
        InputStream mapping = getClass().getClassLoader().getResourceAsStream("mapping.json");
        client.indices().putMapping(p -> p.index(INDEX_NAME).withJson(mapping));
    }

    @Override
    public void indexDocument(Movie movie) throws IOException {
        client.index(i -> i
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
            SearchResponse<Movie> response = client.search(s -> s
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
            BulkResponse bulkResponse = client.bulk(request.build());
            return !bulkResponse.errors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
