package com.example.crm.repository;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Employee;
import com.example.crm.entity.dtos.employee.EmployeeResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee,UUID> {

    boolean existsByMobileNumber(String mobileNumber);

    Optional<Employee> findByMobileNumber(String mobile);

    List<Employee> findByIsActiveFalse();

    List<Employee> findByIsActiveTrue();

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByNameContainingIgnoreCaseAndMobileNumber(String name, String mobile);
}
