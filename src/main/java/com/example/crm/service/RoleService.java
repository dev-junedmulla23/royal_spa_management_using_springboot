package com.example.crm.service;


import com.example.crm.entity.dtos.pagination.PageRequest;
import com.example.crm.entity.dtos.roleDtos.RoleRequest;
import com.example.crm.payload.ApiResponse;

import java.util.UUID;

public interface RoleService {
    ApiResponse<?> createRole(RoleRequest request);
    ApiResponse<?> getAllRoles(PageRequest pageRequest);
    ApiResponse<?> getRoleById(UUID id);
    ApiResponse<?> getRolesByName(String name,PageRequest pageRequest);
    ApiResponse<?> updateRole(UUID id, RoleRequest request);
    ApiResponse<?> deleteRole(UUID id);
}
