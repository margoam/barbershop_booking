package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.service.BarberService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
class BarberController {

    private final BarberService barberService;

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
        Optional<Barber> user = Optional.ofNullable(barberService.getById(id));
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Barber> createBarber(@RequestBody @Valid Barber barber) {
        Optional<Barber> result = Optional.ofNullable(barberService.create(barber));
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(@PathVariable Long id, @RequestBody @Valid Barber barber) {
        Optional<Barber> result = barberService.update(id, barber);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBarber(@PathVariable Long id) {
        boolean result = barberService.delete(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
