package com.example.cogniproject.service;

import com.example.cogniproject.dto.CaseUpdateRequest;
import com.example.cogniproject.dto.CaseUpdateResponse;

import java.util.List;

/**
 * Service interface for managing case update notes.
 */
public interface CaseUpdateService {

    /**
     * Adds a clinical note/status update to an existing disease case.
     *
     * @param caseId   the disease case identifier
     * @param request  the update payload
     * @param username the authenticated doctor's username
     * @return the created case update response DTO
     */
    CaseUpdateResponse addUpdate(Long caseId, CaseUpdateRequest request, String username);

    /**
     * Returns all updates for a given case, ordered chronologically.
     *
     * @param caseId   the disease case identifier
     * @param username the authenticated user's username
     * @return list of case update response DTOs
     */
    List<CaseUpdateResponse> getUpdates(Long caseId, String username);

    /**
     * Returns the full event timeline for a case (creation + all updates), ordered chronologically.
     *
     * @param caseId   the disease case identifier
     * @param username the authenticated user's username
     * @return list of case update response DTOs representing the timeline
     */
    List<CaseUpdateResponse> getTimeline(Long caseId, String username);
}
