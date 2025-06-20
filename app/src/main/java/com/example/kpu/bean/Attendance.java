package com.example.kpu.bean;
public class Attendance {

    private int attendanceId;  // 考勤记录ID
    private long userId;        // 用户ID
    private String date;       // 出勤日期（格式：YYYY-MM-DD）
    private int status;        // 出勤状态

    // 无参构造函数
    public Attendance() {
    }

    // 带参构造函数
    public Attendance(int attendanceId, long userId, String date, int status) {
        this.attendanceId = attendanceId;
        this.userId = userId;
        this.date = date;
        this.status = status;
    }

    // Getter 和 Setter 方法

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // 重写 toString 方法，方便打印对象
    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", status=" + status +
                '}';
    }
}
