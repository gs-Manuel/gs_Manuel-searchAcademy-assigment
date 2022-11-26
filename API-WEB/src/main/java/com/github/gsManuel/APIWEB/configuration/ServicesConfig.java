package com.github.gsManuel.APIWEB.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.github.gsManuel.APIWEB.service.elastic.ElasticEngine;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.service.index.IndexEngine;
import com.github.gsManuel.APIWEB.service.index.IndexService;
import com.github.gsManuel.APIWEB.service.query.QueryEngine;
import com.github.gsManuel.APIWEB.service.query.QueryService;
import com.github.gsManuel.APIWEB.service.search.SearchEngine;
import com.github.gsManuel.APIWEB.service.search.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {
    @Bean
    public QueryService QueryService() {
        return new QueryEngine();
    }
    @Bean
    public IndexService IndexService(ElasticService elasticService) {
        return new IndexEngine(elasticService);
    }
    @Bean
    public ElasticService ElasticService(ElasticsearchClient client) {
        return new ElasticEngine(client);
    }
    @Bean
    public SearchService SearchService( ElasticEngine elasticEngine,
                                        QueryService queryService) {
        return new SearchEngine( elasticEngine, queryService);
    }
}
