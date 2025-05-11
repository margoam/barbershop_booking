package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.service.BarberServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Validated
@Tag(name = "Service Management", description = "API for barber service operations")
public class BarberSrvController {

    private final BarberServiceService serviceService;

    @Operation(summary = "Get all services",
            description = "Retrieves a list of all available barber services")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @ApiResponse(responseCode = "404", description = "No services found")
    @GetMapping("/all")
    public ResponseEntity<List<BarberSrv>> getAllServices() {
        List<BarberSrv> services = serviceService.getAll();
        return services.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(services);
    }

    @Operation(summary = "Create new service",
            description = "Creates a new barber service (admin only)")
    @ApiResponse(responseCode = "201", description = "Service created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping("/create")
    public ResponseEntity<BarberSrv> createService(
            @Parameter(description = "Service object to be created", required = true)
            @RequestBody @Valid BarberSrv service) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceService.create(service));
    }

    @Operation(summary = "Update service",
            description = "Updates an existing barber service")
    @ApiResponse(responseCode = "200", description = "Service updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @PutMapping("/{id}")
    public ResponseEntity<BarberSrv> updateService(
            @Parameter(description = "ID of the service to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated service data", required = true)
            @RequestBody @Valid BarberSrv service) {
        return ResponseEntity.ok(serviceService.update(id, service));
    }

    @Operation(summary = "Delete service",
            description = "Deletes a barber service (admin only)")
    @ApiResponse(responseCode = "204", description = "Service deleted successfully")
    @ApiResponse(responseCode = "404", description = "Service not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "ID of the service to delete", required = true)
            @PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}