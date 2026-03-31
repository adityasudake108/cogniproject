package com.example.cogniproject.service;

import com.example.cogniproject.dto.CaseResponse;
import com.example.cogniproject.dto.CreateCaseRequest;
import com.example.cogniproject.dto.UpdateCaseRequest;

import java.util.List;

/**
 * Service interface for disease case management operations.
 */
public interface DieaseCaseService {

    /**
     * Creates a new disease case. The doctor ID is taken from the authenticated user.
     *
     * @param request  case creation payload
     * @param username the authenticated doctor's username
     * @return the created case as a response DTO
     */
    CaseResponse createCase(CreateCaseRequest request, String username);

    /**
     * Returns a list of cases visible to the given user based on their role.
     * Admins see all cases; Doctors see their own; Citizens see cases where they are the patient.
     *
     * @param username the authenticated user's username
     * @return list of case responses
     */
    List<CaseResponse> getCases(String username);

    /**
     * Returns the details of a single case, enforcing role-based access control.
     *
     * @param caseId   the case identifier
     * @param username the authenticated user's username
     * @return case response DTO
     */
    CaseResponse getCaseById(Long caseId, String username);

    /**
     * Updates the status (and optionally notes) of an existing case.
     *
     * @param caseId  the case identifier
     * @param request update payload
     * @param username the authenticated user's username
     * @return updated case response DTO
     */
    CaseResponse updateCase(Long caseId, UpdateCaseRequest request, String username);
}
