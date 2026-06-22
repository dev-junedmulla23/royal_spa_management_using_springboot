package com.example.crm.entity.dtos.notification;

import com.example.crm.util.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message body is required")
    private String body;

    // Optional: defaults to CUSTOM if not provided
    private NotificationType type = NotificationType.CUSTOM;
}