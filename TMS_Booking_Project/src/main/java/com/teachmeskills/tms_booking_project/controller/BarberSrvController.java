package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.service.BarberServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<BarberSrv> barberSrv = Optional.ofNullable(serviceService.create(service));
        if (barberSrv.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(barberSrv.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberSrv> updateService(@PathVariable Long id, @RequestBody BarberSrv service) {
        Optional<BarberSrv> barberSrv = Optional.ofNullable(serviceService.update(id, service));
        if (barberSrv.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(barberSrv.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Long id) {
        boolean result = serviceService.delete(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
