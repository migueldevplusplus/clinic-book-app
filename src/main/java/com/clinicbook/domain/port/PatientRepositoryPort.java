package com.clinicbook.domain.port;

import com.clinicbook.application.dtos.PatientSearchResult;
import com.clinicbook.domain.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepositoryPort {
    Patient save(Patient patient);
    Optional<Patient> findById(UUID id);
    List<PatientSearchResult> search(String query);
}
