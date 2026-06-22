package com.example.crm.entity.dtos.roleDtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {
    private UUID id;

    @NotBlank(message = "User permissions cannot be blank.")
    private String userPermission;

    @NotNull(message = "Privilege object is required")
    @Valid
    private PrivilegeRequest privilege;
}
