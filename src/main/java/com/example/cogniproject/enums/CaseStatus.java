package com.example.cogniproject.enums;

/**
 * Represents the lifecycle states of a disease case.
 * Valid transitions: OPEN → UNDER_TREATMENT → (RECOVERED | DECEASED | CLOSED)
 */
public enum CaseStatus {
    OPEN,
    UNDER_TREATMENT,
    RECOVERED,
    DECEASED,
    CLOSED
}
