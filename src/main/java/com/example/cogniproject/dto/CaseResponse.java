package com.example.cogniproject.dto;

import com.example.cogniproject.enums.CaseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO representing a disease case returned to the client.
 */
public class CaseResponse {

    private Long id;
    private Long citizenId;
    private Long doctorId;
    private String diseaseType;
    private LocalDate diagnosisDate;
    private CaseStatus status;
    private String initialNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CaseResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCitizenId() { return citizenId; }
    public void setCitizenId(Long citizenId) { this.citizenId = citizenId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDiseaseType() { return diseaseType; }
    public void setDiseaseType(String diseaseType) { this.diseaseType = diseaseType; }

    public LocalDate getDiagnosisDate() { return diagnosisDate; }
    public void setDiagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; }

    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) { this.status = status; }

    public String getInitialNotes() { return initialNotes; }
    public void setInitialNotes(String initialNotes) { this.initialNotes = initialNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
