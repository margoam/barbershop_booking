package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.service.BarberScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
class BarberScheduleController {

    private final BarberScheduleService scheduleService;

    @GetMapping
    public List<BarberSchedule> getAllSchedules() {
        return scheduleService.getAll();
    }

    @PostMapping("/create")
    public BarberSchedule createSchedule(@RequestBody @Valid BarberSchedule schedule) {
        return scheduleService.create(schedule);
    }

    @PutMapping("/{id}")
    public BarberSchedule updateSchedule(@PathVariable Long id, @RequestBody BarberSchedule schedule) {
        return scheduleService.update(id, schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam Long barberId,
            @RequestParam Long serviceId
    ) {
        List<AvailableSlotsResponse> slots = scheduleService.getAvailableSlots(barberId, serviceId);

        if (slots.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "No available slots found for the selected barber and service.")
            );
        }
        return ResponseEntity.ok(slots);
    }
}
