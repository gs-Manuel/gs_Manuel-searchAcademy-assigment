package com.github.gsManuel.APIWEB.controller;
import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.model.Response;
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
    @PutMapping("/{indexName}")
    public ResponseEntity<String> createIndex(@PathVariable String indexName, @RequestBody String body)  {
        try {
            indexService.createIndex();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(200).body("Index created");
    }

    /**
     * POST /index - Indexes a new document
     *
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
     * POST request that bulk indexes all contents of a given file
     * Form: POST /index/imdb {JSON body}
     *
     * @param basicsFile  : file with basic info to bulk index
     * @param crewFile    : file with crew info to bulk index
     * @param akasFile    : file with akas info to bulk index
     * @param ratingsFile : file with ratings info to bulk index
     * @return ResponseEntity with right status and custom body
     */
    @PostMapping("/imdb")
    public ResponseEntity indexImdbData(@RequestParam("basics") MultipartFile basicsFile,
                                        @RequestParam("ratings") MultipartFile ratingsFile,
                                        @RequestParam("akas") MultipartFile akasFile,
                                        @RequestParam("crew") MultipartFile crewFile,
                                        @RequestParam("principals") MultipartFile principalsFile) {
        try {
            indexService.indexImdbData(basicsFile, ratingsFile, akasFile, crewFile, principalsFile);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.accepted().build();
    }
}
