package com.example.crm.entity.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOTP {
    @NotNull(message = "OTP is required")
    private Integer otp;
}
