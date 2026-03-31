package com.example.cogniproject.exception;

/**
 * Thrown when a requested disease case does not exist.
 */
public class CaseNotFoundException extends RuntimeException {

    public CaseNotFoundException(Long caseId) {
        super("Disease case not found with id: " + caseId);
    }

    public CaseNotFoundException(String message) {
        super(message);
    }
}
