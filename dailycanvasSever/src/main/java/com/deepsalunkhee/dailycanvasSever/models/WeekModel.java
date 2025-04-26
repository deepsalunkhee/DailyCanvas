package com.deepsalunkhee.dailycanvasSever.models;


import java.time.LocalDate;
import java.util.UUID;



import jakarta.persistence.*;



@Entity
@Table(name = "weeks")
public class WeekModel {

   

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "week_name")
    private String weekName;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "template_type")
    private String templateType; // default_1, default_2, custom

    @Column(name = "brain_sorter_data", columnDefinition = "TEXT")
    private String brainSorterData;

    public WeekModel() {
        this.brainSorterData = """
        {
          "attrs": {
            "width": "100%",
            "height": "100%"
          },
          "className": "Stage",
          "children": [
            {
              "className": "Layer",
              "children": []
            }
          ]
        }
        """;
    }

    


    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getBrainSorterData() {
        return brainSorterData;
    }

    public void setBrainSorterData(String brainSorterData) {
        this.brainSorterData = brainSorterData;
    }

   

    
}