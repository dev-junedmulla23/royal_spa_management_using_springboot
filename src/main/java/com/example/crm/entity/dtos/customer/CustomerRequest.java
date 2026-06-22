package com.example.crm.entity.dtos.customer;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "Invalid blood group (e.g., A+, O-, AB+)"
    )
    private String bloodGroup;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number (must be 10 digits and start with 6-9)"
    )
    private String mobileNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    private Boolean isActive = true;


    private String fcmToken;
}
