package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "barber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column(precision = 5)
    private BigDecimal rating;

}

