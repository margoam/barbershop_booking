package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.dto.AuthRequest;
import com.teachmeskills.tms_booking_project.model.dto.AuthResponse;
import com.teachmeskills.tms_booking_project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest) {
        Optional<AuthResponse> token = Optional.ofNullable(authService.authenticate(authRequest));
        if (token.isPresent()) {
            return new ResponseEntity<>(token.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}