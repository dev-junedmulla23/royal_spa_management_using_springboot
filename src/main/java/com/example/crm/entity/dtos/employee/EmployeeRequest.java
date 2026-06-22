package com.example.crm.entity.dtos.employee;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;   // link with AppUser

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobileNumber;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    private Boolean isAvailable;

    private Boolean isActive;
}