package com.example.crm.controller;


import com.example.crm.payload.ApiResponse;
import com.example.crm.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports API")
public class ReportController {

    private final ReportService reportService;

    /**
     * Total Revenue
     */
    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getTotalRevenue() {

        return ResponseEntity.ok(
                reportService.getTotalRevenue()
        );
    }

    /**
     * Revenue By Date
     */
    @GetMapping("/revenue/by-date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getRevenueByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(
                reportService.getRevenueByDate(from, to)
        );
    }

    /**
     * Active Customers
     */
    @GetMapping("/customers/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getActiveCustomers() {

        return ResponseEntity.ok(
                reportService.getActiveCustomers()
        );
    }

    /**
     * Inactive Customers
     */
    @GetMapping("/customers/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getInactiveCustomers() {

        return ResponseEntity.ok(
                reportService.getInactiveCustomers()
        );
    }

    /**
     * Daily Appointment Count
     */
    @GetMapping("/appointments/daily-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> getDailyCount() {

        return ResponseEntity.ok(
                reportService.getDailyAppointmentCount()
        );
    }
}