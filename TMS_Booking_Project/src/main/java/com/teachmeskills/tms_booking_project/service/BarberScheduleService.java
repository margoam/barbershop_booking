package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.model.BarberSchedule;
import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.model.dto.AvailableSlotsResponse;
import com.teachmeskills.tms_booking_project.repository.BarberRepository;
import com.teachmeskills.tms_booking_project.repository.BarberScheduleRepository;
import com.teachmeskills.tms_booking_project.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberScheduleService {

    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberRepository barberRepository;
    private final ServiceRepository serviceRepository;

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

    public boolean delete(Long id) {
        barberScheduleRepository.deleteById(id);
        return true;
    }

    public List<AvailableSlotsResponse> getAvailableSlots(Long barberId, Long serviceId) {
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));

        BarberSrv service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        int requiredMinutes = service.getDurationMinutes();
        LocalDateTime now = LocalDateTime.now();

        return barberScheduleRepository
                .findByBarberAndAvailableTrueAndBookedFalseAndStartTimeAfter(barber, now)
                .stream()
                .filter(s -> s.getEndTime().isAfter(s.getStartTime().plusMinutes(requiredMinutes)))
                .map(s -> new AvailableSlotsResponse(
                        s.getId(),
                        s.getStartTime(),
                        s.getEndTime()
                ))
                .toList();
    }
}

