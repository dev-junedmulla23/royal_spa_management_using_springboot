package com.example.crm.service;

import com.example.crm.payload.ApiResponse;
import java.time.LocalDate;

public interface ReportService {

    ApiResponse<?> getTotalRevenue();

    ApiResponse<?> getRevenueByDate(LocalDate from, LocalDate to);

    ApiResponse<?> getActiveCustomers();

    ApiResponse<?> getInactiveCustomers();

    ApiResponse<?> getDailyAppointmentCount();
}