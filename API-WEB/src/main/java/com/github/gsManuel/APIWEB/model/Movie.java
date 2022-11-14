package com.github.gsManuel.APIWEB.model;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private Boolean isAdult;
    private int startYear;
    private int endYear;
    private int runtimeMinutes;
    private  String genres;
}
