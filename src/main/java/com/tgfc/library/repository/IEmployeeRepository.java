package com.tgfc.library.repository;

import com.tgfc.library.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepository  extends JpaRepository<Employee,String> {
}
