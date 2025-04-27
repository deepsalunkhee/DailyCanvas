package com.deepsalunkhee.dailycanvasSever.controllers.dto;


public class WeeKListItemDTO {

    
        private String weekId;
        private String startDate;
        private String name;
        private String note;
        private String templet;

        public WeeKListItemDTO(String weekId, String startDate, String name, String note, String templet) {
            this.weekId = weekId;
            this.startDate = startDate;
            this.name = name;
            this.note = note;
            this.templet = templet;
        }
        public String getWeekId() {
            return weekId;
        }
        public String getStartDate() {
            return startDate;
        }
        public String getName() {
            return name;
        }

        public String getNote() {
            return note;
        }

        public String getTemplet() {
            return templet;
        }

        public void setWeekId(String weekId) {
            this.weekId = weekId;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setTemplet(String templet) {
            this.templet = templet;
        }


 
    



}
