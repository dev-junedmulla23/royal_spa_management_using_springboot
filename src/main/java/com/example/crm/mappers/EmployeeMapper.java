package com.example.crm.mappers;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Employee;
import com.example.crm.entity.dtos.employee.EmployeeRequest;
import com.example.crm.entity.dtos.employee.EmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    // ✅ DTO → ENTITY (CREATE)
    public Employee toEntity(EmployeeRequest request, AppUser appUser) {

        return Employee.builder()
                .appUser(appUser)
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .email(request.getEmail())
                .specialization(request.getSpecialization())
                .experience(request.getExperience())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }

    // ✅ ENTITY → RESPONSE
    public EmployeeResponse toResponse(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())

                // AppUser info
                .userId(employee.getAppUser().getId())
                .username(employee.getAppUser().getUsername())
                .role(employee.getAppUser().getRole().getRoleName())

                // Employee info
                .name(employee.getName())
                .mobileNumber(employee.getMobileNumber())
                .email(employee.getEmail())
                .specialization(employee.getSpecialization())
                .experience(employee.getExperience())

                .isAvailable(employee.getIsAvailable())
                .isActive(employee.getIsActive())
                .build();
    }

    // ✅ UPDATE EXISTING ENTITY
    public void updateEntity(Employee employee,
                             EmployeeRequest request,
                             AppUser appUser) {

        if (appUser != null) {
            employee.setAppUser(appUser);
        }

        if (request.getName() != null) {
            employee.setName(request.getName());
        }

        if (request.getMobileNumber() != null) {
            employee.setMobileNumber(request.getMobileNumber());
        }

        if (request.getEmail() != null) {
            employee.setEmail(request.getEmail());
        }

        if (request.getSpecialization() != null) {
            employee.setSpecialization(request.getSpecialization());
        }

        if (request.getExperience() != null) {
            employee.setExperience(request.getExperience());
        }

        if (request.getIsAvailable() != null) {
            employee.setIsAvailable(request.getIsAvailable());
        }

        if (request.getIsActive() != null) {
            employee.setIsActive(request.getIsActive());
        }
    }
}