package com.tgfc.library.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Date borrowDate;
    @Column
    private Date returnDate;
    @Column
    private Integer Status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

//    @ManyToOne
//    @JoinColumn(name = "bookId")
//    private Book book;




}
