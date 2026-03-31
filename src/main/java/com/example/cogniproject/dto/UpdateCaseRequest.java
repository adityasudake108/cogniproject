package com.example.cogniproject.dto;

import com.example.cogniproject.enums.CaseStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for updating the status of an existing disease case.
 */
public class UpdateCaseRequest {

    @NotNull(message = "Status is required")
    private CaseStatus status;

    private String notes;

    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
