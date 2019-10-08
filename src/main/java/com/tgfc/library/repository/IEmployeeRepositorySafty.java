package com.tgfc.library.repository;

import com.tgfc.library.entity.EmployeeSafty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepositorySafty extends JpaRepository<EmployeeSafty,String> {
}
