package com.deepsalunkhee.dailycanvasSever.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.deepsalunkhee.dailycanvasSever.controllers.dto.TodoDTO;
import com.deepsalunkhee.dailycanvasSever.models.DayModel;
import com.deepsalunkhee.dailycanvasSever.models.TodoModel;
import com.deepsalunkhee.dailycanvasSever.services.DayServices;
import com.deepsalunkhee.dailycanvasSever.services.TodoServices;

@RestController
@RequestMapping("/api/v1")
public class TodoControllers {

     private final TodoServices todoServices;
    private final DayServices dayServices;
    private static final Logger logger = LoggerFactory.getLogger(TodoControllers.class);

    public TodoControllers(TodoServices todoServices, DayServices dayServices) {
        this.todoServices = todoServices;
        this.dayServices = dayServices;
    }

    @PostMapping("/create-todo")
    public TodoDTO createTodo(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        UUID dayId = UUID.fromString(request.get("dayId"));
        String textColor = request.get("textColor");
        String fontSize = request.get("fontSize");
        int position = Integer.parseInt(request.get("position"));

        logger.info("Creating Todo with content: {}", content);

       

        DayModel day = dayServices.getDayById(dayId);

         //check is current todo count is less than 20

         if(day.getTodoCount() == 20) {
            logger.error("Todo count exceeded for day: {}", dayId);
            return new TodoDTO();
        }

        //update the todo count of the day
        day.setTodoCount((day.getTodoCount() + 1));
        dayServices.updateDay(dayId, day);

        TodoModel todo = new TodoModel();
        todo.setDay(day);
        todo.setContent(content);
        todo.setTextColor(textColor);
        todo.setFontSize(fontSize);
        todo.setPosition(position);

        
        todo.setActionApplied(false);
        todo.setActionType(null);
        todo.setScratchColor(null);

        TodoModel created = todoServices.createTodo(dayId,todo);

        TodoDTO dto = new TodoDTO(

                created.getId(),
                created.getDay().getId(),
                created.getContent(),
                created.isActionApplied(),
                created.getActionType(),
                created.getTextColor(),
                created.getFontSize(),
                created.getScratchColor(),
                created.getPosition()
        );

        logger.info("Todo created successfully: {}", dto);

        return dto;
    }

    @PostMapping("/update-todo")
    public TodoDTO updateTodo(@RequestBody Map<String, String> request) {
        UUID id = UUID.fromString(request.get("id"));
        String content = request.get("content");
        String textColor = request.get("textColor");
        String fontSize = request.get("fontSize");
        int position = Integer.parseInt(request.get("position"));
        boolean actionApplied = Boolean.parseBoolean(request.get("actionApplied"));
        String actionType = request.get("actionType");
        String scratchColor = request.get("scratchColor");
       


        logger.info("Updating Todo with ID: {}", id);

        TodoModel todo = todoServices.getTodoById(id);
        if (todo == null) {
            logger.error("Todo not found with ID: {}", id);
            return null;
        }

        todo.setContent(content);
        todo.setTextColor(textColor);
        todo.setFontSize(fontSize);
        todo.setPosition(position);
        todo.setActionApplied(actionApplied);
        todo.setActionType(actionType);
        todo.setScratchColor(scratchColor);

        TodoModel updatedTodo = todoServices.updateTodo(id, todo);

        TodoDTO dto = new TodoDTO(
                updatedTodo.getId(),
                updatedTodo.getDay().getId(),
                updatedTodo.getContent(),
                updatedTodo.isActionApplied(),
                updatedTodo.getActionType(),
                updatedTodo.getTextColor(),
                updatedTodo.getFontSize(),
                updatedTodo.getScratchColor(),
                updatedTodo.getPosition()
        );

        logger.info("Todo updated successfully: {}", dto);

        return dto;
    }

    @PostMapping("/delete-todo")
    public void deleteTodo(@RequestBody Map<String, String> request) {
        UUID id = UUID.fromString(request.get("id"));
        logger.info("Deleting Todo with ID: {}", id);
    
        TodoModel todo = todoServices.getTodoById(id);
        if (todo == null) {
            logger.error("Todo not found with ID: {}", id);
            return;
        }
    
        // Update the todo count of the day
        DayModel day = todo.getDay();
        day.setTodoCount(day.getTodoCount() - 1);
        dayServices.updateDay(day.getId(), day);
    
        // Adjust positions of remaining todos
        List<TodoModel> todosOfDay = todoServices.getTodosByDayId(day.getId());
    
        // Sort todos by position
        todosOfDay.sort(Comparator.comparingInt(TodoModel::getPosition));
    
        int deletedTodoPosition = todo.getPosition();
    
        for (TodoModel t : todosOfDay) {
            if (t.getPosition() > deletedTodoPosition) {
                t.setPosition(t.getPosition() - 1);
                todoServices.updateTodo(t.getId(), t); 
            }
        }
    
        // Finally delete the todo
        todoServices.deleteTodo(id);
        logger.info("Todo deleted successfully with ID: {}", id);
    }
    
}
