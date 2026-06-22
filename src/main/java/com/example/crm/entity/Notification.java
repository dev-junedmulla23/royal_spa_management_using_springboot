package com.example.crm.entity;

import com.example.crm.audit.Audit;
import com.example.crm.util.enums.NotificationStatus;
import com.example.crm.util.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Who receives the notification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Notification title shown on phone
    @Column(nullable = false)
    private String title;

    // Notification message body
    @Column(nullable = false, length = 500)
    private String body;

    // Type: BIRTHDAY, APPOINTMENT_REMINDER, CUSTOM
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // SENT, FAILED
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    // Timestamp when notification was triggered
    private LocalDateTime sentAt;
}

