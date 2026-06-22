package com.example.crm.entity;

import com.example.crm.audit.Audit;
import com.example.crm.util.enums.WhatsAppMessageType;
import com.example.crm.util.enums.WhatsAppStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "whatsapp_message_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhatsAppMessageLog extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Customer who received the message (nullable — for broadcast messages)
    @Column(name = "customer_id")
    private UUID customerId;

    // Customer's phone number in WhatsApp format: whatsapp:+91XXXXXXXXXX
    @Column(nullable = false)
    private String phoneNumber;

    // The actual message text that was sent
    @Column(nullable = false, length = 1600)
    private String message;

    // Message type: BIRTHDAY, APPOINTMENT_REMINDER, OFFER, CUSTOM
    @Enumerated(EnumType.STRING)
    private WhatsAppMessageType messageType;

    // SUCCESS or FAILED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WhatsAppStatus status;

    // Twilio message SID (returned on success — useful for tracking)
    private String twilioMessageSid;

    // Error details if sending failed
    @Column(length = 500)
    private String errorMessage;

    // Number of retry attempts made
    @Builder.Default
    private Integer retryCount = 0;

    // When the message was sent
    private LocalDateTime sentAt;
}

