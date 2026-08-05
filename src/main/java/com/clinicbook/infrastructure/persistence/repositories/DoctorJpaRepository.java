package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.model.Doctor;
import com.clinicbook.infrastructure.persistence.models.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, UUID> {
    List<DoctorEntity> findBySpecialty(Specialty specialty);
}
