package com.github.gsManuel.APIWEB.controller;

import com.github.gsManuel.APIWEB.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController{
    @Autowired
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @GetMapping("/{query}")
    public int search(@PathVariable String query){
        return searchService.search(query);
    }
    @RequestMapping(value = "?query={query}", method = RequestMethod.GET)
    public ResponseEntity search1(@RequestParam (value = "query") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello "+ name;
    }

}
