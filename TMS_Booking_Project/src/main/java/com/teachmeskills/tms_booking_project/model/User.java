package com.teachmeskills.tms_booking_project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"user\"")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "is_subscribed", nullable = false)
    private boolean isSubscribed;
}

