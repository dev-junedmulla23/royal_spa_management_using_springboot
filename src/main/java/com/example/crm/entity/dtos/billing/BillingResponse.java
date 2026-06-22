package com.example.crm.entity.dtos.billing;

import com.example.crm.util.enums.PaymentMethod;
import com.example.crm.util.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillingResponse {

    private Long id;

    private String invoiceNumber;

    private String customerName;

    private String serviceName;

    private String packageName;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal paidAmount;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private LocalDate billingDate;
}