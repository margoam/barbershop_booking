package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.repository.BarberScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberScheduleService {

    private final BarberScheduleRepository barberScheduleRepository;

    public List<BarberSchedule> getAll() {
        return barberScheduleRepository.findAll();
    }

    public BarberSchedule getById(Long id) {
        return barberScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    }

    public BarberSchedule create(BarberSchedule schedule) {
        return barberScheduleRepository.save(schedule);
    }

    public BarberSchedule update(Long id, BarberSchedule updated) {
        BarberSchedule existing = getById(id);
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setAvailable(updated.isAvailable());
        existing.setBooked(updated.isBooked());
        return barberScheduleRepository.save(existing);
    }

    public void delete(Long id) {
        barberScheduleRepository.deleteById(id);
    }
}

