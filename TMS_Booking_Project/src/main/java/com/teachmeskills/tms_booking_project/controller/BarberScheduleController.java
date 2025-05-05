package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.service.BarberScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
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
class BarberScheduleController {

    private final BarberScheduleService scheduleService;

    @GetMapping("/all")
    public ResponseEntity<List<BarberSchedule>> getAllSchedules() {
        List<BarberSchedule> scheduleList = scheduleService.getAll();
        if (scheduleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<BarberSchedule> getScheduleById(@PathVariable @Parameter(description = "Schedule id") Long id) {
        Optional<BarberSchedule> schedule = Optional.ofNullable(scheduleService.getById(id));
        if (schedule.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(schedule.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BarberSchedule> createSchedule(@RequestBody @Valid BarberSchedule schedule) {
        Optional<BarberSchedule> barberSchedule = Optional.ofNullable(scheduleService.create(schedule));
        if (barberSchedule.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(barberSchedule.get(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberSchedule> updateSchedule(@PathVariable Long id, @RequestBody @Valid BarberSchedule schedule) {
        Optional<BarberSchedule> barberSchedule = Optional.ofNullable(scheduleService.update(id, schedule));
        if (barberSchedule.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(barberSchedule.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSchedule(@PathVariable Long id) {
        boolean result = scheduleService.delete(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotsResponse>> getAvailableSlots(
            @RequestParam Long barberId,
            @RequestParam Long serviceId
    ) {
        List<AvailableSlotsResponse> slots = scheduleService.getAvailableSlots(barberId, serviceId);
        if (slots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(slots);
    }
}
