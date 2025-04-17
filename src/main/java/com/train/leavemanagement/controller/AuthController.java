package com.train.leavemanagement.controller;

import com.train.leavemanagement.dto.LoginRequestDTO;
import com.train.leavemanagement.dto.RegisterRequestDTO;
import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.entity.RoleType;
import com.train.leavemanagement.repository.UserRepository;
import com.train.leavemanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Endpoints for user registration and login")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided email, name, password, and phone number. Sends an OTP to the provided email for verification."
    )
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        try {
            authService.register(registerRequestDTO);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login with email and password",
            description = "Authenticates the user using their email and password. Returns a JWT token if successful."
    )
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try{
            String token = authService.login(loginRequestDTO);
            return ResponseEntity.ok(token);
        }catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/verify")
    @Operation(
            summary = "Verify users' account by admin",
            description = "Only admin can verify users' account"
    )
    public ResponseEntity<?> logout(@RequestParam Long userId, @RequestParam boolean verifyStatus) {
        try{
            authService.verifyByAdmin(userId, verifyStatus);
            return ResponseEntity.ok().build();
        }
        catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
