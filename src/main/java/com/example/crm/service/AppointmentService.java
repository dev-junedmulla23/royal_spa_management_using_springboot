package com.example.crm.service;

import com.example.crm.entity.dtos.appointment.AppointmentRequest;
import com.example.crm.payload.ApiResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface AppointmentService {

    ApiResponse<?> createAppointment(AppointmentRequest request, UUID customerId, UUID employeeId, UUID serviceId, UUID packageId);

    ApiResponse<?> getAllAppointments();

    ApiResponse<?> getAppointmentsByDate(LocalDate appointmentDate);

    ApiResponse<?> getAppointmentsByEmployee(UUID employeeId);

    ApiResponse<?> updateAppointmentById(UUID id, AppointmentRequest request);

    ApiResponse<?> deleteAppointmentById(UUID id);

    ApiResponse<?> assignEmployeeToAppointment(UUID appointmentId, UUID employeeId);

    ApiResponse<?> getDailyAppointmentCount(LocalDate date);

    ApiResponse<?> searchAppointments(LocalDate date, UUID employeeId, String status);
}