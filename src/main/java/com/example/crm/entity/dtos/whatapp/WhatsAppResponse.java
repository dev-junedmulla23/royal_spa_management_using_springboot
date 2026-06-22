package com.example.crm.entity.dtos.whatapp;

import com.example.crm.util.enums.WhatsAppStatus;
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
public class WhatsAppResponse {
    private UUID logId;
    private String phoneNumber;
    private String message;
    private WhatsAppStatus status;
    private String twilioMessageSid;   // returned by Twilio on success
    private LocalDateTime sentAt;
    private String errorMessage;        // populated only on failure
}