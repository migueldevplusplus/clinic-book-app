package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.Patient;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
}
