package com.company.services.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.services.web.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
