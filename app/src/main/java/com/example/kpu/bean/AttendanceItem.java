package com.example.kpu.bean;

import java.util.List;

public class AttendanceItem {
    String year;
    List<AttendanceSubItem> subItems;  // 子列表

    public AttendanceItem(String year, List<AttendanceSubItem> subItems) {
        this.year = year;
        this.subItems = subItems;
    }

    public List<AttendanceSubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<AttendanceSubItem> subItems) {
        this.subItems = subItems;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
