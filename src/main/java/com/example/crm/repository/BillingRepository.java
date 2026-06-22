package com.example.crm.repository;

import com.example.crm.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface  BillingRepository  extends JpaRepository<Billing,Long> {

    List<Billing> findByCustomer_Id(UUID customerId);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.billingDate = :today")
    BigDecimal getTodayRevenue(LocalDate today);

    @Query("SELECT COUNT(b) FROM Billing b WHERE b.billingDate = :today")
    Long countTodayBills(LocalDate today);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b")
    BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(b) FROM Billing b")
    Long countAllBills();

    // Total revenue — NO status filter
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b")
    BigDecimal findTotalRevenue();

    // Revenue between two dates — NO status filter
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b " +
            "WHERE b.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal findRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    // Count billings between dates
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
