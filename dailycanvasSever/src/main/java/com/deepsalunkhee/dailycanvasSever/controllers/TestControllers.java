package com.deepsalunkhee.dailycanvasSever.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllers {

    
    
    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Daily Canvas Server is up and running");
    }

    
}