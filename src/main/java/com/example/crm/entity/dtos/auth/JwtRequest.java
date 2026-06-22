package com.example.crm.entity.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @NotBlank(message = "Username cannot be blank.")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
