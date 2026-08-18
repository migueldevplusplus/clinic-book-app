package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.application.dtos.PatientSearchResult;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.port.PatientRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.PatientMapper;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import com.clinicbook.infrastructure.persistence.models.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class PatientRepositoryImpl implements PatientRepositoryPort {
    private final PatientMapper patientMapper;
    private final PatientJpaRepository patientJpaRepo;
    private final EntityManager entityManager;


    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = patientMapper.toEntity(patient);

        UserEntity userRef = entityManager.getReference(UserEntity.class, patient.getUser().getId());
        entity.setUser(userRef);

        entityManager.persist(entity);

        return patientMapper.toDomain(entity);
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return patientJpaRepo.findById(id).map(patientMapper::toDomain);
    }

    @Override
    public List<PatientSearchResult> search(String query) {
        return patientJpaRepo.search(query);
    }

}
