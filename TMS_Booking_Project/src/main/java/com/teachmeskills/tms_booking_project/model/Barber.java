package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "barber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(precision = 5, scale = 0)
    private BigDecimal rating;

    @OneToMany(mappedBy = "barber")
    private List<Booking> bookings;
}

