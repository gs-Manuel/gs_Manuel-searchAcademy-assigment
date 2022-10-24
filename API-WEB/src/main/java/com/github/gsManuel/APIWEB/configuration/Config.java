package com.github.gsManuel.APIWEB.configuration;

import com.github.gsManuel.APIWEB.service.SearchEngine;
import com.github.gsManuel.APIWEB.service.SearchEngineImpl;
import com.github.gsManuel.APIWEB.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This is the configuration class where we'll define our beans (objects whose lifecycle is managed by Spring)
@Configuration
public class Config {

    @Bean
    public SearchEngine searchEngine() {
        return new SearchEngineImpl();
    }

    @Bean
    public SearchService searchService(SearchEngine searchEngine) {
        return new SearchService(searchEngine);
    }
}
