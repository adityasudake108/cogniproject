package com.example.cogniproject.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a case update/note returned to the client.
 */
public class CaseUpdateResponse {

    private Long id;
    private Long caseId;
    private String status;
    private String notes;
    private LocalDateTime updateDate;
    private LocalDateTime createdAt;

    public CaseUpdateResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCaseId() { return caseId; }
    public void setCaseId(Long caseId) { this.caseId = caseId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
