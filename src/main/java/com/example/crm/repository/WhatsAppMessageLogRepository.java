package com.example.crm.repository;

import com.example.crm.entity.WhatsAppMessageLog;
import com.example.crm.util.enums.WhatsAppMessageType;
import com.example.crm.util.enums.WhatsAppStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WhatsAppMessageLogRepository extends JpaRepository<WhatsAppMessageLog, UUID> {

    // Get all logs for a specific customer
    List<WhatsAppMessageLog> findByCustomerId(UUID customerId);

    // Get all failed messages (for retry logic)
    List<WhatsAppMessageLog> findByStatusAndRetryCountLessThan(
            WhatsAppStatus status, Integer maxRetries);

    // Get recent logs ordered by time (for admin dashboard)
    Page<WhatsAppMessageLog> findAllByOrderBySentAtDesc(Pageable pageable);

    // Get logs by type (e.g., all birthday messages)
    List<WhatsAppMessageLog> findByMessageType(WhatsAppMessageType type);

    // Count messages sent today
    @Query("SELECT COUNT(w) FROM WhatsAppMessageLog w WHERE w.sentAt >= :startOfDay")
    Long countTodaysMessages(@Param("startOfDay") LocalDateTime startOfDay);
}