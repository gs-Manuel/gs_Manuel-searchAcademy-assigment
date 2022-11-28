package com.github.gsManuel.APIWEB.service.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import com.github.gsManuel.APIWEB.model.Movie;
import com.github.gsManuel.APIWEB.model.Response;
import com.github.gsManuel.APIWEB.service.elastic.ElasticEngine;
import com.github.gsManuel.APIWEB.service.elastic.ElasticService;
import com.github.gsManuel.APIWEB.util.ImdbReader;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class IndexEngine implements IndexService {


    private final int SUCCESS_CODE = 200;
    private final int BAD_REQUEST_CODE = 400;
    private final int CLIENT_ERROR_CODE = 403;
    private final int SERVER_ERROR_CODE = 500;
    private final int MAX_LINE_COUNTER = 20000;
    private final String IMDB_INDEX = "imdb";
    private final int TOP_MAX_COUNT = 100;
    private final ElasticService elasticService;
    @Autowired
    private ElasticsearchClient elastic;
    @Autowired
    private RestClient client;
    public IndexEngine(ElasticService elasticService) {
        this.elasticService = elasticService;
    }
    @Override
    public Response createIndex(String indexName, String body) throws IOException {
        // First checks parameters
        if(indexName == null)
            return new Response(BAD_REQUEST_CODE, "ERROR: missing required parameter <indexName>");
        if (body == null) {
            return new Response(BAD_REQUEST_CODE, "ERROR: missing JSON body in PUT request.");
        }
        // Creates index request with default settings
        InputStream mappings = getClass().getClassLoader().getResourceAsStream("default-mappings.json");

        // Opens input stream for the request body
        //InputStream mappings = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        // Fills index request with the contents for the new indexName
        CreateIndexRequest request = CreateIndexRequest.of(b -> b.index(indexName).withJson(mappings));

        try {
            // Creates the index, and checks it's been created correctly
            if(elastic.indices().create(request).acknowledged())
                return new Response(SUCCESS_CODE, " * Index '"+indexName+"' created correctly.");
        } catch (IOException e) {
            return new Response(SERVER_ERROR_CODE, "ERROR: can't connect to server.");
        } catch (Exception e){
            return new Response(BAD_REQUEST_CODE, "ERROR: index with <indexName> = '"+indexName+"', already exists.");
        }
        // Can't create index for unhandled reasons
        return new Response(BAD_REQUEST_CODE, "ERROR: can't index '"+indexName+"'.");
    }

    @Override
    public void indexDocument(Movie movie) throws IOException {
        elasticService.indexDocument(movie);
    }

    @Override
    public Response indexImdbData(MultipartFile basics, MultipartFile crew, MultipartFile akas, MultipartFile ratings) {
        if(basics.isEmpty())
            return new Response(SUCCESS_CODE, "Nothing to index.");
        List<Movie> movies = new ArrayList<Movie>();
        InputStream stream = null;

        try {
            // Check if there's already an index with name IMDB_INDEX
            org.elasticsearch.client.Response response = client.performRequest(new Request("HEAD", "/" + IMDB_INDEX));
            if (response.getStatusLine().getStatusCode() == 200){
                // If there's an index with that name, we delete it
                elastic.indices().delete(d -> d.index(IMDB_INDEX));
            }
            // We create the IMDB index, with default configuration and mappings
            createIndex(IMDB_INDEX, "");

            // Read all docs given, and indexing all the contents
            indexAllDocsImdb(basics, crew, akas, ratings);
            return new Response(SUCCESS_CODE, "All movies from '"+basics.getOriginalFilename()+
                    "' were successfully indexed into '"+IMDB_INDEX+"' index.");
        } catch (IOException e) {
            return new Response(BAD_REQUEST_CODE, "ERROR while indexing file '"+basics.getOriginalFilename()+"'");
        }
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
    /**
     * Auxiliar function to bulk index the contents of different files to a new index
     * @param basicsFile : file with basic info to bulk index
     * @param crewFile : file with crew info to bulk index
     * @param akasFile : file with akas info to bulk index
     * @param ratingsFile : file with ratings info to bulk index
     * @param starringFile : file with ratings info to bulk index
     * @return SimpleResponse with right status and custom body
     */
    public void indexAllDocsImdb(MultipartFile basicsFile, MultipartFile crewFile, MultipartFile akasFile,
                                 MultipartFile ratingsFile){
        List<Movie> movies = new ArrayList<Movie>();
        try {
            // First we initialize our BufferReaders
            BufferedReader basics = new BufferedReader(new InputStreamReader(basicsFile.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader crew = new BufferedReader(new InputStreamReader(crewFile.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader akas = new BufferedReader(new InputStreamReader(akasFile.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader ratings = new BufferedReader(new InputStreamReader(ratingsFile.getInputStream(), StandardCharsets.UTF_8));

            // And skip the first lines with the headers
            basics.readLine();
            crew.readLine();
            akas.readLine();
            ratings.readLine();

            int lineCounter = 0;
            String currentMovie;
            while((currentMovie = basics.readLine()) != null){
                addMovie(currentMovie, akas, crew, ratings, movies);
                lineCounter++;
                // Index bulks of MAX_LINE_COUNTER movies
                if(lineCounter == MAX_LINE_COUNTER){
                    indexNewBulk(movies);
                    movies = new ArrayList<Movie>();
                    lineCounter = 0;
                }
            }
            // Index the last part of the bulk
            indexNewBulk(movies);

            // Close streams
            basics.close();
            crew.close();
            akas.close();
            ratings.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /**
         * Bulk index a list of movies
         * @param movies : current bulk list
         */
        public void indexNewBulk(List<Movie> movies) throws IOException {
            BulkRequest.Builder br = new BulkRequest.Builder();

            for (Movie movie : movies) {
                br.operations(op -> op
                        .index(idx -> idx
                                .index(IMDB_INDEX)
                                .id(movie.getTconst())
                                .document(movie)
                        )
                );
            }

            BulkResponse result = elastic.bulk(br.build());
        }
    }
}

