package com.example.crm.entity.dtos.roleDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeRequest {
    private String readPermission;
    private String deletePermission;
    private String updatePermission;
    private String writePermission;
}
