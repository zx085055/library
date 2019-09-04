package com.tgfc.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tgfc.library.entity.Employee;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {


    private String account;
    private String name;
    private String email;
    private String password;
    private List<String> permissions;
    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static EmployeeResponse valueOf(Employee user) {
        EmployeeResponse response = new EmployeeResponse();
        response.setAccount(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setDepartment(user.getDepartment());
        return response;
    }
}
