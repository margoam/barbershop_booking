package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.service.BarberServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
class BarberSrvController {

    private final BarberServiceService serviceService;

    @GetMapping("/all")
    public ResponseEntity<List<BarberSrv>> getAllServices() {
        List<BarberSrv> services = serviceService.getAll();
        if (services.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BarberSrv> createService(@RequestBody @Valid BarberSrv service) {
        BarberSrv barberSrv = serviceService.create(service);
        return new ResponseEntity<>(barberSrv, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberSrv> updateService(@PathVariable Long id, @RequestBody @Valid BarberSrv service) {
        BarberSrv barberSrv = serviceService.update(id, service);
        return new ResponseEntity<>(barberSrv, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
