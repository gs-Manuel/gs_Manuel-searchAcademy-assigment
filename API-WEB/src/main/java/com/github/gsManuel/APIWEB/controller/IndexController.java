package com.github.gsManuel.APIWEB.controller;
import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.service.index.IndexService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class IndexController {
    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * PUT /index/create - Creates a new index
     *
     * @return ResponseEntity - 200 if the index was created, 400 if the index already exists and 500 if there was an error
     */
    @PutMapping("/create")
    public ResponseEntity<String> createIndex() {
        try {
            indexService.createIndex();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error creating index");
        }

        return ResponseEntity.ok("Index created");
    }

    /**
     * POST /index - Indexes a new document
     * @param movie - movie to be indexed
     * @return ResponseEntity - 200 if the document was indexed and 500 if there was an error
     */
    @PostMapping("/{indexName}")
    public ResponseEntity<Movie> indexDocument(@RequestBody Movie movie) {
        try {
            indexService.indexDocument(movie);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(null).body(movie);
    }

    /**
     * POST /index/imdb - Indexes imdb data in the index
     *
     * @param file - Title basics file containing the data to be indexed
     */
    @PostMapping("/imdb")
    public ResponseEntity indexImdbData(@RequestParam("file") MultipartFile file) {
        indexService.indexImdbData(file);
        return ResponseEntity.accepted().build();
    }

}
