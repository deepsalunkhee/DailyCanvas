package com.deepsalunkhee.dailycanvasSever.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepsalunkhee.dailycanvasSever.models.UserModel;
import com.deepsalunkhee.dailycanvasSever.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class StarterControllers {

    @Autowired
    private UserServices UserServices;

    @GetMapping("/user-email")
    public ResponseEntity<Map<String, String>> userEmail(HttpServletRequest request) {
        Map<String, String> resultResponse = new HashMap<>();

        // Fetch attributes set by the middleware/interceptor
        String email = (String) request.getAttribute("email");
        String name = (String) request.getAttribute("name");

        //check if user exits using email 
        Optional<UserModel> userstatus= UserServices.getUserByEmail(email);
        if(userstatus.isEmpty()){
            UserModel user = new UserModel();
            user.setEmail(email);
            user.setName(name);
            UserServices.saveUser(user);
        }

    

        // Populate the response map
        resultResponse.put("email", email != null ? email : "");
        resultResponse.put("name", name != null ? name : "");

        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("/tokenStatus")
    public ResponseEntity<String> tokenStatus(HttpServletRequest request) {
        if ((boolean) request.getAttribute("tokenStatus")) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
    }
}
