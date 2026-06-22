package com.example.crm.entity.dtos.spaservice;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpaServiceRequest{

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Boolean isActive;
}