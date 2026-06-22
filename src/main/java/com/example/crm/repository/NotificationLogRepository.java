package com.example.crm.repository;

import com.example.crm.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {

    // Get all logs for a specific notification
    List<NotificationLog> findByNotification_Id(UUID notificationId);

    // Get all failed logs (for retry logic)
    List<NotificationLog> findByDeliveryStatus(String deliveryStatus);

    // Recent logs ordered by time (for dashboard)
    Page<NotificationLog> findAllByOrderByAttemptedAtDesc(Pageable pageable);
}