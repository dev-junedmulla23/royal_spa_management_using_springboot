package com.example.crm.service;

import com.example.crm.entity.Customer;

import com.example.crm.entity.Notification;
import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.entity.dtos.notification.NotificationRequest;
import com.example.crm.payload.ApiResponse;
import com.example.crm.util.enums.NotificationType;

import org.springframework.data.domain.Pageable;

public interface NotificationService {

    ApiResponse<?> triggerNotification(NotificationRequest request);

    ApiResponse<?> getAllLogs(Pageable pageable);

    Notification sendPushNotification(Customer customer, String title, String body, NotificationType type);
}