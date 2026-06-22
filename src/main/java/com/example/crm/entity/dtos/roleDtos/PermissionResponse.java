package com.example.crm.entity.dtos.roleDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionResponse {
    private UUID id;
    private String userPermission;
    private List<String> privileges=new ArrayList<>();
}
