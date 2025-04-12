package com.deepsalunkhee.dailycanvasSever.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deepsalunkhee.dailycanvasSever.models.DayModel;
import com.deepsalunkhee.dailycanvasSever.models.TodoModel;
import com.deepsalunkhee.dailycanvasSever.repository.DayRepo;
import com.deepsalunkhee.dailycanvasSever.repository.TodoRepo;


@Service
public class TodoServices {
    private final TodoRepo todoRepo;
    private final DayRepo dayRepo;

    public TodoServices(TodoRepo todoRepo, DayRepo dayRepo) {
        this.todoRepo = todoRepo;
        this.dayRepo = dayRepo;
    }

    public TodoModel createTodo(UUID dayId, TodoModel todo) {
        Optional<DayModel> dayOptional = dayRepo.findById(dayId);
        if (dayOptional.isEmpty()) {
            throw new IllegalArgumentException("Day not found");
        }

        todo.setDay(dayOptional.get());
        return todoRepo.save(todo);
    }

    public List<TodoModel> getTodosByDayId(UUID dayId) {
        return todoRepo.findByDayId(dayId);
    }

    public TodoModel updateTodo(UUID todoId, TodoModel updatedTodo) {
        TodoModel existing = todoRepo.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found"));

    
        existing.setContent(updatedTodo.getContent());
        existing.setTextColor(updatedTodo.getTextColor());
        existing.setFontSize(updatedTodo.getFontSize());
        existing.setScratchColor(updatedTodo.getScratchColor());
        existing.setActionType(updatedTodo.getActionType());
        existing.setActionApplied(updatedTodo.isActionApplied());
        existing.setPosition(updatedTodo.getPosition());
        

        return todoRepo.save(existing);
    }

    public void deleteTodo(UUID todoId) {
        todoRepo.deleteById(todoId);
    }

    public TodoModel getTodoById(UUID todoId) {
        return todoRepo.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found"));
    }
}
