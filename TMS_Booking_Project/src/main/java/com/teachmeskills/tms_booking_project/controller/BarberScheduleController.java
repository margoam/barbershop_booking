package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.model.dto.BarberScheduleUpdateRequest;
import com.teachmeskills.tms_booking_project.service.BarberScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule Management", description = "API for schedule operations")
class BarberScheduleController {

    private final BarberScheduleService scheduleService;

    @Operation(summary = "Get all schedules")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/all")
    public ResponseEntity<List<BarberSchedule>> getAllSchedules() {
        List<BarberSchedule> scheduleList = scheduleService.getAll();
        if (scheduleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @Operation(summary = "Get schedule by ID")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<BarberSchedule> getScheduleById(@PathVariable @Parameter(description = "Schedule id") Long id) {
        BarberSchedule schedule = scheduleService.getById(id);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @Operation(summary = "Create new schedule")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/create")
    public ResponseEntity<BarberSchedule> createSchedule(@RequestBody @Valid BarberSchedule schedule) {
        return new ResponseEntity<>(scheduleService.create(schedule), HttpStatus.CREATED);
    }

    @Operation(summary = "Update schedule")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    public ResponseEntity<BarberSchedule> updateSchedule(
            @Parameter(description = "ID of schedule to be updated")
            @PathVariable Long id,
            @RequestBody @Valid BarberScheduleUpdateRequest request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    @Operation(summary = "Delete schedule")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSchedule(
            @Parameter(description = "ID of schedule to be deleted")
            @PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get available slots")
    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotsResponse>> getAvailableSlots(
            @Parameter(description = "Barber ID")
            @RequestParam Long barberId,
            @Parameter(description = "Service ID")
            @RequestParam Long serviceId) {
        return ResponseEntity.ok(scheduleService.getAvailableSlots(barberId, serviceId));
    }
}
