package com.github.gsManuel.APIWEB.service.index;

import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.service.elastic.ElasticEngine;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.util.ImdbReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class IndexEngine implements IndexService {


    private final ElasticService elasticService;

    public IndexEngine(ElasticService elasticService) {
        this.elasticService = elasticService;
    }
    @Override
    public void createIndex() throws IOException {
        elasticService.createIndex();
        elasticService.putSettings();
        elasticService.putMapping();
    }

    @Override
    public void indexDocument(Movie movie) throws IOException {
        elasticService.indexDocument(movie);
    }

    @Override
    public boolean indexImdbData(MultipartFile file) {
        ImdbReader reader = new ImdbReader(file);

        while (reader.hasDocuments()) {
            List<Movie> movies = reader.readDocuments();
            boolean result = elasticService.indexBulk(movies);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}

