package com.example.crm.controller;


import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.pagination.PageRequest;
import com.example.crm.entity.validations.CreateValidation;
import com.example.crm.entity.validations.UpdateValidation;
import com.example.crm.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    // Content-Type of @RequestPart("userData") must be application/json
    @PostMapping(value = "/create-user",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(@Validated(CreateValidation.class) @RequestPart("userData") AppUserRequest request,
                                        @RequestPart(value = "userImage",required = false) MultipartFile userImage){
        return ResponseEntity.ok(appUserService.createUser(request,userImage));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(PageRequest pageRequest){
        return ResponseEntity.ok(appUserService.getAllUsers(pageRequest));
    }

    @GetMapping("/get-user-by-id")
    public ResponseEntity<?> getUserById(@RequestParam UUID id){
        return ResponseEntity.ok(appUserService.getUserById(id));
    }

    @GetMapping("/get-users-by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username){
        return ResponseEntity.ok(appUserService.getUserByUsername(username));
    }

    // Content-Type of @RequestPart("userData") must be application/json
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestParam UUID id,
                                        @Validated(UpdateValidation.class) @RequestPart("userData") AppUserRequest request,
                                        @RequestPart(value = "userImage",required = false) MultipartFile userImage){
        return ResponseEntity.ok(appUserService.updateUser(id, request,userImage));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam UUID id){
        return ResponseEntity.ok(appUserService.deleteUser(id));
    }
}
