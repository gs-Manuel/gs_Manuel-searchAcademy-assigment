package com.github.gsManuel.APIWEB.controller;

import com.github.gsManuel.APIWEB.service.ElasticService;
import com.github.gsManuel.APIWEB.service.QueriesEngine;
import com.github.gsManuel.APIWEB.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController{
    @Autowired
    private final ElasticService searchService;

    public SearchController(ElasticService searchService) {
        this.searchService = searchService;
    }
    @GetMapping("/{query}")
    public int search(@PathVariable String query){
        return QueryService.search(query);
    }

}
