package com.example.crm.service.impl;

import com.example.crm.entity.ServicePackage;
import com.example.crm.entity.SpaService;
import com.example.crm.entity.dtos.servicepackage.ServicePackageRequest;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.ServicePackageMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.ServicePackageRepository;
import com.example.crm.repository.SpaServiceRepository;
import com.example.crm.service.ServicePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements ServicePackageService {

    private final ServicePackageRepository servicePackageRepository;
    private final ServicePackageMapper servicePackageMapper;
    private final SpaServiceRepository spaServiceRepository;

    @Override
    public ApiResponse<?> createServicePackage(ServicePackageRequest request) {

        List<SpaService> services = spaServiceRepository.findAllById(request.getServiceIds());

        ServicePackage entity = servicePackageMapper.toEntity(request, services);

        ServicePackage saved = servicePackageRepository.save(entity);

        return ApiResponseUtil.created(servicePackageMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getAllServicePackages() {

        return ApiResponseUtil.fetchedList(
                servicePackageRepository.findAll()
                        .stream()
                        .map(servicePackageMapper::toResponse)
                        .toList()
        );
    }

    @Override
    public ApiResponse<?> getServicePackageById(UUID id) {

        ServicePackage servicePackage = servicePackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));

        return ApiResponseUtil.fetched(servicePackageMapper.toResponse(servicePackage));
    }

    @Override
    public ApiResponse<?> updateServicePackageById(UUID id, ServicePackageRequest request) {

        ServicePackage existing = servicePackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));


        List<SpaService> services = spaServiceRepository.findAllById(request.getServiceIds());

        servicePackageMapper.updateEntity(existing, request, services);

        ServicePackage updated = servicePackageRepository.save(existing);

        return ApiResponseUtil.updated(servicePackageMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<?> deleteServicePackageById(UUID id) {

        ServicePackage servicePackage = servicePackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));

        servicePackage.setIsActive(false);
        servicePackageRepository.save(servicePackage);

        return ApiResponseUtil.deleted();
    }

    @Override
    public ApiResponse<?> getActivePackages() {

        return ApiResponseUtil.fetchedList(
                servicePackageRepository.findByIsActiveTrue()
                        .stream()
                        .map(servicePackageMapper::toResponse)
                        .toList()
        );
    }
}