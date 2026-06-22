package com.example.crm.entity.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueReportDto {
    private BigDecimal totalRevenue;
    private Long totalBillings;
    private LocalDate fromDate;    // null for all-time report
    private LocalDate toDate;      // null for all-time report
}