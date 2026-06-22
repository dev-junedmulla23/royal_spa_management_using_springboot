package com.example.crm.service.impl;

import com.example.crm.config.jwt.JwtUtils;
import com.example.crm.config.jwt.MyUserDetailsService;
import com.example.crm.entity.AppUser;
import com.example.crm.entity.Permission;
import com.example.crm.entity.Privilege;
import com.example.crm.entity.Role;
import com.example.crm.entity.dtos.auth.*;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.JwtMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.AppUserRepository;
import com.example.crm.repository.PermissionRepository;
import com.example.crm.repository.RoleRepository;
import com.example.crm.service.JwtAuthenticationService;
import com.example.crm.service.MailService;
import com.example.crm.util.StaticStringsUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final MailService mailService;
    private final JwtMapper jwtMapper;
    private final JwtUtils jwtUtils;
    private final GoogleAuthenticator googleAuthenticator;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final StaticStringsUtil staticStringsUtil;

    @Override
    @Transactional
    public ApiResponse<?> loginUser(JwtRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails= userDetailsService.loadUserByUsername(request.getUsername());

        AppUser appUser=getByUsername(userDetails.getUsername());

        //===MFA checking===
        if(Boolean.TRUE.equals(appUser.getMultiFactor())){
            String tempToken=jwtUtils.generateTempToken(appUser.getUsername());
            return new ApiResponse<>("success","OTP is required!", Map.of("tempToken",tempToken));
        }

        //===JWT Generation===
        String token=jwtUtils.generateToken(appUser.getUsername());

        appUser.setIsUserLoggedIn(true);
        AppUser savedUser=appUserRepository.save(appUser);
        return ApiResponseUtil.success(jwtMapper.mapToJwtResponse(savedUser,token));
    }

    @Override
    @Transactional
    public ApiResponse<?> logoutUser(String username){
        AppUser appUser=getByUsername(username);

        appUser.setIsUserLoggedIn(false);
        appUserRepository.save(appUser);
        return ApiResponseUtil.success("User logout successfully.");
    }

    @Override
    @Transactional
    public ApiResponse<?> sendForgetPasswordLink(ForgotPasswordRequest request){
        AppUser appUser=getByUsername(request.getUsername());

        if(!appUserRepository.existsByEmail(request.getEmail())){
            throw new ResourceNotFoundException("Email not found : "+request.getEmail());
        }

        String token=UUID.randomUUID().toString();

        appUser.setResetToken(token);
        appUser.setTokenExpiryTime(LocalDateTime.now().plusMinutes(15));
        appUserRepository.save(appUser);

        String resetLink= staticStringsUtil.getForgetPasswordLink(token);
        mailService.sendMail(request.getEmail(), "Password Reset Link",resetLink);
        return ApiResponseUtil.success("Reset password link has been sent to your registered email.");
    }

    @Override
    public ApiResponse<?> resetPassword(ResetPasswordRequest request){
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match.");
        }

        AppUser appUser=appUserRepository.findByResetToken(request.getToken())
                .orElseThrow(()->new ResourceNotFoundException("Reset token not found : "+request.getToken()));

        if(appUser.getTokenExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token is expired.");
        }

        appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        appUser.setResetToken(null);
        appUser.setTokenExpiryTime(null);
        appUserRepository.save(appUser);

        return ApiResponseUtil.success("Password reset successfully.");
    }

    public ApiResponse<?> loginWithOtp(LoginOTP loginOTP){
        String username=jwtUtils.extractUsername(loginOTP.getTempToken());

        AppUser appUser=getByUsername(username);

        if(!Boolean.TRUE.equals(appUser.getMultiFactor())){
            throw new RuntimeException("2 Factor Authentication is not enabled.");
        }

        if(!googleAuthenticator.authorize(appUser.getSecretKey(), loginOTP.getOtp())){
            throw new BadCredentialsException("Invalid OTP.");
        }

        String jwtToken=jwtUtils.generateToken(appUser.getUsername());
        appUser.setIsUserLoggedIn(true);

        return ApiResponseUtil.success(
                jwtMapper.mapToJwtResponse(appUserRepository.save(appUser),jwtToken)
        );
    }

    @Override
    @Transactional
    public ApiResponse<?> enableTwoFactorAuthentication(String username) throws WriterException, IOException {
        AppUser appUser=getByUsername(username);

        if(Boolean.TRUE.equals(appUser.getMultiFactor())){
            throw new RuntimeException("2FA already enabled.");
        }

        //  Generate secret only once
        if(appUser.getSecretKey()==null){
            appUser.setSecretKey(generateKey());
        }

        //  DO NOT enable MFA yet
        // MFA will be enabled only after OTP verification
        appUser.setMultiFactor(false);

        //generate QR code
        String qrCodeUrl =StaticStringsUtil.getQrCodeUrl(appUser.getUsername(), appUser.getSecretKey());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        String base64QRCode =
                Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());

        appUser.setQrCodeImage(base64QRCode);
        appUserRepository.save(appUser);

        return ApiResponseUtil.success(Map.of("qrCode",base64QRCode));
    }

    @Override
    public ApiResponse<?> verifyOtp(String authHeader, VerifyOTP verifyOTP){
        String jwtToken = authHeader.substring(7);
        String username = jwtUtils.extractUsername(jwtToken);

        AppUser appUser=getByUsername(username);
        boolean valid=googleAuthenticator.authorize(appUser.getSecretKey(),verifyOTP.getOtp());
        if(!valid){
            throw new BadCredentialsException("Invalid OTP");
        }

        appUser.setMultiFactor(true);
        AppUser savedAppUser= appUserRepository.save(appUser);
        return ApiResponseUtil.success(Map.of("multiFactor",savedAppUser.getMultiFactor()));
    }

    public ApiResponse<?> disableTwoFactorAuthentication(String username, VerifyOTP verifyOTP){
        AppUser appUser=getByUsername(username);

        if(Boolean.FALSE.equals(appUser.getMultiFactor())){
            throw new RuntimeException("2FA already disabled.");
        }

        if(!googleAuthenticator.authorize(appUser.getSecretKey(),verifyOTP.getOtp())){
            throw new BadCredentialsException("Invalid OTP");
        }

        appUser.setMultiFactor(false);
        appUser.setSecretKey(null);
        appUser.setQrCodeImage(null);
        AppUser savedAppUser= appUserRepository.save(appUser);
        return ApiResponseUtil.success(Map.of("multiFactor",savedAppUser.getMultiFactor()));
    }


    //=== private methods for internal use ===
    private AppUser getByUsername(String username){
        return appUserRepository.getMyUserByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found with username : "+username));
    }

    private String generateKey() {
        return googleAuthenticator.createCredentials().getKey();
    }


    //===CREATE ADMIN (DEFAULT USER)===
    @PostConstruct
    @Transactional
    public void createAdminIfNotExists() {

        if (appUserRepository.existsByUsername("superadmin.com")) {
            return;
        }

        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(this::createAdminRole);

        AppUser admin = new AppUser();
        admin.setName("superadmin");
        admin.setUsername("superadmin.com");
        admin.setPassword(passwordEncoder.encode("superadmin@123"));
        admin.setAddress("-");
        admin.setContact("-");
        admin.setEmail("aspawar1727@gmail.com");
        admin.setDateOfJoining(null);
        admin.setSalary(0.0);
        admin.setRole(adminRole);

        appUserRepository.save(admin);
    }
    @Transactional
    public Role createAdminRole() {

        // Create Role
        Role role = new Role();
        role.setRoleName("ADMIN");
        role.setRoleDescription("This is default role for ADMIN");
        Role savedRole = roleRepository.save(role);

        // Create Privilege (DO NOT SAVE IT)
        Privilege privilege = new Privilege();
        privilege.setReadPermission("READ");
        privilege.setWritePermission("WRITE");
        privilege.setUpdatePermission("UPDATE");
        privilege.setDeletePermission("DELETE");

        // Create Permission and link everything
        Permission permission = new Permission();
        permission.setUserPermission("ALL_PERMISSIONS");
        permission.setRole(savedRole);
        permission.setPrivilege(privilege);

        // Set reverse mapping (IMPORTANT)
        privilege.setPermission(permission);

        // Save ONLY Permission
        Permission savedPermission = permissionRepository.save(permission);

        // Attach permission to role
        savedRole.setPermissions(List.of(savedPermission));

        return savedRole;
    }

}
