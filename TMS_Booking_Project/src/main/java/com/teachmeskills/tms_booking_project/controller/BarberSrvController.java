package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.service.BarberServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
class BarberSrvController {

    private final BarberServiceService serviceService;

    @GetMapping
    public List<BarberSrv> getAllServices() {
        return serviceService.getAll();
    }

    @PostMapping
    public BarberSrv createService(@RequestBody @Valid BarberSrv service) {
        return serviceService.create(service);
    }

    @PutMapping("/{id}")
    public BarberSrv updateService(@PathVariable Long id, @RequestBody BarberSrv service) {
        return serviceService.update(id, service);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
