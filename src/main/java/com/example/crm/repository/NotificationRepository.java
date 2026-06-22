package com.example.crm.repository;

import com.example.crm.entity.Notification;
import com.example.crm.util.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // Find all notifications for a specific customer
    List<Notification> findByCustomer_Id(UUID customerId);

    // Find notifications by type (e.g., all BIRTHDAY notifications)
    List<Notification> findByType(NotificationType type);

    // Find notifications sent on a specific date
    List<Notification> findBySentAtBetween(LocalDateTime start, LocalDateTime end);
}