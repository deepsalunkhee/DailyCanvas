package com.deepsalunkhee.dailycanvasSever.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deepsalunkhee.dailycanvasSever.models.DayModel;
import com.deepsalunkhee.dailycanvasSever.models.WeekModel;
import com.deepsalunkhee.dailycanvasSever.repository.DayRepo;
import com.deepsalunkhee.dailycanvasSever.repository.WeekRepo;

@Service
public class DayServices {
     private final DayRepo dayRepo;
    private final WeekRepo weekRepo;

    public DayServices(DayRepo dayRepo, WeekRepo weekRepo) {
        this.dayRepo = dayRepo;
        this.weekRepo = weekRepo;
    }

    public DayModel createDay(UUID weekId, DayModel day) {
        Optional<WeekModel> weekOptional = weekRepo.findById(weekId);
        if (weekOptional.isEmpty()) {
            throw new IllegalArgumentException("Week not found");
        }

        day.setWeek(weekOptional.get());
        return dayRepo.save(day);
    }

    public List<DayModel> getDaysByWeekId(UUID weekId) {
        return dayRepo.findByWeekId(weekId);
    }

    public void deleteDay(UUID dayId) {
        dayRepo.deleteById(dayId);
    }

    public DayModel getDayById(UUID dayId) {
        return dayRepo.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Day not found"));
    }

}
