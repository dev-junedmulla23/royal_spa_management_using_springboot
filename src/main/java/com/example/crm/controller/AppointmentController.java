package com.example.crm.controller;

import com.example.crm.entity.dtos.appointment.AppointmentRequest;
import com.example.crm.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("create-appointment")
    public ResponseEntity<?> createAppointment(
            @Valid @RequestBody AppointmentRequest request,
            @RequestParam UUID customerId,
            @RequestParam UUID employeeId,
            @RequestParam UUID serviceId,
            @RequestParam UUID packageId) {

        return ResponseEntity.ok(appointmentService.createAppointment(request, customerId, employeeId, serviceId, packageId));
    }

    @GetMapping("get-all-appointment")
    public ResponseEntity<?> getAllAppointment() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("get-appointment-by-date")
    public ResponseEntity<?> getAppointmentByDate(@RequestParam LocalDate date)
    {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }

    @PutMapping("update-appointment-by-id")
    public ResponseEntity<?> updateAppointmentById(
            @RequestParam UUID id,
            @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(appointmentService.updateAppointmentById(id, request));
    }

    @DeleteMapping("delete-appointment-by-id")
    public ResponseEntity<?> deleteAppointmentById(@RequestParam UUID id) {
        return ResponseEntity.ok(appointmentService.deleteAppointmentById(id));
    }

    @GetMapping("get-appointment-by-employeeid")
    public ResponseEntity<?> getAppointmentsByEmployee(@RequestParam UUID employeeId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByEmployee(employeeId));
    }

    @PutMapping("assign-employee-to-appointment")
    public ResponseEntity<?> assignEmployeeToAppointment(
            @RequestParam UUID appointmentId,
            @RequestParam UUID employeeId) {

        return ResponseEntity.ok(appointmentService.assignEmployeeToAppointment(appointmentId, employeeId));
    }

    @GetMapping("get-daily-appointment-count")
    public ResponseEntity<?> getDailyAppointmentCount(@RequestParam LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDailyAppointmentCount(date));
    }

    @GetMapping("search-appointment-by-date-employeeid-status")
    public ResponseEntity<?> searchAppointments(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) UUID employeeId,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(appointmentService.searchAppointments(date, employeeId, status));
    }
}