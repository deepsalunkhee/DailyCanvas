package com.deepsalunkhee.dailycanvasSever.models;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "todos")
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private DayModel day;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "action_applies")
    private boolean actionApplied;

    @Column(name = "action_type")
    private String actionType; // "SCRATCHED" or "DONE"

    @Column(name = "text_color")
    private String textColor; // "black" or "blue"

    @Column(name = "font_size")
    private String fontSize; // "S", "M", "L"

    @Column(name = "scratch_color")
    private String scratchColor; // "blue" or "black"

    @Column
    private int position; // Position of the todo in the list


    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DayModel getDay() {
        return day;
    }

    public void setDay(DayModel day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isActionApplied() {
        return actionApplied;
    }

    public void setActionApplied(boolean actionApplied) {
        this.actionApplied = actionApplied;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getScratchColor() {
        return scratchColor;
    }

    public void setScratchColor(String scratchColor) {
        this.scratchColor = scratchColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
