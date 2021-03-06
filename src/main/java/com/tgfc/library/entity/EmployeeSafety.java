package com.tgfc.library.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "employee")
public class EmployeeSafety implements Serializable {


    private static final long serialVersionUID = 6535024363806436052L;
    @Id
    private String id;
    @Column(name = "name", length = 20, nullable = false)
    private String name;
    @Column(name = "email", length = 50, nullable = false)
    private String email;
    @Column(name = "department", length = 100, nullable = false)
    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
