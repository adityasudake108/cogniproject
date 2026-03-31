package com.example.cogniproject.repository;

import com.example.cogniproject.entity.DiseaseCase;
import com.example.cogniproject.enums.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for DiseaseCase entities supporting role-based queries.
 */
@Repository
public interface DieaseCaseRepository extends JpaRepository<DiseaseCase, Long> {

    /** Returns all cases assigned to the given doctor. */
    List<DiseaseCase> findByDoctorId(Long doctorId);

    /** Returns all cases belonging to the given citizen. */
    List<DiseaseCase> findByCitizenId(Long citizenId);

    /** Returns cases for a doctor filtered by status. */
    List<DiseaseCase> findByDoctorIdAndStatus(Long doctorId, CaseStatus status);

    /** Returns cases for a citizen filtered by status. */
    List<DiseaseCase> findByCitizenIdAndStatus(Long citizenId, CaseStatus status);

    /** Returns all cases for a specific disease type (case-insensitive). */
    @Query("SELECT dc FROM DiseaseCase dc WHERE LOWER(dc.diseaseType) = LOWER(:diseaseType)")
    List<DiseaseCase> findByDiseaseTypeIgnoreCase(@Param("diseaseType") String diseaseType);
}
