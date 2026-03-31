package com.example.cogniproject.exception;

/**
 * Thrown when a JWT token is missing, malformed, expired, or otherwise invalid.
 */
public class InvalidJwtException extends RuntimeException {

    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
