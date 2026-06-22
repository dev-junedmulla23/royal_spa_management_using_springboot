package com.example.crm.entity.dtos.spaservice;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SpaServiceResponse {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Boolean isActive;
}