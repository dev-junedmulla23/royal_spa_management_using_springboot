package com.example.crm.controller.auth;

import com.example.crm.entity.dtos.auth.ForgotPasswordRequest;
import com.example.crm.entity.dtos.auth.JwtRequest;
import com.example.crm.entity.dtos.auth.LoginOTP;
import com.example.crm.entity.dtos.auth.VerifyOTP;
import com.example.crm.service.JwtAuthenticationService;
import com.google.zxing.WriterException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final JwtAuthenticationService jwtAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody JwtRequest request){
        return ResponseEntity.ok(jwtAuthenticationService.loginUser(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam String username){
        return ResponseEntity.ok(jwtAuthenticationService.logoutUser(username));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgotPasswordRequest request){
        return ResponseEntity.ok(jwtAuthenticationService.sendForgetPasswordLink(request));
    }

    // ================= 2FA APIs =================
    @PostMapping("/enable-two-factor-authentication")
    public ResponseEntity<?> enableTwoFactorAuthentication(@RequestParam String username) throws WriterException, IOException {
        return ResponseEntity.ok(jwtAuthenticationService.enableTwoFactorAuthentication(username));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody VerifyOTP verifyOTP){
        System.out.println("Received data : "+verifyOTP.toString());
        return ResponseEntity.ok(jwtAuthenticationService.verifyOtp(authHeader,verifyOTP));
    }

    //if 2FA is enabled
    @PostMapping("/login-with-otp")
    public ResponseEntity<?> loginWithOtp(@Valid @RequestBody LoginOTP loginOTP){
        System.out.println("Received data : "+loginOTP.toString());
        return ResponseEntity.ok(jwtAuthenticationService.loginWithOtp(loginOTP));
    }

    @PostMapping("/disable-two-factor-authentication")
    public ResponseEntity<?> disableTwoFactorAuthentication(@RequestParam String username,
                                                            @Valid @RequestBody VerifyOTP verifyOTP){
        return ResponseEntity.ok(jwtAuthenticationService.disableTwoFactorAuthentication(username,verifyOTP));
    }
}
