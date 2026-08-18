package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.application.dtos.PatientSearchResult;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {

    @Query("SELECT new com.clinicbook.application.dtos.PatientSearchResult(p.id, u.fullName, u.email, p.phoneNumber) " +
            " FROM PatientEntity p " +
            "JOIN UserEntity u " +
            "ON p.id = u.id " +
            "WHERE ( LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) ) " +
            "AND u.disabledAt IS NULL " +
            "ORDER BY u.fullName ASC ")
    List<PatientSearchResult> search(@Param("query") String query);
}
