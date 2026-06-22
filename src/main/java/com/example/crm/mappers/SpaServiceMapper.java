package com.example.crm.mappers;

import com.example.crm.entity.SpaService;
import com.example.crm.entity.dtos.spaservice.SpaServiceRequest;
import com.example.crm.entity.dtos.spaservice.SpaServiceResponse;
import org.springframework.stereotype.Component;


@Component
public class SpaServiceMapper {

    // Convert RequestDTO → Entity
    public  SpaService toEntity(SpaServiceRequest dto) {
        SpaService service = new SpaService();

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        service.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return service;
    }

    // Convert Entity → ResponseDTO
    public  SpaServiceResponse toResponse(SpaService service) {
        SpaServiceResponse dto = new SpaServiceResponse();

        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDuration(service.getDuration());
        dto.setIsActive(service.getIsActive());

        return dto;
    }


    public  void updateEntity(SpaService service, SpaServiceRequest dto) {


        if (dto.getName() != null) {
            service.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            service.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            service.setPrice(dto.getPrice());
        }

        if (dto.getDuration() != null) {
            service.setDuration(dto.getDuration());
        }

        if (dto.getIsActive() != null) {
            service.setIsActive(dto.getIsActive());
        }
    }
}
