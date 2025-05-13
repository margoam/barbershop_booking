package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.AuthRequest;
import com.teachmeskills.tms_booking_project.model.dto.AuthResponse;
import com.teachmeskills.tms_booking_project.repository.UserRepository;
import com.teachmeskills.tms_booking_project.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse authenticate(AuthRequest request) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

            if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
                logger.info("Authentication failed for user {}", userDetails.getUsername());
                throw new AuthenticationServiceException("Invalid email or password");
            }

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user)
                    .orElseThrow(() -> new JwtException("Token generation failed"));

            logger.info("Authentication successful for user {}", user.getId());
            return new AuthResponse(token);

        } catch (AuthenticationServiceException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }
}
