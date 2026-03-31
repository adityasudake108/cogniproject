package com.example.cogniproject.repository;

import com.example.cogniproject.entity.CaseUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CaseUpdate entities.
 */
@Repository
public interface CaseUpdateRepository extends JpaRepository<CaseUpdate, Long> {

    /** Returns all updates for a given case, ordered by update date ascending. */
    List<CaseUpdate> findByDiseaseCaseIdOrderByUpdateDateAsc(Long caseId);
}
