package com.github.gsManuel.APIWEB.controller;

import com.github.gsManuel.APIWEB.model.QueryRespone;
import com.github.gsManuel.APIWEB.service.query.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController{

    private QueryService queriesService;
    public SearchController(QueryService queriesService) {
        this.queriesService = queriesService;
    }
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryRespone search(@RequestParam("query") String query){
        return queriesService.search(query);
    }
  }
