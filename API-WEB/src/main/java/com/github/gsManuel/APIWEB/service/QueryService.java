package com.github.gsManuel.APIWEB.service;

import co.elastic.clients.elasticsearch.sql.QueryResponse;

public interface QueryService {
    QueryResponse search(String query);
}
