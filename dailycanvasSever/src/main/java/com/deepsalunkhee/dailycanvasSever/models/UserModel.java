package com.deepsalunkhee.dailycanvasSever.models;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserModel {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

     @Column(nullable = false, unique = true)
     private String email;

     @Column(nullable = false)
     private String name;

     @Column(name = "refresh_token", columnDefinition = "TEXT")
     private String refreshToken;

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeekModel> weeks;

    // soon here I will be adding brain sorters

     // Getters and Setters

     public UUID getId() {
          return id;
     }

     public void setId(UUID id) {
          this.id = id;
     }

     public String getEmail() {
          return email;
     }

     public void setEmail(String email) {
          this.email = email;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getRefreshToken() {
          return refreshToken;
     }

     public void setRefreshToken(String refreshToken) {
          this.refreshToken = refreshToken;
     }

     public List<WeekModel> getWeeks() {
          return weeks;
     }

     public void setWeeks(List<WeekModel> weeks) {
          this.weeks = weeks;
     }

     public void addWeek(WeekModel week) {
          weeks.add(week);
     }



     
}
