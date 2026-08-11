package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.port.PatientRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.PatientMapper;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class PatientRepositoryImpl implements PatientRepositoryPort {
    private final PatientMapper patientMapper;
    private final PatientJpaRepository patientJpaRepo;


    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = patientMapper.toEntity(patient);
        return patientMapper.toDomain(patientJpaRepo.save(entity));
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        return patientJpaRepo.findById(id).map(patientMapper::toDomain);
    }

}
