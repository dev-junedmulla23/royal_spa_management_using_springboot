package com.example.crm.entity.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOTP {
    @NotBlank(message = "Temp token is required")
    private String tempToken;

    @NotNull(message = "OTP is required")
    private Integer otp;
}
