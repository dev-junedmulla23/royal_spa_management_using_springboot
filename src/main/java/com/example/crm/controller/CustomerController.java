package com.example.crm.controller;

import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @GetMapping("/get-all-customers")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/get-customer-by-id")
    public ResponseEntity<?> getCustomerById(@RequestParam UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/update-customer-by-id")
    public ResponseEntity<?> updateCustomerById(@RequestParam UUID id,
                                                @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.UpdateCustomerById(id, request));
    }

    @DeleteMapping("/delete-customer-by-id")
    public ResponseEntity<?> deleteCustomerById(@RequestParam UUID id) {
        return ResponseEntity.ok(customerService.deleteCustomerById(id));
    }

    @GetMapping("/get-all-inactive-customer")
    public ResponseEntity<?> getInactiveCustomer() {
        return ResponseEntity.ok(customerService.getInactiveCustomer());
    }

    @GetMapping("/search-by-name-or-mobile")
    public ResponseEntity<?> searchCustomer(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile) {

        return ResponseEntity.ok(customerService.searchCustomers(name, mobile));
    }

    @GetMapping("/get-customer-history")
    public ResponseEntity<?> getCustomerHistory(@RequestParam UUID id) {
        return ResponseEntity.ok(customerService.getCustomerHistory(id));
    }
}