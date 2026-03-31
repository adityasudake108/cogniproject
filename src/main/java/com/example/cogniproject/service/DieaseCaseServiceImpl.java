package com.example.cogniproject.service;

import com.example.cogniproject.dto.CaseResponse;
import com.example.cogniproject.dto.CreateCaseRequest;
import com.example.cogniproject.dto.UpdateCaseRequest;
import com.example.cogniproject.entity.DiseaseCase;
import com.example.cogniproject.entity.User;
import com.example.cogniproject.enums.UserRole;
import com.example.cogniproject.exception.CaseNotFoundException;
import com.example.cogniproject.exception.UnauthorizedException;
import com.example.cogniproject.repository.DieaseCaseRepository;
import com.example.cogniproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of DieaseCaseService with role-based data filtering.
 */
@Service
@Transactional
public class DieaseCaseServiceImpl implements DieaseCaseService {

    private final DieaseCaseRepository caseRepository;
    private final UserRepository userRepository;

    public DieaseCaseServiceImpl(DieaseCaseRepository caseRepository, UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CaseResponse createCase(CreateCaseRequest request, String username) {
        User doctor = getUser(username);
        if (doctor.getRole() != UserRole.DOCTOR) {
            throw new UnauthorizedException("Only doctors can create disease cases");
        }

        DiseaseCase diseaseCase = new DiseaseCase();
        diseaseCase.setDoctorId(doctor.getId());
        diseaseCase.setCitizenId(request.getCitizenId());
        diseaseCase.setDiseaseType(request.getDiseaseType());
        diseaseCase.setDiagnosisDate(request.getDiagnosisDate());
        diseaseCase.setInitialNotes(request.getInitialNotes());

        DiseaseCase saved = caseRepository.save(diseaseCase);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseResponse> getCases(String username) {
        User user = getUser(username);
        List<DiseaseCase> cases = switch (user.getRole()) {
            case ADMIN  -> caseRepository.findAll();
            case DOCTOR -> caseRepository.findByDoctorId(user.getId());
            case CITIZEN -> caseRepository.findByCitizenId(user.getId());
        };
        return cases.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CaseResponse getCaseById(Long caseId, String username) {
        DiseaseCase diseaseCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFoundException(caseId));

        User user = getUser(username);
        authorizeAccess(diseaseCase, user);
        return toResponse(diseaseCase);
    }

    @Override
    public CaseResponse updateCase(Long caseId, UpdateCaseRequest request, String username) {
        DiseaseCase diseaseCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFoundException(caseId));

        User user = getUser(username);
        // Only admins and the assigned doctor can update
        if (user.getRole() == UserRole.CITIZEN) {
            throw new UnauthorizedException("Citizens cannot update disease cases");
        }
        if (user.getRole() == UserRole.DOCTOR && !diseaseCase.getDoctorId().equals(user.getId())) {
            throw new UnauthorizedException("You are not the assigned doctor for this case");
        }

        diseaseCase.setStatus(request.getStatus());
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            diseaseCase.setInitialNotes(request.getNotes());
        }

        return toResponse(caseRepository.save(diseaseCase));
    }

    // ---- helpers ----

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Enforces role-based read access:
     * - ADMIN: always allowed
     * - DOCTOR: only their assigned cases
     * - CITIZEN: only cases where they are the patient
     */
    private void authorizeAccess(DiseaseCase diseaseCase, User user) {
        switch (user.getRole()) {
            case ADMIN -> { /* unrestricted */ }
            case DOCTOR -> {
                if (!diseaseCase.getDoctorId().equals(user.getId())) {
                    throw new UnauthorizedException("You are not the assigned doctor for this case");
                }
            }
            case CITIZEN -> {
                if (!diseaseCase.getCitizenId().equals(user.getId())) {
                    throw new UnauthorizedException("You can only view your own cases");
                }
            }
        }
    }

    /** Maps a DiseaseCase entity to its response DTO. */
    private CaseResponse toResponse(DiseaseCase dc) {
        CaseResponse resp = new CaseResponse();
        resp.setId(dc.getId());
        resp.setCitizenId(dc.getCitizenId());
        resp.setDoctorId(dc.getDoctorId());
        resp.setDiseaseType(dc.getDiseaseType());
        resp.setDiagnosisDate(dc.getDiagnosisDate());
        resp.setStatus(dc.getStatus());
        resp.setInitialNotes(dc.getInitialNotes());
        resp.setCreatedAt(dc.getCreatedAt());
        resp.setUpdatedAt(dc.getUpdatedAt());
        return resp;
    }
}
