package com.example.kpu.bean;

public class AttendanceSubItem {
    String date;
    int presentNum;
    int absentNum;
    int leaveNum;

    public AttendanceSubItem(String date, int presentNum, int absentNum, int leaveNum) {
        this.date = date;
        this.presentNum = presentNum;
        this.absentNum = absentNum;
        this.leaveNum = leaveNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPresentNum() {
        return presentNum;
    }

    public void setPresentNum(int presentNum) {
        this.presentNum = presentNum;
    }

    public int getAbsentNum() {
        return absentNum;
    }

    public void setAbsentNum(int absentNum) {
        this.absentNum = absentNum;
    }

    public int getLeaveNum() {
        return leaveNum;
    }

    public void setLeaveNum(int leaveNum) {
        this.leaveNum = leaveNum;
    }
}
