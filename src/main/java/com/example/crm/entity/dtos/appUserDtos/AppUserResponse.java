package com.example.crm.entity.dtos.appUserDtos;

import com.example.crm.entity.dtos.roleDtos.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserResponse {
    private UUID id;
    private String name;
    private String address;
    private String username;
    private String password;
    private String contact;
    private String email;
    private LocalDate dateOfJoining;
    private double salary;
    private String imageUrl;
    private Boolean multiFactor;
    private RoleResponse role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
