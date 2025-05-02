package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.dto.UserCreateRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserRegistrationRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserResponse;
import com.teachmeskills.tms_booking_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid UserCreateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin")
    public UserResponse createUser(@RequestBody @Valid UserCreateRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/create")
    public UserResponse registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        return userService.register(request);
    }
}
