package com.tgfc.library.repository;

import com.tgfc.library.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepository  extends JpaRepository<Employee,String> {
    @Query("SELECT r from Employee r where r.email=?1")
    Boolean existsByEmail(String email);
}
