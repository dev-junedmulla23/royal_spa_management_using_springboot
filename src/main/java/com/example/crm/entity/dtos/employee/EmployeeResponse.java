package com.example.crm.entity.dtos.employee;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse {
    private UUID id;

    // AppUser info
    private UUID userId;
    private String username;
    private String role;

    // Employee info
    private String name;
    private String mobileNumber;
    private String email;
    private String specialization;
    private Integer experience;

    private Boolean isAvailable;
    private Boolean isActive;
}
