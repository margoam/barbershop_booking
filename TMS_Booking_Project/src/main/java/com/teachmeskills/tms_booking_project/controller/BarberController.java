package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.service.BarberService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
class BarberController {

    private final BarberService barberService;

    @Autowired
    BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Barber>> getAllBarbers() {
        List<Barber> barberList = barberService.getAll();
        if (barberList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(barberList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(@PathVariable @Parameter(description = "Barber id") Long id) {
        Barber user = barberService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Barber> createBarber(@RequestBody @Valid Barber barber) {
        Barber result = barberService.create(barber);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(@PathVariable Long id, @RequestBody @Valid Barber barber) {
        Barber result = barberService.update(id, barber);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBarber(@PathVariable Long id) {
        barberService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
