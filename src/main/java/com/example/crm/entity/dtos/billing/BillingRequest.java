package com.example.crm.entity.dtos.billing;

import com.example.crm.util.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BillingRequest {

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Appointment ID is required")
    private UUID appointmentId;

    private BigDecimal discountAmount; // optional

    @NotNull(message = "Paid amount is required")
    private BigDecimal paidAmount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}