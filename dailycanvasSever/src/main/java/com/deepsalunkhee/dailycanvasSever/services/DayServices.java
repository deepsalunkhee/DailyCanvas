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

    public DayModel createDay(UUID weekId, String dayOfWeek) {
        WeekModel week = weekRepo.findById(weekId)
                .orElseThrow(() -> new IllegalArgumentException("Week not found"));

        DayModel newday = new DayModel();
        newday.setWeek(week);
        newday.setDayOfWeek(dayOfWeek);
        newday.setTodoCount(0); 

        DayModel cratedDay=dayRepo.save(newday);

        return cratedDay;
       
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

    public DayModel updateDay(UUID dayId, DayModel updatedDay) {
        Optional<DayModel> existingDayOptional = dayRepo.findById(dayId);
        if (existingDayOptional.isEmpty()) {
            throw new IllegalArgumentException("Day not found");
        }

        DayModel existingDay = existingDayOptional.get();
        existingDay.setDayOfWeek(updatedDay.getDayOfWeek());
        existingDay.setTodoCount(updatedDay.getTodoCount());

        return dayRepo.save(existingDay);
    }

}
