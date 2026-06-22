package com.example.crm.controller.auth;


import com.example.crm.entity.dtos.auth.ResetPasswordRequest;
import com.example.crm.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class    ResetPasswordPageController {
    private final JwtAuthenticationService jwtAuthenticationService;

    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam String token,
            Model model
    ) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model
    ) {
        ResetPasswordRequest request =
                new ResetPasswordRequest(token, newPassword, confirmPassword);

        jwtAuthenticationService.resetPassword(request);

        model.addAttribute("message", "Password reset successfully!");
        return "reset-success";
    }
}

