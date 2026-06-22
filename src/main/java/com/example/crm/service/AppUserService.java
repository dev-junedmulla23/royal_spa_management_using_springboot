package com.example.crm.service;

import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.pagination.PageRequest;
import com.example.crm.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AppUserService {
    ApiResponse<?> createUser(AppUserRequest request, MultipartFile file);
    ApiResponse<?> getAllUsers(PageRequest pageRequest);
    ApiResponse<?> getUserById(UUID id);
    ApiResponse<?> getUserByUsername(String name);
    ApiResponse<?> updateUser(UUID id, AppUserRequest request, MultipartFile file);
    ApiResponse<?> deleteUser(UUID id);

}
