package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
class BarberController {

    private final BarberService barberService;

    @GetMapping
    public List<Barber> getAllBarbers() {
        return barberService.getAll();
    }

    @PostMapping("/create")
    public Barber createBarber(@RequestBody @Valid Barber barber) {
        return barberService.create(barber);
    }

    @PutMapping("/{id}")
    public Barber updateBarber(@PathVariable Long id, @RequestBody Barber barber) {
        return barberService.update(id, barber);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Long id) {
        barberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
