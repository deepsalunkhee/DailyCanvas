package com.deepsalunkhee.dailycanvasSever.services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deepsalunkhee.dailycanvasSever.models.UserModel;
import com.deepsalunkhee.dailycanvasSever.models.WeekModel;
import com.deepsalunkhee.dailycanvasSever.repository.UserRepo;
import com.deepsalunkhee.dailycanvasSever.repository.WeekRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class WeekServices {
    
    private final WeekRepo weekRepo;
    private final UserRepo userRepo;

    public WeekServices(WeekRepo weekRepo, UserRepo userRepo) {
        this.weekRepo = weekRepo;
        this.userRepo = userRepo;
    }

    public WeekModel createWeek(String email, LocalDate startDate) {
    UserModel user = userRepo.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    WeekModel week = new WeekModel();
    week.setUser(user);
    week.setStartDate(startDate);

    user.getWeeks().add(week);

    return weekRepo.save(week);
}

    public WeekModel getWeeksById(UUID weekId) {
        return weekRepo.findByid(weekId);
    }

    public WeekModel getWeekById(UUID weekId) {
        return weekRepo.findById(weekId)
                .orElseThrow(() -> new IllegalArgumentException("Week not found"));
    }

    public void deleteWeek(UUID weekId) {
        weekRepo.deleteById(weekId);
    }

     public void updateBrainSorterData(UUID weekId, String brainSorterData) {
        WeekModel week = weekRepo.findById(weekId)
                .orElseThrow(() -> new RuntimeException("Week not found"));

        week.setBrainSorterData(brainSorterData);
        weekRepo.save(week);
    }
}
