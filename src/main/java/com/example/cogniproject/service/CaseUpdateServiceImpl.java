package com.example.cogniproject.service;

import com.example.cogniproject.dto.CaseUpdateRequest;
import com.example.cogniproject.dto.CaseUpdateResponse;
import com.example.cogniproject.entity.CaseUpdate;
import com.example.cogniproject.entity.DiseaseCase;
import com.example.cogniproject.entity.User;
import com.example.cogniproject.enums.UserRole;
import com.example.cogniproject.exception.CaseNotFoundException;
import com.example.cogniproject.exception.UnauthorizedException;
import com.example.cogniproject.repository.CaseUpdateRepository;
import com.example.cogniproject.repository.DieaseCaseRepository;
import com.example.cogniproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of CaseUpdateService for managing clinical update notes on disease cases.
 */
@Service
@Transactional
public class CaseUpdateServiceImpl implements CaseUpdateService {

    private final CaseUpdateRepository updateRepository;
    private final DieaseCaseRepository caseRepository;
    private final UserRepository userRepository;

    public CaseUpdateServiceImpl(CaseUpdateRepository updateRepository,
                                 DieaseCaseRepository caseRepository,
                                 UserRepository userRepository) {
        this.updateRepository = updateRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CaseUpdateResponse addUpdate(Long caseId, CaseUpdateRequest request, String username) {
        User user = getUser(username);
        if (user.getRole() != UserRole.DOCTOR) {
            throw new UnauthorizedException("Only doctors can add case updates");
        }

        DiseaseCase diseaseCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFoundException(caseId));

        // A doctor may only update cases assigned to them
        if (!diseaseCase.getDoctorId().equals(user.getId())) {
            throw new UnauthorizedException("You are not the assigned doctor for this case");
        }

        CaseUpdate update = new CaseUpdate();
        update.setDiseaseCase(diseaseCase);
        update.setStatus(request.getStatus());
        update.setNotes(request.getNotes());
        update.setUpdateDate(LocalDateTime.now());

        return toResponse(updateRepository.save(update));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseUpdateResponse> getUpdates(Long caseId, String username) {
        User user = getUser(username);
        DiseaseCase diseaseCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFoundException(caseId));

        authorizeRead(diseaseCase, user);

        return updateRepository.findByDiseaseCaseIdOrderByUpdateDateAsc(caseId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseUpdateResponse> getTimeline(Long caseId, String username) {
        // Timeline is the same as the ordered updates list for this implementation
        return getUpdates(caseId, username);
    }

    // ---- helpers ----

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private void authorizeRead(DiseaseCase diseaseCase, User user) {
        switch (user.getRole()) {
            case ADMIN -> { /* unrestricted */ }
            case DOCTOR -> {
                if (!diseaseCase.getDoctorId().equals(user.getId())) {
                    throw new UnauthorizedException("You are not the assigned doctor for this case");
                }
            }
            case CITIZEN -> throw new UnauthorizedException("Citizens cannot view case updates");
        }
    }

    /** Maps a CaseUpdate entity to its response DTO. */
    private CaseUpdateResponse toResponse(CaseUpdate cu) {
        CaseUpdateResponse resp = new CaseUpdateResponse();
        resp.setId(cu.getId());
        resp.setCaseId(cu.getDiseaseCase().getId());
        resp.setStatus(cu.getStatus());
        resp.setNotes(cu.getNotes());
        resp.setUpdateDate(cu.getUpdateDate());
        resp.setCreatedAt(cu.getCreatedAt());
        return resp;
    }
}
