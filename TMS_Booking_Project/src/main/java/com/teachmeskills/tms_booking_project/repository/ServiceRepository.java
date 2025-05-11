package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.BarberSrv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<BarberSrv, Long> {
    List<BarberSrv> findByActiveTrue();
}
