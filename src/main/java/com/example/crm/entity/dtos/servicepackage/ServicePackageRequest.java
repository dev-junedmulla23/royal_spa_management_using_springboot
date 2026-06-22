package com.example.crm.entity.dtos.servicepackage;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ServicePackageRequest {

    private String packageName;
    private String description;
    private BigDecimal price;
    private Integer validityInDays;
    private Boolean isActive;

    // IMPORTANT: only IDs
    private List<UUID> serviceIds;
}