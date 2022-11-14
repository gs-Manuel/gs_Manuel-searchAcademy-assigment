package com.github.gsManuel.APIWEB.util;

import com.github.gsManuel.APIWEB.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImdbReader {
    private final BufferedReader imdbReader;
    private final int documentsSize = 20000;

    private boolean hasDocuments = true;


    public ImdbReader(MultipartFile imdbFile) {
        try {
            this.imdbReader = new BufferedReader(new InputStreamReader(imdbFile.getInputStream()));
            imdbReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a list of documents from the file and converts it to movies
     * @return a list of movies from the file
     */
    public List<Movie> readDocuments() {
        List<Movie> movies = new ArrayList<>();
        int currentLinesRead = 0;
        while (currentLinesRead < documentsSize) {
            String line = null;
            try {
                line = imdbReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                this.hasDocuments = false;
                return movies;
            }
            movies.add(createMovie(line));
            currentLinesRead++;
        }
        return movies;
    }
    /**
     * Parse a line with Movie.class parameters
     * @param line : current line from the file that we're reading
     * @return movie : to be added to the bulk list
     */
    private Movie createMovie(String line) {
        String[] fields = line.split("\t");
        Movie movie = new Movie(fields[0], fields[1], fields[2], fields[3], fields[4].contentEquals("1"),
                fields[5].equals("\\N") ? 0 : Integer.parseInt(fields[5]),
                fields[6].equals("\\N") ? 0 : Integer.parseInt(fields[6]),
                fields[7].equals("\\N") ? 0 : Integer.parseInt(fields[7]),
                fields[8]);
        return movie;
    }

    public boolean hasDocuments() {
        return hasDocuments;
    }
}
