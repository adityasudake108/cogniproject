package com.example.cogniproject.service;

import com.example.cogniproject.dto.LoginRequest;
import com.example.cogniproject.dto.LoginResponse;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    /**
     * Authenticates a user with the given credentials and returns a JWT token.
     *
     * @param loginRequest contains username and password
     * @return JWT token with metadata
     */
    LoginResponse login(LoginRequest loginRequest);
}
