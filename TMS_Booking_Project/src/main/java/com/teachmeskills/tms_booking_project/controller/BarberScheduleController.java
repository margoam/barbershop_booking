package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.model.dto.BarberScheduleUpdateRequest;
import com.teachmeskills.tms_booking_project.service.BarberScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule Management", description = "API for schedule operations")
class BarberScheduleController {

    private final BarberScheduleService scheduleService;

    @Operation(summary = "Get all schedules")
    @GetMapping("/all")
    public ResponseEntity<List<BarberSchedule>> getAllSchedules() {
        List<BarberSchedule> scheduleList = scheduleService.getAll();
        if (scheduleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @Operation(summary = "Get schedule by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BarberSchedule> getScheduleById(@PathVariable @Parameter(description = "Schedule id") Long id) {
        Optional<BarberSchedule> schedule = Optional.ofNullable(scheduleService.getById(id));
        if (schedule.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedule.get(), HttpStatus.OK);
    }

    @Operation(summary = "Create new schedule")
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody @Valid BarberSchedule schedule) {
        try {
            return new ResponseEntity<>(scheduleService.create(schedule), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update schedule")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(
            @Parameter(description = "ID of schedule to be updated")
            @PathVariable Long id,
            @RequestBody @Valid BarberScheduleUpdateRequest request) {
        try {
            return ResponseEntity.ok(scheduleService.update(id, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete schedule")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSchedule(
            @Parameter(description = "ID of schedule to be deleted")
            @PathVariable Long id) {
        try {
            scheduleService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get available slots")
    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotsResponse>> getAvailableSlots(
            @Parameter(description = "Barber ID")
            @RequestParam Long barberId,
            @Parameter(description = "Service ID")
            @RequestParam Long serviceId) {
        try {
            return ResponseEntity.ok(scheduleService.getAvailableSlots(barberId, serviceId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
