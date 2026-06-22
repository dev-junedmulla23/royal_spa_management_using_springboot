package com.example.crm.controller;

import com.example.crm.entity.dtos.pagination.PageRequest;
import com.example.crm.entity.dtos.roleDtos.RoleRequest;
import com.example.crm.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create-role")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequest request){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @Operation(description = "This is page request demo")
    @ApiResponse(responseCode = "200",description = "Ok if okey")
    @GetMapping("/get-all-roles")
    public ResponseEntity<?> getAllRoles(PageRequest pageRequest){
        return ResponseEntity.ok(roleService.getAllRoles(pageRequest));
    }

    @GetMapping("/get-role-by-id")
    public ResponseEntity<?> getRoleById(@RequestParam UUID id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping("/get-roles-by-name")
    public ResponseEntity<?> getRolesByName(@RequestParam String name,PageRequest pageRequest){
        return ResponseEntity.ok(roleService.getRolesByName(name,pageRequest));
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestParam UUID id,@Valid @RequestBody RoleRequest request){
        return ResponseEntity.ok(roleService.updateRole(id,request));
    }

    @DeleteMapping("/delete-role")
    public ResponseEntity<?> deleteRole(@RequestParam UUID id){
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
