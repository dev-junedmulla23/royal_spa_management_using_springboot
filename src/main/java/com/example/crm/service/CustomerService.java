package com.example.crm.service;


import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CustomerService {

    ApiResponse<?> createCustomer(CustomerRequest request);

    ApiResponse<?> getAllCustomers();

    ApiResponse<?> UpdateCustomerById(UUID id, CustomerRequest request);

    ApiResponse<?> getCustomerById(UUID id);

    ApiResponse<?> deleteCustomerById(UUID id);

    ApiResponse<?> getInactiveCustomer();

    ApiResponse<?> searchCustomers(String name, String mobile);

    ApiResponse<?> getCustomerHistory(UUID id);
}
