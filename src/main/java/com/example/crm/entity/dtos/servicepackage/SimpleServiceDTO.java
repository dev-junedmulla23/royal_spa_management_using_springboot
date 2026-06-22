package com.example.crm.entity.dtos.servicepackage;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SimpleServiceDTO {

    private UUID id;
    private String name;
    private BigDecimal price;
}