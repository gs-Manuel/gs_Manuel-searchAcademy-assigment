package com.github.gsManuel.APIWEB.service;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine
public class SearchService {

    private final SearchEngine searchEngine;

    public SearchService(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public int search(String query) {
        return searchEngine.search(query);
    }
}
