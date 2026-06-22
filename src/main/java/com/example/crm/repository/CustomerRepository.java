package com.example.crm.repository;

import com.example.crm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {


    boolean existsByMobileNumber(String mobileNumber);


    List<Customer> findByIsActiveFalse();


    List<Customer> findByNameContainingIgnoreCase(String name);


    Optional<Customer> findByMobileNumber(String mobileNumber);


    // Get active customers (visited within last 3 months)
    @Query("SELECT c FROM Customer c WHERE c.lastVisitDate >= :cutoffDate AND c.isActive = true")
    List<Customer> findActiveCustomers(@Param("cutoffDate") LocalDate cutoffDate);

    // Get inactive customers (no visit in 3+ months)
    @Query("SELECT c FROM Customer c WHERE c.lastVisitDate < :cutoffDate OR c.lastVisitDate IS NULL")
    List<Customer> findInactiveCustomers(@Param("cutoffDate") LocalDate cutoffDate);

    // Get customers with birthday today (for scheduler)
    @Query("SELECT c FROM Customer c WHERE MONTH(c.dateOfBirth) = :month AND DAY(c.dateOfBirth) = :day")
    List<Customer> findByBirthdayToday(@Param("month") int month, @Param("day") int day);
}