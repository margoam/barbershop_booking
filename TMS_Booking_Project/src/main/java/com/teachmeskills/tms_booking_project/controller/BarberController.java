package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.service.BarberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Barber Management", description = "API for barber operations")
public class BarberController {

    private final BarberService barberService;

    @Operation(summary = "Get all barbers",
            description = "Retrieves a list of all barbers with their details")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved barber list")
    @ApiResponse(responseCode = "404", description = "No barbers found")
    @GetMapping("/all")
    public ResponseEntity<List<Barber>> getAllBarbers() {
        List<Barber> barbers = barberService.getAll();
        return barbers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(barbers);
    }

    @Operation(summary = "Get barber by ID",
            description = "Retrieves a specific barber by their unique identifier")
    @ApiResponse(responseCode = "200", description = "Barber found and returned")
    @ApiResponse(responseCode = "404", description = "Barber not found")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<Barber> getBarberById(
            @Parameter(description = "ID of the barber to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(barberService.getById(id));
    }

    @Operation(summary = "Create new barber",
            description = "Creates a new barber record (admin only)")
    @ApiResponse(responseCode = "201", description = "Barber created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/create")
    public ResponseEntity<Barber> createBarber(
            @Parameter(description = "Barber object that needs to be created", required = true)
            @RequestBody @Valid Barber barber) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(barberService.create(barber));
    }

    @Operation(summary = "Update barber",
            description = "Updates an existing barber's information")
    @ApiResponse(responseCode = "200", description = "Barber updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Barber not found")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    public ResponseEntity<Barber> updateBarber(
            @Parameter(description = "ID of the barber to update", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated barber data", required = true)
            @RequestBody @Valid Barber barber) {
        return ResponseEntity.ok(barberService.update(id, barber));
    }

    @Operation(summary = "Delete barber",
            description = "Deletes a barber record (admin only)")
    @ApiResponse(responseCode = "204", description = "Barber deleted successfully")
    @ApiResponse(responseCode = "404", description = "Barber not found")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarber(
            @Parameter(description = "ID of the barber to delete", required = true, example = "1")
            @PathVariable Long id) {
        barberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}