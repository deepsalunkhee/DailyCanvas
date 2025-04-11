package com.deepsalunkhee.dailycanvasSever.models;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "days")
public class DayModel {

    final static int MAX_TODO_COUNT = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private WeekModel week;

    @Column(name = "day_of_week")
    private String dayOfWeek; // Monday, Tuesday, etc.

    @Column
    private LocalDate date;

    @Column(name = "todo_count")
    private int todoCount;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WeekModel getWeek() {
        return week;
    }

    public void setWeek(WeekModel week) {
        this.week = week;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
