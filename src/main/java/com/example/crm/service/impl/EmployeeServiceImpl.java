package com.example.crm.service.impl;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Employee;
import com.example.crm.entity.dtos.employee.EmployeeRequest;
import com.example.crm.entity.dtos.employee.EmployeeResponse;
import com.example.crm.exceptions.handlers.DuplicateRecordException;
import com.example.crm.exceptions.handlers.ResourceInUseException;
import com.example.crm.exceptions.handlers.ResourceNotFoundException;
import com.example.crm.mappers.EmployeeMapper;
import com.example.crm.payload.ApiResponse;
import com.example.crm.payload.ApiResponseUtil;
import com.example.crm.repository.EmployeeRepository;
import com.example.crm.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public ApiResponse<?> createEmployee(EmployeeRequest request, AppUser appUser) {

        if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {
            throw new IllegalArgumentException("Mobile number is required");
        }

        if (employeeRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateRecordException("Mobile number already exists");
        }

        Employee employee = employeeMapper.toEntity(request, appUser);
        employee.setIsActive(true);

        Employee saved = employeeRepository.save(employee);

        return ApiResponseUtil.created(employeeMapper.toResponse(saved));
    }

    @Override
    public ApiResponse<?> getAllEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return ApiResponseUtil.fetchedList(List.of());
        }

        List<EmployeeResponse> list = employees.stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> updateEmployeeById(UUID id, EmployeeRequest request, AppUser appUser) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        if (request.getMobileNumber() != null &&
                !request.getMobileNumber().equals(existing.getMobileNumber()) &&
                employeeRepository.existsByMobileNumber(request.getMobileNumber())) {

            throw new ResourceInUseException("Mobile number already exists");
        }

        employeeMapper.updateEntity(existing, request, appUser);

        Employee updated = employeeRepository.save(existing);

        return ApiResponseUtil.updated(employeeMapper.toResponse(updated));
    }

    @Override
    public ApiResponse<?> getEmployeeById(UUID id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        return ApiResponseUtil.fetched(employeeMapper.toResponse(employee));
    }

    @Override
    public ApiResponse<?> deleteEmployeeById(UUID id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        employee.setIsActive(false);
        employeeRepository.save(employee);

        return ApiResponseUtil.deleted();
    }

    @Override
    public ApiResponse<?> getInactiveEmployee() {

        List<EmployeeResponse> list = employeeRepository.findByIsActiveFalse()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> getAvailableEmployee() {

        List<EmployeeResponse> list = employeeRepository.findByIsActiveTrue()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }

    @Override
    public ApiResponse<?> searchEmployee(String name, String mobile) {

        List<Employee> employees;

        if (name != null && !name.isEmpty() && mobile != null && !mobile.isEmpty()) {
            employees = employeeRepository.findByNameContainingIgnoreCaseAndMobileNumber(name, mobile);
        } else if (name != null && !name.isEmpty()) {
            employees = employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (mobile != null && !mobile.isEmpty()) {
            Employee employee = employeeRepository.findByMobileNumber(mobile)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with mobile " + mobile));
            employees = List.of(employee);
        } else {
            return ApiResponseUtil.fetchedList(List.of());
        }

        List<EmployeeResponse> list = employees.stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponseUtil.fetchedList(list);
    }
}