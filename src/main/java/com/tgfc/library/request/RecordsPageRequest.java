package com.tgfc.library.request;

import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.EmployeeSafty;

import java.util.Date;

public class RecordsPageRequest extends PageableRequest {
    private Integer id;
    private String borrowId;
    private Date borrowDate;
    private Date returnDate;
    private Integer Status;
    private EmployeeSafty employee;
    private Book book;
    private String keyword;

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

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
