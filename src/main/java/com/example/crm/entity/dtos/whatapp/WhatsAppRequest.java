package com.example.crm.entity.dtos.whatapp;

import com.example.crm.util.enums.WhatsAppMessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppRequest {

    // Customer UUID — used to look up their phone number automatically
    private UUID customerId;

    // OR provide phone number directly (one of these is required)
    private String phoneNumber;

    // The message to send
    @NotBlank(message = "Message cannot be blank")
    private String message;

    // Optional: defaults to CUSTOM
    private WhatsAppMessageType messageType = WhatsAppMessageType.CUSTOM;
}

