package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.AuthRequest;
import com.teachmeskills.tms_booking_project.model.dto.AuthResponse;
import com.teachmeskills.tms_booking_project.repository.UserRepository;
import com.teachmeskills.tms_booking_project.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new JwtException("Invalid password");
            }

            String token = jwtUtil.generateToken(user)
                    .orElseThrow(() -> new JwtException("Token generation failed"));

            return new AuthResponse(token);
        } catch (JwtException e) {
            throw new JwtException("Invalid email or password");
        } catch (Exception e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }
}