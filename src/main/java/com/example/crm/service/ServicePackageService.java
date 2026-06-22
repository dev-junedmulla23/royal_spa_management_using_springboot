package com.example.crm.service;

import com.example.crm.entity.dtos.servicepackage.ServicePackageRequest;
import com.example.crm.payload.ApiResponse;

import java.util.UUID;

public interface ServicePackageService {

    ApiResponse<?> createServicePackage(ServicePackageRequest servicePackageRequest);

    ApiResponse<?> getAllServicePackages();

    ApiResponse<?> getServicePackageById(UUID id);

    ApiResponse<?> updateServicePackageById(UUID id,ServicePackageRequest servicePackageRequest);

    ApiResponse<?> deleteServicePackageById(UUID id);

    ApiResponse<?> getActivePackages();
}
