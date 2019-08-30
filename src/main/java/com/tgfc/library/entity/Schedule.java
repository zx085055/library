package com.tgfc.library.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="generatorName")
    @GenericGenerator(name = "generatorName", strategy = "native")
    private Integer id;

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
    private String jobName;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private Employee employee;


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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
