package com.example.kpu.bean;
public class Schedule {
    private long scheduleId;
    private String title;
    private String content;
    private String createTime;
    private long userId;

    public Schedule(){

    }
    // 构造方法、getter和setter方法
    public Schedule(long scheduleId, String title, String content, String createTime, long userId) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.userId = userId;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
