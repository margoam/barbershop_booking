package com.teachmeskills.tms_booking_project.service;

import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.AuthRequest;
import com.teachmeskills.tms_booking_project.model.dto.AuthResponse;
import com.teachmeskills.tms_booking_project.repository.UserRepository;
import com.teachmeskills.tms_booking_project.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));


            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            String token = jwtUtil.generateToken(user)
                    .orElseThrow(() -> new RuntimeException("Token generation failed"));

            return new AuthResponse(token);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid email or password", e);
        }
    }
}