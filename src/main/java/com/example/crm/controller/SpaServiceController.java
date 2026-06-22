package com.example.crm.controller;

import com.example.crm.entity.dtos.spaservice.SpaServiceRequest;
import com.example.crm.service.SpaServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spaservice")
public class SpaServiceController {

    private final SpaServiceService spaServiceService;


    @PostMapping("/create-spa-service")
    public ResponseEntity<?> createSpaService(@Valid @RequestBody SpaServiceRequest spaServiceRequest) {

        return ResponseEntity.ok(spaServiceService.createService(spaServiceRequest));
    }

    @GetMapping("/get-all-spa-services")
    public ResponseEntity<?> getAllSpaService() {

        return ResponseEntity.ok(spaServiceService.getAllServices());
    }

    @GetMapping("/get-spa-service-by-id")
    public ResponseEntity<?> getSpaServiceById(@RequestParam UUID id) {

        return ResponseEntity.ok(spaServiceService.getServiceById(id));

    }

    @PutMapping("/update-spa-service-by-id")
    public ResponseEntity<?> updateSpaServiceById(@RequestParam UUID id, @Valid @RequestBody SpaServiceRequest spaServiceRequest) {

        return ResponseEntity.ok(spaServiceService.updateServiceById(id, spaServiceRequest));

    }

    @DeleteMapping("/delete-spa-service-by-id")
    public ResponseEntity<?> deleteSpaServiceById(@RequestParam UUID id) {

        return ResponseEntity.ok(spaServiceService.deleteServiceById(id));
    }

    @GetMapping("/get-active-spa-services")
    public ResponseEntity<?> getAllActiveSpaService() {

        return ResponseEntity.ok(spaServiceService.getActiveServices());

    }
}
