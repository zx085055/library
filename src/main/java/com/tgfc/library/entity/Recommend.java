package com.tgfc.library.entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "recommend")
public class Recommend implements Serializable {

    private static final long serialVersionUID = -3049053392909116198L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 13, nullable = false)
    @Pattern(regexp = "^[0-9X]{10}|^[0-9X]{13}")
    private String isbn;
    @Column(length = 30, nullable = false)
    private String pubHouse;
    @Column(length = 30, nullable = false)
    private String auther;
    @Column
    private String reason;
    @Column(nullable = false)
    private Date publishDate;
    @Column(nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "id")
    private EmployeeSafety employee;

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

    public EmployeeSafety getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSafety employee) {
        this.employee = employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
