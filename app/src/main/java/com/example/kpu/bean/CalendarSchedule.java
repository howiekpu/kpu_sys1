package com.example.kpu.bean;


import java.util.ArrayList;
import java.util.List;

public class CalendarSchedule {
    private String date;
    private List<Schedule> scheduleList = new ArrayList<>();

    public CalendarSchedule(String date, List<Schedule> scheduleList) {
        this.date = date;
        this.scheduleList = scheduleList;
    }

    public CalendarSchedule(){

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
