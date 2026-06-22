package com.example.crm.mappers;

import com.example.crm.entity.Appointment;
import com.example.crm.entity.Customer;
import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.entity.dtos.customer.CustomerResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setBloodGroup(request.getBloodGroup());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setAddress(request.getAddress());

        // ✅ FIXED LOGIC
        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        } else {
            customer.setIsActive(true);
        }

        // ✅ NEW FIELD
        customer.setFcmToken(request.getFcmToken());

        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setBloodGroup(customer.getBloodGroup());
        response.setMobileNumber(customer.getMobileNumber());
        response.setAddress(customer.getAddress());
        response.setIsActive(customer.getIsActive());
        response.setLastVisitDate(customer.getLastVisitDate());

        // ✅ NEW FIELD
        response.setFcmToken(customer.getFcmToken());

        if (customer.getAppointments() != null && !customer.getAppointments().isEmpty()) {

            List<String> appointmentDates = customer.getAppointments()
                    .stream()
                    .map(Appointment::getAppointmentDate)
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            response.setAppointmentDates(appointmentDates);
        }

        return response;
    }

    public void updateEntity(Customer customer, CustomerRequest request) {

        if (request.getName() != null) {
            customer.setName(request.getName());
        }

        if (request.getDateOfBirth() != null) {
            customer.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getBloodGroup() != null) {
            customer.setBloodGroup(request.getBloodGroup());
        }

        if (request.getMobileNumber() != null) {
            customer.setMobileNumber(request.getMobileNumber());
        }

        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }

        // ✅ NEW FIELD UPDATE
        if (request.getFcmToken() != null) {
            customer.setFcmToken(request.getFcmToken());
        }
    }
}