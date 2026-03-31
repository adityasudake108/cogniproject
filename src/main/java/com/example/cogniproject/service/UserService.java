package com.example.cogniproject.service;

import com.example.cogniproject.entity.User;
import com.example.cogniproject.enums.UserRole;

import java.util.Optional;

/**
 * Service interface for user management operations.
 */
public interface UserService {

    User createUser(String username, String rawPassword, String email, UserRole role);

    Optional<User> findByUsername(String username);

    User getUserByUsername(String username);
}
