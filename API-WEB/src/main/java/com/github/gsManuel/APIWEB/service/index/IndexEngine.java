package com.github.gsManuel.APIWEB.service.index;


import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.util.IMDbReader;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public class IndexEngine implements IndexService {

    private final ElasticService elasticService;

    public IndexEngine(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    /**
     * Creates an index in elastic
     *
     * @throws IOException - if the index cannot be created
     */
    @Override
    public void createIndex() throws IOException {
        elasticService.createIndex();
        elasticService.putSettings();
        elasticService.putMapping();
    }

    /**
     * Indexes a document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    @Override
    public void indexDocument(Movie movie) throws IOException {
        elasticService.indexDocument(movie);
    }

    /**
     * Indexes imdb data from a file
     *
     * @param basicsFile     File with the imdb basics data
     * @param ratingsFile    File with the imdb ratings data
     * @param akasFile       File with the imdb akas data
     * @param crewFile       File with the imdb crew data
     * @param principalsFile File with the imdb principals data
     * @return True if the data was indexed correctly, false otherwise
     * @throws IOException        - if the data cannot be indexed
     */
    @Override
    public void indexImdbData(MultipartFile basicsFile, MultipartFile ratingsFile,
                              MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile)
            throws IOException {
        IMDbReader reader = new IMDbReader(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);

        while (reader.hasDocuments()) {
            List<Movie> movies = reader.readDocuments();
            elasticService.indexBulk(movies);
        }
    }

}

