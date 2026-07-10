package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.port.PatientRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.PatientMapper;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PatientRepositoryImpl implements PatientRepositoryPort {
    private final PatientMapper patientMapper;
    private final PatientJpaRepository patientJpaRepo;

    public PatientRepositoryImpl(PatientJpaRepository patientJpaRepo, PatientMapper patientMapper){
        this.patientJpaRepo = patientJpaRepo;
        this.patientMapper = patientMapper;
    }

    @Override
    public void save(Patient patient) {
        PatientEntity entity = patientMapper.toEntity(patient);
        patientJpaRepo.save(entity);
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Patient> findByUserId(UUID userId) {
        return Optional.empty();
    }
}
