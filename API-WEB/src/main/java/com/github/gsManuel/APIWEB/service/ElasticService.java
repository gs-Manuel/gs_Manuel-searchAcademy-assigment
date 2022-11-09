package com.github.gsManuel.APIWEB.service;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine
public interface ElasticService {
   String getElasticInfo();
}
