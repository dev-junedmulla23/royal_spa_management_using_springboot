package com.example.crm.controller;

import com.example.crm.entity.dtos.whatapp.WhatsAppOfferRequest;
import com.example.crm.entity.dtos.whatapp.WhatsAppRequest;
import com.example.crm.service.WhatsAppService;
import com.example.crm.util.enums.WhatsAppMessageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
@Tag(name = "WhatsApp API")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    /**
     * POST /api/whatsapp/send
     * Send a custom WhatsApp message to a customer
     * Body: { "customerId": "uuid", "message": "Hello!" }
     *  OR   { "phoneNumber": "+91XXXXXXXXXX", "message": "Hello!" }
     */
    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Send custom WhatsApp message to a customer")
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody WhatsAppRequest request) {

        return ResponseEntity.ok(whatsAppService.send(request));
    }

    /**
     * POST /api/whatsapp/send-offer
     * Send promotional offer to multiple customers
     * Body: { "customerIds": ["uuid1","uuid2"], "offerMessage": "Get 20% off!" }
     */
    @PostMapping("/send-offer")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Send promotional offer to multiple customers")
    public ResponseEntity<?> sendOffer(
            @Valid @RequestBody WhatsAppOfferRequest request) {


        return ResponseEntity.ok(whatsAppService.sendOffer(request));

    }

    /**
     * POST /api/whatsapp/send-reminder
     * Manually trigger appointment reminder for a specific customer
     * Body: { "customerId": "uuid", "message": "Reminder: appointment at 3PM" }
     */
    @PostMapping("/send-reminder")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Manually send appointment reminder via WhatsApp")
    public ResponseEntity<?> sendReminder(
            @Valid @RequestBody WhatsAppRequest request) {

        request.setMessageType(WhatsAppMessageType.APPOINTMENT_REMINDER);
        return ResponseEntity.ok(whatsAppService.send(request));
    }

    /**
     * GET /api/whatsapp/logs?page=0&size=20
     * Get all WhatsApp message logs (admin dashboard)
     */
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all WhatsApp message logs")
    public ResponseEntity<?> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(whatsAppService.getLogs(pageable));
    }
}