package com.deepsalunkhee.dailycanvasSever.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestControllers {

    
    
    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Daily Canvas Server is up and running");
    }

    @GetMapping("/user-email")
public ResponseEntity<Map<String, String>> userEmail(HttpServletRequest request) {
    Map<String, String> resultResponse = new HashMap<>();

    // Fetch attributes set by the middleware/interceptor
    String email = (String) request.getAttribute("email");
    String name = (String) request.getAttribute("name");

    // Populate the response map
    resultResponse.put("email", email != null ? email : "");
    resultResponse.put("name", name != null ? name : "");

    return ResponseEntity.ok(resultResponse);
}

    @GetMapping("/tokenStatus")
    public ResponseEntity<String> tokenStatus(HttpServletRequest request) {
        if((boolean) request.getAttribute("tokenStatus")) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
    }



    
}