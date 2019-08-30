package com.tgfc.library.request;

import java.sql.Time;
import java.util.Date;

public class SchedulePageRequset extends PageableRequest {
    private String id;
    private String name;
    private Time noticeTime;
    private Date startTime;
    private Date endTime;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Time noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}