package com.example.crm.controller;

import com.example.crm.entity.dtos.servicepackage.ServicePackageRequest;
import com.example.crm.service.ServicePackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/servicepackage")
public class ServicePackageController {

    private final ServicePackageService servicePackageService;

    @PostMapping("/create-service-package")
    public ResponseEntity<?> createServicePackage(@Valid @RequestBody ServicePackageRequest servicePackageRequest) {

        return ResponseEntity.ok(servicePackageService.createServicePackage(servicePackageRequest));
    }

    @GetMapping("/get-all-service-packages")
    public ResponseEntity<?> getAllServicePackages() {

        return ResponseEntity.ok(servicePackageService.getAllServicePackages());
    }

    @GetMapping("/get-service-package-by-id")
    public ResponseEntity<?> getServicePackageById(@RequestParam UUID id) {

        return ResponseEntity.ok(servicePackageService.getServicePackageById(id));
    }

    @PutMapping("/update-service-package-by-id")
    public ResponseEntity<?> updateServicePackageById(@RequestParam UUID id, @Valid @RequestBody ServicePackageRequest servicePackageRequest) {

        return ResponseEntity.ok(servicePackageService.updateServicePackageById(id, servicePackageRequest));
    }

    @DeleteMapping("/delete-service-package-by-id")
    public ResponseEntity<?> deleteServicePackageById(@RequestParam UUID id) {

        return ResponseEntity.ok(servicePackageService.deleteServicePackageById(id));
    }

    @GetMapping("/get-active-service-packages")
    public ResponseEntity<?> getActiveServicePackages() {

        return ResponseEntity.ok(servicePackageService.getActivePackages());
    }
}