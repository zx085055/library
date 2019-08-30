package com.tgfc.library.response;

import java.util.Date;

public class SchedulePageResponse {
    private Integer id;
    private String name;
    private Date startTime;
    private Date endTime;
    private String lastExecute;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLastExecute() {
        return lastExecute;
    }

    public void setLastExecute(String lastExecute) {
        this.lastExecute = lastExecute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
