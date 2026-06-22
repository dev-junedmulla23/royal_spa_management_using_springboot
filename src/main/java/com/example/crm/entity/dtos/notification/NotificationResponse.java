package com.example.crm.entity.dtos.notification;

import com.example.crm.util.enums.NotificationStatus;
import com.example.crm.util.enums.NotificationType;
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
public class NotificationResponse {
    private UUID id;
    private String customerName;
    private String title;
    private String body;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime sentAt;
}