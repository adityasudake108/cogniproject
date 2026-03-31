package com.example.cogniproject.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for adding a clinical note/update to an existing disease case.
 */
public class CaseUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Notes are required")
    private String notes;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
