package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Barber;
import com.teachmeskills.tms_booking_project.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    public List<Barber> getAll() {
        return barberRepository.findAll();
    }

    public Barber getById(Long id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));
    }

    public Barber create(Barber barber) {
        return barberRepository.save(barber);
    }

    public Barber update(Long id, Barber updatedBarber) {
        Barber existing = getById(id);
        existing.setFullName(updatedBarber.getFullName());
        existing.setEmail(updatedBarber.getEmail());
        existing.setPhone(updatedBarber.getPhone());
        existing.setRating(updatedBarber.getRating());
        return barberRepository.save(existing);
    }

    public void delete(Long id) {
        barberRepository.deleteById(id);
    }
}
