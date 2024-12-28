package com.deepsalunkhee.dailycanvasSever.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestConrollers {
    

    @GetMapping("/open")
    public String test() {
        return "Hello World";
    }
}
