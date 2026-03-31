package com.example.cogniproject.exception;

/**
 * Thrown when a user attempts to access a resource they are not permitted to view.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
