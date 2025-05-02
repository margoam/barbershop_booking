package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import com.teachmeskills.tms_booking_project.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberServiceService {

    private final ServiceRepository serviceRepository;

    public List<BarberSrv> getAll() {
        return serviceRepository.findAll();
    }

    public BarberSrv getById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
    }

    public BarberSrv create(BarberSrv service) {
        return serviceRepository.save(service);
    }

    public BarberSrv update(Long id, BarberSrv updatedService) {
        BarberSrv existing = getById(id);
        existing.setName(updatedService.getName());
        existing.setDescription(updatedService.getDescription());
        existing.setPrice(updatedService.getPrice());
        existing.setActive(updatedService.isActive());
        return serviceRepository.save(existing);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }
}

