package com.example.cogniproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * Request body for creating a new disease case.
 */
public class CreateCaseRequest {

    @NotNull(message = "Citizen ID is required")
    private Long citizenId;

    @NotBlank(message = "Disease type is required")
    private String diseaseType;

    @NotNull(message = "Diagnosis date is required")
    @PastOrPresent(message = "Diagnosis date cannot be in the future")
    private LocalDate diagnosisDate;

    private String initialNotes;

    public Long getCitizenId() { return citizenId; }
    public void setCitizenId(Long citizenId) { this.citizenId = citizenId; }

    public String getDiseaseType() { return diseaseType; }
    public void setDiseaseType(String diseaseType) { this.diseaseType = diseaseType; }

    public LocalDate getDiagnosisDate() { return diagnosisDate; }
    public void setDiagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; }

    public String getInitialNotes() { return initialNotes; }
    public void setInitialNotes(String initialNotes) { this.initialNotes = initialNotes; }
}
