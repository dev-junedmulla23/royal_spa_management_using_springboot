package com.example.crm.service.impl;

import com.example.crm.entity.Customer;
import com.example.crm.entity.Notification;
import com.example.crm.entity.NotificationLog;
import com.example.crm.entity.dtos.notification.NotificationLogResponse;
import com.example.crm.entity.dtos.notification.NotificationRequest;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.NotificationMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.NotificationLogRepository;
import com.example.crm.repository.NotificationRepository;
import com.example.crm.service.NotificationService;
import com.example.crm.util.enums.NotificationStatus;
import com.example.crm.util.enums.NotificationType;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final NotificationMapper notificationMapper;

    // ✅ 1. Trigger Notification
    @Override
    public ApiResponse<?> triggerNotification(NotificationRequest request) {

        // Step 1: Fetch customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Step 2: Send & save notification
        Notification notification = sendPushNotification(
                customer,
                request.getTitle(),
                request.getBody(),
                request.getType()
        );

        // Step 3: Return response
        return ApiResponseUtil.created(notificationMapper.toResponse(notification));
    }

    // ✅ 2. Send Push Notification (used internally + scheduler)
    @Override
    public Notification sendPushNotification(Customer customer,
                                             String title,
                                             String body,
                                             NotificationType type) {

        String fcmToken = customer.getFcmToken();
        NotificationStatus status;
        String responseMsg;

        if (fcmToken == null || fcmToken.isBlank()) {
            log.warn("No FCM token for customer: {}", customer.getId());
            status = NotificationStatus.FAILED;
            responseMsg = "No FCM token registered";
        } else {
            try {
                // Build FCM message
                Message message = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .putData("type", type.name())
                        .putData("customerId", customer.getId().toString())
                        .build();

                // Send message
                String fcmResponse = FirebaseMessaging.getInstance().send(message);
                log.info("FCM sent: {}", fcmResponse);

                status = NotificationStatus.SENT;
                responseMsg = fcmResponse;

            } catch (FirebaseMessagingException e) {
                log.error("FCM failed: {}", e.getMessage());
                status = NotificationStatus.FAILED;
                responseMsg = e.getMessage();
            }
        }

        // Save Notification
        Notification notification = Notification.builder()
                .customer(customer)
                .title(title)
                .body(body)
                .type(type)
                .status(status)
                .sentAt(LocalDateTime.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // Save Log
        NotificationLog logEntity = NotificationLog.builder()
                .notification(savedNotification)
                .deliveryStatus(status.name())
                .responseMessage(responseMsg)
                .fcmToken(fcmToken)
                .attemptedAt(LocalDateTime.now())
                .build();

        notificationLogRepository.save(logEntity);

        return savedNotification;
    }

    // ✅ 3. Get All Logs
    @Override
    public ApiResponse<?> getAllLogs(Pageable pageable) {

        Page<NotificationLogResponse> page = notificationLogRepository
                .findAllByOrderByAttemptedAtDesc(pageable)
                .map(notificationMapper::toLogResponse);

        return ApiResponseUtil.fetchedList(page.getContent());
    }


}
