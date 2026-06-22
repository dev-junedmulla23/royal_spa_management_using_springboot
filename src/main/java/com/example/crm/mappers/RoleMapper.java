package com.example.crm.mappers;

import com.example.crm.entity.Privilege;
import com.example.crm.entity.Role;
import com.example.crm.entity.dtos.roleDtos.*;
import org.springframework.stereotype.Component;
import com.example.crm.entity.Permission;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RoleMapper {
    public Role mapToRoleEntity(RoleRequest roleRequest){
        Role role=new Role();
        role.setRoleName(roleRequest.getRoleName());
        role.setRoleDescription(roleRequest.getRoleDescription());
        role.setPermissions(
                roleRequest.getPermissions().stream()
                        .map(p->mapToPermissionEntity(p,role))
                        .collect(Collectors.toList())
        );
        return role;
    }

    public Permission mapToPermissionEntity(PermissionRequest permissionRequest, Role role){
        Permission permission=new Permission();
        permission.setUserPermission(permissionRequest.getUserPermission());
        permission.setPrivilege(mapToPrivilegeEntity(permissionRequest.getPrivilege()));
        permission.setRole(role);
        return permission;
    }

    public Privilege mapToPrivilegeEntity(PrivilegeRequest privilegeRequest){
        Privilege privilege=new Privilege();
        privilege.setReadPermission(privilegeRequest.getReadPermission());
        privilege.setWritePermission(privilegeRequest.getWritePermission());
        privilege.setUpdatePermission(privilegeRequest.getUpdatePermission());
        privilege.setDeletePermission(privilegeRequest.getDeletePermission());
        return privilege;
    }

    public RoleResponse mapToRoleResponse(Role role){
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .roleDescription(role.getRoleDescription())
                .permissions(
                        role.getPermissions().stream()
                                .map(this::mapToPermissionResponse)
                                .collect(Collectors.toList())
                )
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .createdBy(role.getCreatedBy())
                .updatedBy(role.getUpdatedBy())
                .build();
    }

    public PermissionResponse mapToPermissionResponse(Permission permission){
        Privilege privilege = permission.getPrivilege();
        List<String> privileges = Stream.of(
                        privilege.getWritePermission(),
                        privilege.getReadPermission(),
                        privilege.getUpdatePermission(),
                        privilege.getDeletePermission()
                )
                .filter(Objects::nonNull)
                .toList();

        return PermissionResponse.builder()
                .id(permission.getId())
                .userPermission(permission.getUserPermission())
                .privileges(privileges)
                .build();
    }
}
