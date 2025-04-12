package com.deepsalunkhee.dailycanvasSever.controllers.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WeekDTO {
     private UUID weekId;
    private LocalDate weekStartDate;
    private List<DayInfo> days;

    public static class DayInfo {
        private String day;
        private UUID dayId;
        private List<TodoDTO> todos;

        public DayInfo(String day, UUID dayId, List<TodoDTO> todos) {
            this.day = day;
            this.dayId = dayId;
            this.todos= todos;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public UUID getDayId() {
            return dayId;
        }

        public void setDayId(UUID dayId) {
            this.dayId = dayId;
        }

        public List<TodoDTO> getTodos() {
            return todos;
        }

        public void setTodos(List<TodoDTO> todos) {
            this.todos = todos;
        }

        public void addTodo(TodoDTO todo) {
            if (todos != null) {
                todos.add(todo);
            }
        }
    }

    public UUID getWeekId() {
        return weekId;
    }

    public void setWeekId(UUID weekId) {
        this.weekId = weekId;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public List<DayInfo> getDays() {
        return days;
    }

    public void setDays(List<DayInfo> days) {
        this.days = days;
    }
}
