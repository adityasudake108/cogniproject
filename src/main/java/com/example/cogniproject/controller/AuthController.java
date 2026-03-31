package com.example.cogniproject.controller;

import com.example.cogniproject.dto.LoginRequest;
import com.example.cogniproject.dto.LoginResponse;
import com.example.cogniproject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user authentication (login) requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login and obtain a JWT token")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and returns a JWT Bearer token.
     *
     * @param loginRequest username and password
     * @return JWT token with metadata
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username/password and receive a JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
