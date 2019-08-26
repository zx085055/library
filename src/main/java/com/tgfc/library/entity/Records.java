package com.tgfc.library.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Records")
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Date borrowDate;
    @Column
    private Date returnDate;
    @Column
    private Integer Status;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

//    @ManyToOne
//    @JoinColumn
//    private Book book;

}
