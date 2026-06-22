package com.example.crm.mappers;

import com.example.crm.entity.*;
import com.example.crm.entity.dtos.appointment.AppointmentRequest;
import com.example.crm.entity.dtos.appointment.AppointmentResponse;
import com.example.crm.util.enums.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

   public Appointment toEntity(AppointmentRequest request,
                         Customer customer, Employee employee,
                         SpaService spaService, ServicePackage servicePackage) {

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setAssignedEmployee(employee);
        appointment.setSelectedPackage(servicePackage);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        } else {
            appointment.setStatus(AppointmentStatus.BOOKED);
        }

        appointment.setNotes(request.getNotes());

        return appointment;
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                //Customer
                .customerId(appointment.getCustomer().getId())
                .customerName(appointment.getCustomer().getName())

                //Employee
                .employeeId(appointment.getAssignedEmployee() != null ? appointment.getAssignedEmployee().getId(): null)
                .employeeName(appointment.getAssignedEmployee() != null ? appointment.getAssignedEmployee().getName() : null)

                //Services
                .serviceId(appointment.getSpaService() != null ? appointment.getSpaService().getId() : null)
                .serviceName(appointment.getSpaService() != null ? appointment.getSpaService().getName() : null)

                //ServicePackage
                .servicePackageId(appointment.getSelectedPackage() != null ? appointment.getSelectedPackage().getId() : null)
                .servicePackageName(appointment.getSelectedPackage() != null ? appointment.getSelectedPackage().getPackageName() : null)

                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .build();

    }

    public void updateEntity(Appointment appointment,
                      AppointmentRequest request,
                      Customer customer,
                      Employee employee,
                      SpaService spaService,
                      ServicePackage servicePackage) {


        if (customer != null) {
            appointment.setCustomer(customer);
        }

        if (employee != null) {
            appointment.setAssignedEmployee(employee);
        }

        if (spaService != null) {
            appointment.setSpaService(spaService);
        }

        if (servicePackage != null) {
            appointment.setSelectedPackage(servicePackage);
        }


        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(request.getAppointmentDate());
        }

        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
    }


}

