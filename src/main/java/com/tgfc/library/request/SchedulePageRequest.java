package com.tgfc.library.request;

import java.sql.Time;
import java.util.Date;

public class SchedulePageRequest extends PageableRequest {
    private Integer id;
    private String type;
    private String name;
    private Time noticeTime;
    private Date startTime;
    private Date endTime;
    private String scheduleStatus;
    private String lastExecute;
    private Boolean isEdit;

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        super.setPageNumber(pageNumber - 1);
    }

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

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String status) {
        this.scheduleStatus = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    public String getLastExecute() {
        return lastExecute;
    }

    public void setLastExecute(String lastExecute) {
        this.lastExecute = lastExecute;
    }
}
