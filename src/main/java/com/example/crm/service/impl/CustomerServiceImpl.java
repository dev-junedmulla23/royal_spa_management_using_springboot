package com.example.crm.service.impl;

import com.example.crm.entity.Customer;
import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.entity.dtos.customer.CustomerResponse;
import com.example.crm.exceptions.handlers.DuplicateRecordException;
import com.example.crm.exceptions.handlers.ResourceInUseException;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.CustomerMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public ApiResponse<?> createCustomer(CustomerRequest request) {

        if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateRecordException("Mobile number already exists");
        }

        Customer customer = customerMapper.toEntity(request);
        Customer saved = customerRepository.save(customer);

        return ApiResponseUtil.created(customerMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getAllCustomers() {

        List<CustomerResponse> list = customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> UpdateCustomerById(UUID id, CustomerRequest request) {

        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (request.getMobileNumber() != null &&
                !request.getMobileNumber().equals(existing.getMobileNumber()) &&
                customerRepository.existsByMobileNumber(request.getMobileNumber())) {

            throw new ResourceInUseException("Mobile number already exists");
        }

        customerMapper.updateEntity(existing, request);

        Customer updated = customerRepository.save(existing);

        return ApiResponseUtil.updated(customerMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<?> getCustomerById(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return ApiResponseUtil.fetched(customerMapper.toResponse(customer));
    }

    @Override
    public ApiResponse<?> deleteCustomerById(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setIsActive(false);
        customerRepository.save(customer);

        return ApiResponseUtil.deleted();
    }

    @Override
    public ApiResponse<?> getInactiveCustomer() {

        List<CustomerResponse> list = customerRepository.findByIsActiveFalse()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> searchCustomers(String name, String mobile) {

        List<Customer> customers;

        if (name != null && !name.isEmpty()) {
            customers = customerRepository.findByNameContainingIgnoreCase(name);

        } else if (mobile != null && !mobile.isEmpty()) {

            Customer customer = customerRepository.findByMobileNumber(mobile)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            customers = List.of(customer);

        } else {
            customers = customerRepository.findAll();
        }

        List<CustomerResponse> list = customers.stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> getCustomerHistory(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return ApiResponseUtil.fetched(customerMapper.toResponse(customer));
    }
}