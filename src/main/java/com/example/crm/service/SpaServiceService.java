package com.example.crm.service;

import com.example.crm.entity.dtos.spaservice.SpaServiceRequest;
import com.example.crm.payload.ApiResponse;

import java.util.UUID;

public interface SpaServiceService {
    ApiResponse<?> createService(SpaServiceRequest spaServiceRequest);

    ApiResponse<?> getAllServices();

    ApiResponse<?> getServiceById(UUID id);

    ApiResponse<?> updateServiceById(UUID id, SpaServiceRequest spaServiceRequest);

    ApiResponse<?> deleteServiceById(UUID id);

    ApiResponse<?> getActiveServices();
}
