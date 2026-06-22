package com.example.crm.service;

import com.example.crm.entity.WhatsAppMessageLog;
import com.example.crm.entity.dtos.whatapp.WhatsAppOfferRequest;
import com.example.crm.entity.dtos.whatapp.WhatsAppRequest;
import com.example.crm.payload.ApiResponse;
import com.example.crm.util.enums.WhatsAppMessageType;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WhatsAppService {

    // Core method — send a WhatsApp message to a phone number
    ApiResponse<?> sendWhatsAppMessage(String phoneNumber, String message,
                                       UUID customerId, WhatsAppMessageType type);

    // Send via request DTO (for controller use)
    ApiResponse<?> send(WhatsAppRequest request);

    // Send offer to multiple customers
    ApiResponse<?> sendOffer(WhatsAppOfferRequest request);

    // Retry failed messages
    ApiResponse<?> retryFailedMessages();

    ApiResponse<?> getLogs(Pageable pageable);
}