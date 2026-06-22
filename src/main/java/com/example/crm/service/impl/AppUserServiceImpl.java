package com.example.crm.service.impl;



import com.example.crm.entity.AppUser;
import com.example.crm.entity.Role;

import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.appUserDtos.AppUserResponse;
import com.example.crm.entity.dtos.pagination.PageRequest;
import com.example.crm.exceptions.handlers.DuplicateRecordException;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.AppUserMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.helper.QueryResultHandler;
import com.example.crm.repository.AppUserRepository;
import com.example.crm.repository.RoleRepository;
import com.example.crm.service.AppUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final AppUserMapper appUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final QueryResultHandler queryResultHandler;

    @Override
    @Transactional
    public ApiResponse<?> createUser(AppUserRequest request, MultipartFile file){
        if(appUserRepository.existsByUsername(request.getUsername())){
            throw new DuplicateRecordException("Username already exist.");
        }

        Role role=getRoleById(request.getRoleId());

        AppUser appUser=appUserMapper.mapToAppUserEntity(request);
        appUser.setRole(role);

        //image upload logics goes here

        AppUser savedUser=appUserRepository.save(appUser);
        return ApiResponseUtil.created(appUserMapper.mapToAppUserResponse(savedUser));
    }

    @Override
    public ApiResponse<?> getAllUsers(PageRequest pageRequest){
        return queryResultHandler.fetchAndRespond(
                pageRequest,
                pageable -> appUserRepository.findAll(pageable),
                sort -> appUserRepository.findAll(sort),
                () -> appUserRepository.findAll(),
                appUserMapper::mapToAppUserResponse,
                "Users not found"
        );
    }

    @Override
    public ApiResponse<?> getUserById(UUID id){
        AppUser appUser=getById(id);
        return ApiResponseUtil.fetched(appUserMapper.mapToAppUserResponse(appUser));
    }

    @Override
    public ApiResponse<?> getUserByUsername(String username){
        List<AppUser> appUsers=appUserRepository.findByUsernameContainingIgnoreCase(username);

        if(appUsers.isEmpty()){
            throw new ResourceNotFoundException("Users not found with name : "+username);
        }

        List<AppUserResponse> responseList=appUsers.stream()
                .map(user->appUserMapper.mapToAppUserResponse(user))
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(responseList);
    }

    @Override
    @Transactional
    public ApiResponse<?> updateUser(UUID id, AppUserRequest request,MultipartFile userImage) {
        if(appUserRepository.existsByUsernameAndIdNot(request.getUsername(),id)){
            throw new DuplicateRecordException("Username already exist for another user.");
        }

        AppUser user = getById(id);

        // update basic fields
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setUsername(request.getUsername());
        user.setContact(request.getContact());
        user.setEmail(request.getEmail());
        user.setDateOfJoining(request.getDateOfJoining());
        user.setSalary(request.getSalary());

        // update password only if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // update role if changed
        if (!user.getRole().getId().equals(request.getRoleId())) {
            Role role = getRoleById(request.getRoleId());
            user.setRole(role);
        }

        //image update logics goes here.

        AppUser savedUser = appUserRepository.save(user);

        return ApiResponseUtil.updated(
                appUserMapper.mapToAppUserResponse(savedUser)
        );
    }



    @Override
    @Transactional
    public ApiResponse<?> deleteUser(UUID id){
        AppUser appUser=getById(id);

        appUserRepository.delete(appUser);
        return ApiResponseUtil.deleted();
    }


    private AppUser getById(UUID id){
        return appUserRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with ID : "+id));
    }

    private Role getRoleById(UUID id){
        return roleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Role not found with ID : "+id));
    }
}
