package com.tgfc.library.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class schedule implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_execute", length = 2)
    private String lastExecute;

    @Column(name = "status", length = 2, nullable = false)
    private String status;

    @Column(name = "type", length = 2, nullable = false)
    private String type;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "notice_time", nullable = false)
    private Time noticeTime;

    @Column(name = "job_name", nullable = false)
    private Time jobName;

    @Column(name = "job_group", nullable = false)
    private Time jobGroup;

    @Column(name = "job_class_name", nullable = false)
    private Time jobClassName;

    @Column(name = "trigger_name", nullable = false)
    private Time triggerName;

    @Column(name = "trigger_group", nullable = false)
    private Time triggerGroup;

    @Column(name = "cron", nullable = false)
    private Time cron;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private Employee employee;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Time getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Time noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Time getJobName() {
        return jobName;
    }

    public void setJobName(Time jobName) {
        this.jobName = jobName;
    }

    public Time getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Time jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Time getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(Time jobClassName) {
        this.jobClassName = jobClassName;
    }

    public Time getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(Time triggerName) {
        this.triggerName = triggerName;
    }

    public Time getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(Time triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public Time getCron() {
        return cron;
    }

    public void setCron(Time cron) {
        this.cron = cron;
    }
}
