package com.clinicbook.domain.port;

import com.clinicbook.domain.model.Receptionist;

import java.util.Optional;
import java.util.UUID;

public interface ReceptionistRepositoryPort {
    Receptionist save(Receptionist receptionist);
    Optional<Receptionist> findById(UUID id);
}
