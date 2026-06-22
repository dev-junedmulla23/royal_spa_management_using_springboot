package com.example.crm.controller;

import com.example.crm.entity.AppUser;
import com.example.crm.entity.Employee;
import com.example.crm.entity.dtos.appUserDtos.AppUserRequest;
import com.example.crm.entity.dtos.customer.CustomerRequest;
import com.example.crm.entity.dtos.employee.EmployeeRequest;
import com.example.crm.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/employee/")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("create-employee")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequest request, AppUser appUser) {
        return ResponseEntity.ok(employeeService.createEmployee(request, appUser));
    }

    @GetMapping("get-all-employees")
    public ResponseEntity<?> getAllEmployee() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("get-employee-by-id")
    public ResponseEntity<?> getEmployeeById(@RequestParam UUID id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("update-employee-by-id")
    public ResponseEntity<?> updateEmployeeById(@RequestParam UUID id, @Valid @RequestBody EmployeeRequest request, @Valid @RequestBody AppUser appUser) {
        return ResponseEntity.ok(employeeService.updateEmployeeById(id, request, appUser));
    }

    @DeleteMapping("delete-employee-by-id")
    public ResponseEntity<?> deleteEmployeeById(@RequestParam UUID id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }

    @GetMapping("/get-all-inactive-employee")
    public ResponseEntity<?> getInactiveEmployee() {
        return ResponseEntity.ok(employeeService.getInactiveEmployee());
    }

    @GetMapping("/get-all-available-employee")
    public ResponseEntity<?> getAvailableEmployee() {
        return ResponseEntity.ok(employeeService.getAvailableEmployee());
    }

    @PostMapping("search-by-name-or-mobile")
    public ResponseEntity<?> searchEmployee(@RequestParam(required = false) String name, @RequestParam(required = false) String number) {
        return ResponseEntity.ok(employeeService.searchEmployee(name, name));
    }

}
