package com.clinicbook.domain.port;

import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.model.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepositoryPort {
    void save(Doctor doctor);
    boolean existsById(UUID id);
    Optional<Doctor> findById(UUID id);
    List<Doctor> findBySpecialty(Specialty specialty);
    List<Doctor> findAll();
    
}
