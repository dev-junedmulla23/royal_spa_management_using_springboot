package com.example.crm.service.impl;

import com.example.crm.entity.SpaService;
import com.example.crm.entity.dtos.spaservice.SpaServiceRequest;
import com.example.crm.entity.dtos.spaservice.SpaServiceResponse;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.SpaServiceMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.SpaServiceRepository;
import com.example.crm.service.SpaServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaServiceServiceImpl implements SpaServiceService {

    private final SpaServiceRepository spaServiceRepository;
    private final SpaServiceMapper spaServiceMapper;

    @Override
    public ApiResponse<?> createService(SpaServiceRequest spaServiceRequest) {

        SpaService spaService = spaServiceMapper.toEntity(spaServiceRequest);
        SpaService saved = spaServiceRepository.save(spaService);

        return ApiResponseUtil.created(spaServiceMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getAllServices() {

        List<SpaServiceResponse> list = spaServiceRepository.findAll().stream().map(spaServiceMapper::toResponse).toList();

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> getServiceById(UUID id) {
        SpaService spaService = spaServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spa Service not found with id " + id));

        return ApiResponseUtil.fetched(spaServiceMapper.toResponse(spaService));
    }

    @Override
    public ApiResponse<?> updateServiceById(UUID id, SpaServiceRequest spaServiceRequest) {

            SpaService existing = spaServiceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            spaServiceMapper.updateEntity(existing,spaServiceRequest);

            SpaService updated = spaServiceRepository.save(existing);

            return ApiResponseUtil.updated(spaServiceMapper.toResponse(updated));

    }

    @Override
    public ApiResponse<?> deleteServiceById(UUID id) {

        SpaService spaService=spaServiceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Spa Service not found with "+id));
        spaService.setIsActive(false);
        spaServiceRepository.save(spaService);
        return ApiResponseUtil.deleted();
    }

    @Override
    public ApiResponse<?> getActiveServices() {

        return ApiResponseUtil.fetchedList(spaServiceRepository.findByIsActiveTrue()
                .stream()
                .map(spaServiceMapper::toResponse)
                .toList());
    }
}
