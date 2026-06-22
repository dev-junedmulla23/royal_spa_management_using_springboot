package com.example.crm.mappers;


import com.example.crm.entity.AppUser;
import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.appUserDtos.AppUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserMapper {
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    public AppUser mapToAppUserEntity(AppUserRequest request){
        AppUser appUser=new AppUser();
        appUser.setName(request.getName());
        appUser.setAddress(request.getAddress());
        appUser.setUsername(request.getUsername());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setEmail(request.getEmail());
        appUser.setContact(request.getContact());
        appUser.setDateOfJoining(request.getDateOfJoining());
        appUser.setSalary(request.getSalary());
        return appUser;
    }

    public AppUserResponse mapToAppUserResponse(AppUser appUser){
        return AppUserResponse.builder()
                .id(appUser.getId())
                .name(appUser.getName())
                .username(appUser.getUsername())
                .password(null)
                .email(appUser.getEmail())
                .contact(appUser.getContact())
                .address(appUser.getAddress())
                .dateOfJoining(appUser.getDateOfJoining())
                .salary(appUser.getSalary())
                .imageUrl(appUser.getImageUrl())
                .multiFactor(appUser.getMultiFactor())
                .createdAt(appUser.getCreatedAt())
                .updatedAt(appUser.getUpdatedAt())
                .createdBy(appUser.getCreatedBy())
                .updatedBy(appUser.getUpdatedBy())
                .role(
                        roleMapper.mapToRoleResponse(appUser.getRole())
                )
                .build();
    }
}
