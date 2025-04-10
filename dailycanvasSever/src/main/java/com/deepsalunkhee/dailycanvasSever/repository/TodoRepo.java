package com.deepsalunkhee.dailycanvasSever.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepsalunkhee.dailycanvasSever.models.TodoModel;

public interface TodoRepo extends JpaRepository<TodoModel,UUID> {
    List<TodoModel> findByDayId(UUID dayId);
}
