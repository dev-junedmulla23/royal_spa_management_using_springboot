package com.example.crm.mappers;

import com.example.crm.entity.ServicePackage;
import com.example.crm.entity.SpaService;
import com.example.crm.entity.dtos.servicepackage.ServicePackageRequest;
import com.example.crm.entity.dtos.servicepackage.ServicePackageResponse;
import com.example.crm.entity.dtos.servicepackage.SimpleServiceDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServicePackageMapper {

    // Convert RequestDTO → Entity
        public  ServicePackage toEntity(ServicePackageRequest dto, List<SpaService> services) {
            ServicePackage servicePackage = new ServicePackage();

            servicePackage.setPackageName(dto.getPackageName());
            servicePackage.setDescription(dto.getDescription());
            servicePackage.setPrice(dto.getPrice());
            servicePackage.setValidityInDays(dto.getValidityInDays());
            servicePackage.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

            // Set linked services
            servicePackage.setServices(services);

            return servicePackage;
        }

        // Convert Entity → ResponseDTO
        public  ServicePackageResponse toResponse(ServicePackage servicePackage) {
            ServicePackageResponse dto = new ServicePackageResponse();

            dto.setId(servicePackage.getId());
            dto.setPackageName(servicePackage.getPackageName());
            dto.setDescription(servicePackage.getDescription());
            dto.setPrice(servicePackage.getPrice());
            dto.setValidityInDays(servicePackage.getValidityInDays());
            dto.setIsActive(servicePackage.getIsActive());

            // Map services → SimpleServiceDTO
            if (servicePackage.getServices() != null) {
                List<SimpleServiceDTO> services = servicePackage.getServices()
                        .stream()
                        .map(service -> {
                            SimpleServiceDTO s = new SimpleServiceDTO();
                            s.setId(service.getId());
                            s.setName(service.getName());
                            s.setPrice(service.getPrice());
                            return s;
                        })
                        .collect(Collectors.toList());

                dto.setServices(services);
            }

            return dto;

        }

    public void updateEntity(
            ServicePackage servicePackage,
            ServicePackageRequest dto,
            List<SpaService> services // fetched from DB
    ) {

        if (dto.getPackageName() != null) {
            servicePackage.setPackageName(dto.getPackageName());
        }

        if (dto.getDescription() != null) {
            servicePackage.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            servicePackage.setPrice(dto.getPrice());
        }

        if (dto.getValidityInDays() != null) {
            servicePackage.setValidityInDays(dto.getValidityInDays());
        }

        if (dto.getIsActive() != null) {
            servicePackage.setIsActive(dto.getIsActive());
        }

        // Update services if provided
        if (services != null && !services.isEmpty()) {
            servicePackage.setServices(services);
        }
    }
}
