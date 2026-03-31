package com.example.cogniproject.entity;

import com.example.cogniproject.enums.CaseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Core entity representing a disease case reported by a doctor for a citizen.
 */
@Entity
@Table(name = "disease_cases", indexes = {
        @Index(name = "idx_citizen_id", columnList = "citizen_id"),
        @Index(name = "idx_doctor_id", columnList = "doctor_id"),
        @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class DiseaseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id", nullable = false)
    private Long citizenId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @NotBlank
    @Column(name = "disease_type", nullable = false, length = 255)
    private String diseaseType;

    @NotNull
    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CaseStatus status = CaseStatus.OPEN;

    @Column(name = "initial_notes", columnDefinition = "TEXT")
    private String initialNotes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "diseaseCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseUpdate> updates = new ArrayList<>();

    public DiseaseCase() {}

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

    public List<CaseUpdate> getUpdates() { return updates; }
    public void setUpdates(List<CaseUpdate> updates) { this.updates = updates; }
}
