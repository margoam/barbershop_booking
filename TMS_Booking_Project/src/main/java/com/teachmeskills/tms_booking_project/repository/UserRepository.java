package com.teachmeskills.tms_booking_project.repository;

import com.teachmeskills.tms_booking_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

