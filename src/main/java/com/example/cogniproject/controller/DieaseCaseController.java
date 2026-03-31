package com.example.cogniproject.controller;

import com.example.cogniproject.dto.*;
import com.example.cogniproject.service.CaseUpdateService;
import com.example.cogniproject.service.DieaseCaseService;
import com.example.cogniproject.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing all 7 disease-case management endpoints.
 * All endpoints require a valid JWT Bearer token.
 */
@RestController
@RequestMapping("/api/v1/cases")
@Tag(name = "Disease Cases", description = "Disease case reporting and tracking")
@SecurityRequirement(name = "bearerAuth")
public class DieaseCaseController {

    private final DieaseCaseService caseService;
    private final CaseUpdateService caseUpdateService;

    public DieaseCaseController(DieaseCaseService caseService, CaseUpdateService caseUpdateService) {
        this.caseService = caseService;
        this.caseUpdateService = caseUpdateService;
    }

    // ---- 1. Create disease case (DOCTOR only) ----

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Create disease case", description = "DOCTOR only: report a new disease case for a citizen")
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CreateCaseRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        CaseResponse response = caseService.createCase(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---- 2. List cases (role-filtered) ----

    @GetMapping
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "List cases", description = "ADMIN sees all cases; DOCTOR sees their own cases")
    public ResponseEntity<List<CaseResponse>> getCases() {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(caseService.getCases(username));
    }

    // ---- 3. Get single case ----

    @GetMapping("/{caseId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('CITIZEN')")
    @Operation(summary = "Get case by ID", description = "ADMIN: any case; DOCTOR: assigned cases; CITIZEN: own cases")
    public ResponseEntity<CaseResponse> getCaseById(@PathVariable Long caseId) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(caseService.getCaseById(caseId, username));
    }

    // ---- 4. Update case status ----

    @PatchMapping("/{caseId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Update case status", description = "DOCTOR (assigned) or ADMIN: update the case status")
    public ResponseEntity<CaseResponse> updateCase(@PathVariable Long caseId,
                                                   @Valid @RequestBody UpdateCaseRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(caseService.updateCase(caseId, request, username));
    }

    // ---- 5. Add case update/note ----

    @PostMapping("/{caseId}/updates")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Add case update", description = "DOCTOR (assigned): add a clinical note/update to the case")
    public ResponseEntity<CaseUpdateResponse> addUpdate(@PathVariable Long caseId,
                                                        @Valid @RequestBody CaseUpdateRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        CaseUpdateResponse response = caseUpdateService.addUpdate(caseId, request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---- 6. List case updates ----

    @GetMapping("/{caseId}/updates")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "List case updates", description = "DOCTOR or ADMIN: list all updates for a case")
    public ResponseEntity<List<CaseUpdateResponse>> getUpdates(@PathVariable Long caseId) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(caseUpdateService.getUpdates(caseId, username));
    }

    // ---- 7. Get case timeline ----

    @GetMapping("/{caseId}/timeline")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Get case timeline", description = "DOCTOR or ADMIN: retrieve the chronological event timeline")
    public ResponseEntity<List<CaseUpdateResponse>> getTimeline(@PathVariable Long caseId) {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(caseUpdateService.getTimeline(caseId, username));
    }
}
