package com.example.crm.service.impl;


import com.example.crm.entity.Customer;
import com.example.crm.entity.dtos.report.CustomerReportDto;
import com.example.crm.entity.dtos.report.RevenueReportDto;
import com.example.crm.helper.reporthelper.ReportHelper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.AppointmentRepository;
import com.example.crm.repository.BillingRepository;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final BillingRepository billingRepository;
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final ReportHelper reportHelper;

    // ✅ 1. Total Revenue
    @Override
    public ApiResponse<?> getTotalRevenue() {

        BigDecimal total = billingRepository.findTotalRevenue();
        Long count = billingRepository.count();

        RevenueReportDto response = RevenueReportDto.builder()
                .totalRevenue(total != null ? total : BigDecimal.ZERO)
                .totalBillings(count)
                .build();

        return ApiResponseUtil.fetched(response);
    }

    // ✅ 2. Revenue By Date
    @Override
    public ApiResponse<?> getRevenueByDate(LocalDate from, LocalDate to) {

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        BigDecimal revenue = billingRepository.findRevenueByDateRange(start, end);
        Long count = billingRepository.countByCreatedAtBetween(start, end);

        RevenueReportDto response = RevenueReportDto.builder()
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .totalBillings(count)
                .fromDate(from)
                .toDate(to)
                .build();

        return ApiResponseUtil.fetched(response);
    }

    // ✅ 3. Active Customers
    @Override
    public ApiResponse<?> getActiveCustomers() {

        LocalDate cutoff = LocalDate.now().minusMonths(3);
        List<Customer> customers = customerRepository.findActiveCustomers(cutoff);

        CustomerReportDto response = reportHelper.buildCustomerReport(customers);

        return ApiResponseUtil.fetched(response);
    }

    // ✅ 4. Inactive Customers
    @Override
    public ApiResponse<?> getInactiveCustomers() {

        LocalDate cutoff = LocalDate.now().minusMonths(3);
        List<Customer> customers = customerRepository.findInactiveCustomers(cutoff);

        CustomerReportDto response = reportHelper.buildCustomerReport(customers);

        return ApiResponseUtil.fetched(response);
    }

    // ✅ 5. Daily Appointment Count
    @Override
    public ApiResponse<?> getDailyAppointmentCount() {

        Long count = appointmentRepository.countByAppointmentDate(LocalDate.now());

        return ApiResponseUtil.fetched(count);
    }
}