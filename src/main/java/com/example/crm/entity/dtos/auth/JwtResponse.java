package com.example.crm.entity.dtos.auth;

import com.example.crm.entity.dtos.roleDtos.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private UUID userId;
    private String username;
    private Boolean isUserLoggedIn;
    private String jwtToken;
    private String imageUrl;
    private Boolean multiFactor;
    private RoleResponse role;
}
