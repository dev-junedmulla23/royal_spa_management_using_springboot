package com.example.crm.entity.dtos.roleDtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    @NotBlank(message = "Role name cannot be blank")
    private String roleName;
    private String roleDescription;

    @NotEmpty(message = "Role permissions cannot be blank")
    @Valid
    private List<PermissionRequest> permissions;
}
