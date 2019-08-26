package com.tgfc.library.entity;

import javax.persistence.*;
import java.util.Date;

public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String isbn;
    @Column
    private String pubHouse;
    @Column
    private String auther;
    @Column
    private String reason;
    @Column
    private Date publishDate;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPubHouse() {
        return pubHouse;
    }

    public void setPubHouse(String pubHouse) {
        this.pubHouse = pubHouse;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
