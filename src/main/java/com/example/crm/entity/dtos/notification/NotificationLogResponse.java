package com.example.crm.entity.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLogResponse {
    private UUID id;
    private UUID notificationId;
    private String deliveryStatus;
    private String responseMessage;
    private LocalDateTime attemptedAt;
}
