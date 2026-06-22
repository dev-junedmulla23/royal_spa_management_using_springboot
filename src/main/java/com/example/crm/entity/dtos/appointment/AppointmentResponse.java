package com.example.crm.entity.dtos.appointment;


import com.example.crm.util.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    private UUID id;

    private UUID customerId;
    private String customerName;


    private UUID employeeId;
    private String employeeName;

    private UUID serviceId;
    private String serviceName;

    private UUID servicePackageId;
    private String servicePackageName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private String notes;
}