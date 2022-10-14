package com.github.gsManuel.APIWEB.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.web.bind.annotation.RestController

public class RestController {
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello "+ name;
    }
}

