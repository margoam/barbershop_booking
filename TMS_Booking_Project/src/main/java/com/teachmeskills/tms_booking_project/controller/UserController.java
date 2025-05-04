package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.dto.UserCreateRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserRegistrationRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserResponse;
import com.teachmeskills.tms_booking_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        if (userResponses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<UserResponse> userResponse = Optional.ofNullable(userService.getUserById(id));
        if (userResponse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponse.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid UserCreateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        Optional<UserResponse> user = Optional.ofNullable(userService.createUser(request));
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        Optional<UserResponse> user = Optional.ofNullable(userService.register(request));
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.CREATED);
    }
}
