package com.example.crm.entity.dtos.appointment;

import com.example.crm.util.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class AppointmentRequest {
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotNull(message = "ServicePackage ID is required")
    private UUID servicePackageId;

    private UUID employeeId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;


    private AppointmentStatus status;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}
