package com.example.crm.service.impl;

import com.example.crm.entity.Customer;
import com.example.crm.entity.WhatsAppMessageLog;
import com.example.crm.entity.dtos.whatapp.WhatsAppOfferRequest;
import com.example.crm.entity.dtos.whatapp.WhatsAppRequest;
import com.example.crm.entity.dtos.whatapp.WhatsAppResponse;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.WhatsAppMessageLogRepository;
import com.example.crm.service.WhatsAppService;
import com.example.crm.util.enums.WhatsAppMessageType;
import com.example.crm.util.enums.WhatsAppStatus;
import com.twilio.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppServiceImpl implements WhatsAppService {

    private final WhatsAppMessageLogRepository logRepository;
    private final CustomerRepository customerRepository;

    @Value("${twilio.whatsapp.number}")
    private String twilioNumber;

    // ─────────────────────────────────────────────────────────────
    // CORE METHOD (INTERNAL)
    // ─────────────────────────────────────────────────────────────
    private WhatsAppResponse sendWhatsAppMessageInternal(
            String phoneNumber, String message,
            UUID customerId, WhatsAppMessageType type) {

        String formattedTo = formatPhoneNumber(phoneNumber);
        String formattedFrom = "whatsapp:" + twilioNumber;

        String twilioSid = null;
        String errorMsg = null;
        WhatsAppStatus status;

        try {
            com.twilio.rest.api.v2010.account.Message twilioMessage =
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new PhoneNumber(formattedTo),
                            new PhoneNumber(formattedFrom),
                            message
                    ).create();

            twilioSid = twilioMessage.getSid();
            status = WhatsAppStatus.SUCCESS;

            log.info("[WhatsApp] ✅ Sent to {} | SID: {}", phoneNumber, twilioSid);

        } catch (ApiException e) {
            errorMsg = "Twilio Error [" + e.getCode() + "]: " + e.getMessage();
            status = WhatsAppStatus.FAILED;

            log.error("[WhatsApp] ❌ Failed: {}", errorMsg);

        } catch (Exception e) {
            errorMsg = "Unexpected error: " + e.getMessage();
            status = WhatsAppStatus.FAILED;

            log.error("[WhatsApp] ❌ Unexpected error: {}", e.getMessage());
        }

        WhatsAppMessageLog logEntry = WhatsAppMessageLog.builder()
                .customerId(customerId)
                .phoneNumber(phoneNumber)
                .message(message)
                .messageType(type)
                .status(status)
                .twilioMessageSid(twilioSid)
                .errorMessage(errorMsg)
                .sentAt(LocalDateTime.now())
                .retryCount(0)
                .build();

        WhatsAppMessageLog saved = logRepository.save(logEntry);

        return WhatsAppResponse.builder()
                .logId(saved.getId())
                .phoneNumber(phoneNumber)
                .message(message)
                .status(status)
                .twilioMessageSid(twilioSid)
                .sentAt(saved.getSentAt())
                .errorMessage(errorMsg)
                .build();
    }

    // ─────────────────────────────────────────────────────────────
    // PUBLIC METHODS (RETURN ApiResponse)
    // ─────────────────────────────────────────────────────────────

    @Override
    public ApiResponse<?> sendWhatsAppMessage(
            String phoneNumber, String message,
            UUID customerId, WhatsAppMessageType type) {

        WhatsAppResponse response =
                sendWhatsAppMessageInternal(phoneNumber, message, customerId, type);

        if (response.getStatus() == WhatsAppStatus.SUCCESS) {
            return ApiResponseUtil.created(response);
        } else {
            return ApiResponseUtil.genericException(" ","WhatsApp message failed", response);
        }
    }

    @Override
    public ApiResponse<?> send(WhatsAppRequest request) {

        String phone = request.getPhoneNumber();
        UUID cid = request.getCustomerId();

        if (cid != null && (phone == null || phone.isBlank())) {
            Customer customer = customerRepository.findById(cid)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            phone = customer.getMobileNumber();
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        WhatsAppResponse response =
                sendWhatsAppMessageInternal(phone, request.getMessage(), cid, request.getMessageType());

        return ApiResponseUtil.created(response);
    }

    @Override
    public ApiResponse<?> sendOffer(WhatsAppOfferRequest request) {

        List<WhatsAppResponse> responses = request.getCustomerIds().stream()
                .map(customerId -> {
                    try {
                        Customer customer = customerRepository.findById(customerId).orElse(null);
                        if (customer == null) return null;

                        return sendWhatsAppMessageInternal(
                                customer.getMobileNumber(),
                                request.getOfferMessage(),
                                customerId,
                                WhatsAppMessageType.OFFER);

                    } catch (Exception e) {
                        log.error("Offer failed for {}", customerId);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return ApiResponseUtil.created(responses);
    }

    @Override
    public ApiResponse<?> retryFailedMessages() {

        List<WhatsAppMessageLog> failed =
                logRepository.findByStatusAndRetryCountLessThan(WhatsAppStatus.FAILED, 3);

        List<WhatsAppResponse> retried = new ArrayList<>();

        for (WhatsAppMessageLog log : failed) {
            try {
                WhatsAppResponse response = sendWhatsAppMessageInternal(
                        log.getPhoneNumber(),
                        log.getMessage(),
                        log.getCustomerId(),
                        log.getMessageType());

                log.setRetryCount(log.getRetryCount() + 1);
                logRepository.save(log);

                retried.add(response);

            } catch (Exception e) {
                log.setRetryCount(log.getRetryCount() + 1);
                logRepository.save(log);
            }
        }

        return ApiResponseUtil.updated(retried);
    }

    // ─────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────
    private String formatPhoneNumber(String phone) {

        phone = phone.replaceAll("[\\s\\-\\(\\)]", "");

        if (!phone.startsWith("+")) {
            phone = "+91" + phone;
        }

        return "whatsapp:" + phone;
    }


    @Override
    public ApiResponse<Page<WhatsAppMessageLog>> getLogs(Pageable pageable) {

        Page<WhatsAppMessageLog> logs = logRepository.findAll(pageable);

        return ApiResponseUtil.success(logs);
    }
}