package com.deepsalunkhee.dailycanvasSever.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepsalunkhee.dailycanvasSever.models.WeekModel;

public interface WeekRepo extends JpaRepository<WeekModel,UUID> {
    
    WeekModel findByid(UUID id);

}
