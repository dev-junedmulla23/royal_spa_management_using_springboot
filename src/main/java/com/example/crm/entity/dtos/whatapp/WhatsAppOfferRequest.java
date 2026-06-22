package com.example.crm.entity.dtos.whatapp;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppOfferRequest {

    // List of customer IDs to send offer to
    private List<UUID> customerIds;

    // Offer message content
    @NotBlank
    private String offerMessage;
}