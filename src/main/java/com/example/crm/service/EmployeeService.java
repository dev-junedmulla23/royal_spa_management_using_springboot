package com.example.crm.service;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.dtos.employee.EmployeeRequest;
import com.example.crm.payload.ApiResponse;

import java.util.UUID;

public interface EmployeeService {
    ApiResponse<?> createEmployee(EmployeeRequest request, AppUser appUser);

    ApiResponse<?> getAllEmployees();

    ApiResponse<?> updateEmployeeById(UUID id, EmployeeRequest request,AppUser appUser);

    ApiResponse<?> getEmployeeById(UUID id);

    ApiResponse<?> deleteEmployeeById(UUID id);

    ApiResponse<?> getInactiveEmployee();

    ApiResponse<?> getAvailableEmployee();

    ApiResponse<?> searchEmployee(String name, String mobile);
}
