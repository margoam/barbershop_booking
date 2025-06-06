package com.teachmeskills.tms_booking_project.controller;

import com.teachmeskills.tms_booking_project.model.User;
import com.teachmeskills.tms_booking_project.model.dto.UserCreateRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserRegistrationRequest;
import com.teachmeskills.tms_booking_project.model.dto.UserResponse;
import com.teachmeskills.tms_booking_project.model.dto.UserUpdateRequest;
import com.teachmeskills.tms_booking_project.security.SecurityUtils;
import com.teachmeskills.tms_booking_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Management", description = "API for user operations")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Get all users")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUsers();
        if (userResponses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "404", description = "User not found")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();

        if (!securityUtils.isCurrentUserAdmin() && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID of the user to update")
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        if (!securityUtils.isCurrentUserAdmin() && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Delete user")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@Parameter(description = "ID of the user to delete")
                                                 @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create user (admin only)")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Register new user")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        UserResponse user = userService.register(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
