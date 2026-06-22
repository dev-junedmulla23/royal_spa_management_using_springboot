package com.example.crm.entity.dtos.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {

    private UUID id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String bloodGroup;

    private String mobileNumber;

    private String address;

    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastVisitDate;

    private List<String> appointmentDates;

    private String fcmToken;
}