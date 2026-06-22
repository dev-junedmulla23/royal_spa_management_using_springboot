package com.example.crm.service.impl;

import com.example.crm.entity.*;
import com.example.crm.entity.dtos.appointment.AppointmentRequest;
import com.example.crm.entity.dtos.appointment.AppointmentResponse;
import com.example.crm.exceptions.handlers.ResourceInUseException;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.AppointmentMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.*;
import com.example.crm.service.AppointmentService;
import com.example.crm.service.WhatsAppService;          // ← NEW IMPORT
import com.example.crm.util.enums.AppointmentStatus;
import com.example.crm.util.enums.WhatsAppMessageType;  // ← NEW IMPORT
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;                        // ← NEW IMPORT
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository    appointmentRepository;
    private final EmployeeRepository        employeeRepository;
    private final CustomerRepository        customerRepository;
    private final ServicePackageRepository  servicePackageRepository;
    private final SpaServiceRepository      spaServiceRepository;
    private final AppointmentMapper         appointmentMapper;
    private final WhatsAppService           whatsAppService;

    @Override
    public ApiResponse<?> createAppointment(
            AppointmentRequest request,
            UUID customerId,
            UUID employeeId,
            UUID serviceId,
            UUID packageId) {

        // 1. Fetch required entities
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + customerId));

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        SpaService spaService = spaServiceRepository
                .findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service not found with id: " + serviceId));

        ServicePackage servicePackage = servicePackageRepository
                .findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Package not found with id: " + packageId));

        // 2. Guard: inactive employee
        if (Boolean.FALSE.equals(employee.getIsActive())) {
            throw new ResourceInUseException("Employee is inactive");
        }

        // 3. Save appointment
        Appointment appointment =
                appointmentMapper.toEntity(request, customer, employee, spaService, servicePackage);
        appointment.setStatus(AppointmentStatus.BOOKED);
        Appointment saved = appointmentRepository.save(appointment);

        // 4. Send WhatsApp confirmation (non-blocking)
        sendAppointmentConfirmation(saved);

        return ApiResponseUtil.created(appointmentMapper.toResponse(saved));
    }


    // ─────────────────────────────────────────────────────────────────
    // PRIVATE HELPER — WhatsApp Confirmation
    // ─────────────────────────────────────────────────────────────────
    private void sendAppointmentConfirmation(Appointment appointment) {
        try {

            // Null-guard: all fields required for the message
            if (appointment.getCustomer() == null) {
                log.warn("[WhatsApp] Skipped: customer is null for appointment {}",
                        appointment.getId());
                return;
            }

            Customer   customer   = appointment.getCustomer();
            Employee   employee   = appointment.getAssignedEmployee();
            SpaService spaService = appointment.getSpaService();
            ServicePackage servicePackage=appointment.getSelectedPackage();

            String customerName  = customer.getName()   != null ? customer.getName()   : "Valued Customer";
            String customerPhone = customer.getMobileNumber();
            String employeeName  = (employee   != null && employee.getName()   != null) ? employee.getName()   : "Our Staff";
            String servicepackage  = (servicePackage != null && servicePackage.getPackageName()!= null) ? servicePackage.getPackageName(): "Our Service package";
            String spaservice  = (spaService != null && spaService.getName()!= null) ? spaService.getName(): "Our Service";

            if (customerPhone == null || customerPhone.isBlank()) {
                log.warn("[WhatsApp] Skipped: no phone number for customer {}",
                        customer.getId());
                return;
            }

            // Build formatted message
            String message = String.format(
                    "Hello %s,%n"
                            + "Your appointment has been successfully booked.%n%n"
                            + "\uD83D\uDCC5 Date: %s%n"
                            + "\u23F0 Time: %s%n"
                            + "\uD83D\uDC68\u200D\uD83D\uDCBC Staff: %s%n"
                            + "\uD83D\uDC86 Service: %s%n"
                            + "\uD83D\uDCE6 Package: %s%n%n"   // 📦  ← ADDED
                            + "Thank you for choosing us!",
                    customerName,
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(),
                    employeeName,
                    spaservice,      // ← was servicepackage, now spaservice
                    servicepackage   // ← ADDED as second-to-last argument
            );

            // Delegate to WhatsAppService (logs internally)
            whatsAppService.sendWhatsAppMessage(
                    customerPhone,
                    message,
                    customer.getId(),
                    WhatsAppMessageType.APPOINTMENT_CONFIRMATION
            );

            log.info("[WhatsApp] ✅ Confirmation sent for appointment {} to {}",
                    appointment.getId(), customerPhone);

        } catch (Exception e) {

            // CRITICAL: Never let WhatsApp failure break appointment creation
            log.error("[WhatsApp] ❌ Failed to send confirmation for appointment {}: {}",
                    appointment.getId(), e.getMessage());
        }
    }


    // ─────────────────────────────────────────────────────────────────
    // REMAINING METHODS — UNCHANGED
    // ─────────────────────────────────────────────────────────────────

    @Override
    public ApiResponse<?> getAllAppointments() {
        List<AppointmentResponse> list = appointmentRepository.findAll().stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> getAppointmentsByDate(LocalDate appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        List<AppointmentResponse> responses = appointmentRepository
                .findByAppointmentDate(appointmentDate).stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponseUtil.fetchedList(responses);
    }

    @Override
    public ApiResponse<?> getAppointmentsByEmployee(UUID employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee Id is required");
        }
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));
        List<AppointmentResponse> list = appointmentRepository
                .findByAssignedEmployee_Id(employeeId).stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> updateAppointmentById(UUID id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));
        if (appointment.getAppointmentDate() != null)
            appointment.setAppointmentDate(request.getAppointmentDate());
        if (appointment.getAppointmentTime() != null)
            appointment.setAppointmentTime(request.getAppointmentTime());
        if (appointment.getStatus() != null)
            appointment.setStatus(request.getStatus());
        if (appointment.getAssignedEmployee() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with id: " + request.getEmployeeId()));
            if (Boolean.FALSE.equals(employee.getIsActive()))
                throw new ResourceInUseException("Employee is inactive");
            appointment.setAssignedEmployee(employee);
        }
        Appointment updated = appointmentRepository.save(appointment);
        return ApiResponseUtil.updated(appointmentMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<?> deleteAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return ApiResponseUtil.deleted();
    }

    @Override
    public ApiResponse<?> assignEmployeeToAppointment(UUID appointmentId, UUID employeeId) {
        if (appointmentId == null || employeeId == null)
            throw new IllegalArgumentException("AppointmentId and EmployeeId are required");
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));
        if (Boolean.FALSE.equals(employee.getIsActive()))
            throw new ResourceInUseException("Employee is inactive");
        appointment.setAssignedEmployee(employee);
        appointment.setStatus(AppointmentStatus.BOOKED);
        Appointment saved = appointmentRepository.save(appointment);
        return ApiResponseUtil.success(appointmentMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getDailyAppointmentCount(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Date is required");
        long count = appointmentRepository.countByAppointmentDate(date);
        return ApiResponseUtil.success(count);
    }

    @Override
    public ApiResponse<?> searchAppointments(LocalDate date, UUID employeeId, String status) {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (date != null)
            appointments = appointments.stream()
                    .filter(a -> date.equals(a.getAppointmentDate()))
                    .collect(Collectors.toList());
        if (employeeId != null)
            appointments = appointments.stream()
                    .filter(a -> a.getAssignedEmployee() != null
                            && employeeId.equals(a.getAssignedEmployee().getId()))
                    .collect(Collectors.toList());
        if (status != null && !status.isEmpty()) {
            AppointmentStatus apptStatus = AppointmentStatus.valueOf(status.toUpperCase());
            appointments = appointments.stream()
                    .filter(a -> apptStatus.equals(a.getStatus()))
                    .collect(Collectors.toList());
        }
        List<AppointmentResponse> list = appointments.stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponseUtil.fetchedList(list);
    }
}