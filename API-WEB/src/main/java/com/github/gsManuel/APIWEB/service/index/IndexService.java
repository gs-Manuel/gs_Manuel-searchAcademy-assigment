package com.github.gsManuel.APIWEB.service.index;

import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.model.Response;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IndexService {

    /**
     * Creates an index
     *
     * @throws IOException - if the index cannot be created
     */
    void createIndex() throws IOException;

    /**
     * Indexes a document
     *
     * @param movie - movie to be indexed
     * @throws IOException - if the document cannot be indexed
     */
    void indexDocument(Movie movie) throws IOException;

    void indexImdbData(MultipartFile basicsFile, MultipartFile ratingsFile,
                       MultipartFile akasFile, MultipartFile crewFile, MultipartFile principalsFile)
            throws IOException;
}
