package com.deepsalunkhee.dailycanvasSever.controllers.dto;

import java.util.UUID;

public class TodoDTO {
    private UUID id;
    private UUID dayId;
    private String content;
    private boolean actionApplied;
    private String actionType;      // "SCRATCHED" or "DONE"
    private String textColor;       // "black" or "blue"
    private String fontSize;        // "S", "M", "L"
    private String scratchColor;    // "blue" or "black"
    private int position;

    // Constructor
    public TodoDTO(UUID id, UUID dayId, String content, boolean actionApplied, String actionType, String textColor, String fontSize, String scratchColor, int position) {
        this.id = id;
        this.dayId = dayId;
        this.content = content;
        this.actionApplied = actionApplied;
        this.actionType = actionType;
        this.textColor = textColor;
        this.fontSize = fontSize;
        this.scratchColor = scratchColor;
        this.position = position;
    }

    public TodoDTO() {
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDayId() {
        return dayId;
    }

    public void setDayId(UUID dayId) {
        this.dayId = dayId;
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
