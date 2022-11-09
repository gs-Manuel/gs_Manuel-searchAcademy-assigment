package com.github.gsManuel.APIWEB.controller;

import co.elastic.clients.elasticsearch.sql.QueryResponse;
import com.github.gsManuel.APIWEB.service.QueryService;
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
    public QueryResponse search(@RequestParam("query") String query){
        return queriesService.search(query);
    }
  }
