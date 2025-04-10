package com.deepsalunkhee.dailycanvasSever.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepsalunkhee.dailycanvasSever.models.DayModel;

public interface DayRepo extends JpaRepository<DayModel,UUID> {
    List<DayModel> findByWeekId(UUID weekId);

}
