package com.deepsalunkhee.dailycanvasSever.controllers;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.deepsalunkhee.dailycanvasSever.models.WeekModel;
import com.deepsalunkhee.dailycanvasSever.services.*;

@RestController
@RequestMapping("/api/v1")
public class WeekControllers {

    //creating logger for this class
     private static final Logger logger = LoggerFactory.getLogger(WeekControllers.class);

   
     private final WeekServices weekServices;

    @Autowired
    public WeekControllers(WeekServices weekServices) {
        this.weekServices = weekServices;
    }

    @PostMapping("/create-week")
    public WeekModel createWeek(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String startDateString = request.get("startDate"); // expected format: "2025-04-13"

        LocalDate startDate = LocalDate.parse(startDateString);

        logger.info("Creating week for user with email: {}", email);
        logger.info("Start date: {}", startDate);
        

        return weekServices.createWeek(email, startDate);
    }


}
