package com.tgfc.library.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "records")
public class Records implements Serializable {

    private static final long serialVersionUID = -2702636334338651966L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "borrow_id", length = 10, nullable = false)
    private String borrowId;
    @Column(name = "borrow_username", length = 30, nullable = false)
    private String borrowUsername;
    @Column(name = "borrow_date", nullable = false)
    @CreatedDate
    private Date borrowDate;
    @Column(name = "return_date")
    @CreatedDate
    private Date returnDate;
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @Column(name = "status", length = 5, nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeSafty employee;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowUsername() {
        return borrowUsername;
    }

    public void setBorrowUsername(String borrowUsername) {
        this.borrowUsername = borrowUsername;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EmployeeSafty getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSafty employee) {
        this.employee = employee;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
