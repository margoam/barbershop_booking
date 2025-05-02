package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Role;
import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.UserCreateRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserRegistrationRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserResponse;
import com.teachmeskills.tms_booking_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER.name())
                .registeredAt(LocalDateTime.now())
                .isSubscribed(false)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role().toUpperCase())
                .isSubscribed(request.isSubscribed())
                .registeredAt(LocalDateTime.now())
                .build();

        return mapToResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setSubscribed(request.isSubscribed());
        user.setRole(request.role().toUpperCase());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return mapToResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isSubscribed()
        );
    }
}
