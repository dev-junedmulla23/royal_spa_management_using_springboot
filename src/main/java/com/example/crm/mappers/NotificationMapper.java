package com.example.crm.mappers;


import com.example.crm.entity.Notification;
import com.example.crm.entity.NotificationLog;
import com.example.crm.entity.dtos.notification.NotificationLogResponse;
import com.example.crm.entity.dtos.notification.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    // 🔹 Notification → Response
    public NotificationResponse toResponse(Notification notification) {

        if (notification == null) return null;

        return NotificationResponse.builder()
                .id(notification.getId())
                .customerName(notification.getCustomer().getName())
                .title(notification.getTitle())
                .body(notification.getBody())
                .type(notification.getType())
                .status(notification.getStatus())
                .sentAt(notification.getSentAt())
                .build();
    }

    // 🔹 NotificationLog → Response
    public NotificationLogResponse toLogResponse(NotificationLog log) {

        if (log == null) return null;

        return NotificationLogResponse.builder()
                .id(log.getId())
                .notificationId(log.getNotification().getId())
                .deliveryStatus(log.getDeliveryStatus())
                .responseMessage(log.getResponseMessage())
                .attemptedAt(log.getAttemptedAt())
                .build();
    }
}