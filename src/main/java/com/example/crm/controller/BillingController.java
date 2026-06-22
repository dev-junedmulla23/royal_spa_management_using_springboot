package com.example.crm.controller;

import com.example.crm.entity.dtos.billing.BillingRequest;
import com.example.crm.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/create-billing")
    public ResponseEntity<?> createBilling(@Valid @RequestBody BillingRequest billingRequest) {
        return ResponseEntity.ok(billingService.createBilling(billingRequest));
    }


    @GetMapping("/get-all-billings")
    public ResponseEntity<?> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }


    @GetMapping("/get-billing-by-id")
    public ResponseEntity<?> getBillingById(@RequestParam Long id) {
        return ResponseEntity.ok(billingService.getBillingById(id));
    }


    @GetMapping("/get-billings-by-customer-id")
    public ResponseEntity<?> getBillingsByCustomerId(@RequestParam UUID customerId) {
        return ResponseEntity.ok(billingService.getBillingsByCustomerId(customerId));
    }


    @PutMapping("/update-payment")
    public ResponseEntity<?> updatePayment(
            @RequestParam Long billingId,
            @RequestParam BigDecimal paidAmount
    ) {
        return ResponseEntity.ok(billingService.updatePayment(billingId, paidAmount));
    }


    @GetMapping("/get-today-revenue")
    public ResponseEntity<?> getTodayRevenue() {
        return ResponseEntity.ok(billingService.getTodayRevenue());
    }


    @GetMapping("/get-total-revenue")
    public ResponseEntity<?> getTotalRevenue() {
        return ResponseEntity.ok(billingService.getTotalRevenue());
    }
}