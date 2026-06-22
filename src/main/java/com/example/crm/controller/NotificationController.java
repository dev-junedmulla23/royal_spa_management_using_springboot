package com.example.crm.controller;

import com.example.crm.entity.dtos.notification.NotificationRequest;
import com.example.crm.payload.ApiResponse;
import com.example.crm.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification API")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * POST → Trigger Notification
     */
    @PostMapping("/whatsapp/trigger")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Trigger push notification to a customer")
    public ResponseEntity<ApiResponse<?>> triggerNotification(
            @Valid @RequestBody NotificationRequest request) {

        return ResponseEntity.ok(
                notificationService.triggerNotification(request)
        );
    }

    /**
     * GET → Logs
     */
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all notification delivery logs")
    public ResponseEntity<ApiResponse<?>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                notificationService.getAllLogs(pageable)
        );
    }
}