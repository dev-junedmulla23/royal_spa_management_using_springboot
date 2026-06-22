package com.example.crm.entity.dtos.appUserDtos;

import com.example.crm.entity.validations.CreateValidation;
import com.example.crm.entity.validations.UpdateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {
    @NotBlank(
            message = "User's name cannot be blank",
            groups = {CreateValidation.class, UpdateValidation.class}
    )
    private String name;
    private String address;

    @NotBlank(
            message = "Username name cannot be blank",
            groups = {CreateValidation.class, UpdateValidation.class}
    )
    private String username;

    @NotBlank(
            message = "Password cannot be blank",
            groups = {CreateValidation.class}
    )
    private String password;

    @NotBlank(
            message = "Contact cannot be blank",
            groups = {CreateValidation.class, UpdateValidation.class}
    )
    private String contact;

    @NotBlank(
            message = "Email cannot be blank",
            groups = {CreateValidation.class, UpdateValidation.class}
    )
    private String email;

    private LocalDate dateOfJoining;
    private double salary;

    @NotNull(
            message = "User's name cannot be blank",
            groups = {CreateValidation.class, UpdateValidation.class}
    )
    private UUID roleId;
}
