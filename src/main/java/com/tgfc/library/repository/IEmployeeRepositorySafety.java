package com.tgfc.library.repository;

import com.tgfc.library.entity.EmployeeSafety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepositorySafety extends JpaRepository<EmployeeSafety,String> {
}
