package com.example.crm.service;

import com.example.crm.entity.dtos.billing.BillingRequest;
import com.example.crm.payload.ApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface BillingService {

    ApiResponse<?> createBilling(BillingRequest request);

    ApiResponse<?> getAllBillings();

    ApiResponse<?> getBillingById(Long id);

    ApiResponse<?> getBillingsByCustomerId(UUID customerId);

    ApiResponse<?> getTodayRevenue();

    ApiResponse<?> getTotalRevenue();

    ApiResponse<?> updatePayment(Long id, BigDecimal paidAmount);

}
