package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.Role;
import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.UserCreateRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserRegistrationRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserResponse;
import com.teachmeskills.tms_booking_project.model.dto.UserUpdateRequest;
import com.teachmeskills.tms_booking_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .registeredAt(LocalDateTime.now())
                .isSubscribed(false)
                .build();
        logger.info("Registered user with id: {}", user.getId());
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
                .role(request.role())
                .isSubscribed(request.isSubscribed())
                .registeredAt(LocalDateTime.now())
                .build();
        logger.info("Created user with id: {}", user.getId());
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
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean hasChanged = false;

        if (!Objects.equals(request.fullName(), user.getFullName()) && request.fullName() != null) {
            user.setFullName(request.fullName());
            hasChanged = true;
        }

        if (!Objects.equals(request.email(), user.getEmail()) && request.email() != null) {
            if (userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.email());
            hasChanged = true;
        }

        if (request.isSubscribed() != null && request.isSubscribed() != user.isSubscribed()) {
            user.setSubscribed(request.isSubscribed());
            hasChanged = true;
        }

        if (request.role() != user.getRole() && request.role() != null) {
            user.setRole(request.role());
            hasChanged = true;
        }

        if (request.password() != null && !request.password().isBlank()) {
            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(request.password()));
                hasChanged = true;
            }
        }

        if (!hasChanged) {
            logger.info("No changes detected for user {}", id);
            throw new IllegalArgumentException("No changes detected");
        }

        logger.info("Updated user {} - changes applied", id);
        return mapToResponse(userRepository.save(user));
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Removed user with id: {}", id);
            return true;
        } else {
            return false;
        }
    }

    public UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isSubscribed()
        );
    }
}
