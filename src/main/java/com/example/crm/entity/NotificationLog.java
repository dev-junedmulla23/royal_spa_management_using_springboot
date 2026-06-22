package com.example.crm.entity;

import com.example.crm.audit.Audit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Links back to the Notification that was sent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    // SUCCESS or FAILURE
    @Column(nullable = false)
    private String deliveryStatus;

    // FCM response message or error message
    @Column(length = 1000)
    private String responseMessage;

    // FCM token used for this delivery attempt
    private String fcmToken;

    // When the attempt happened
    private LocalDateTime attemptedAt;
}