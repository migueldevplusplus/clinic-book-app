package com.clinicbook.domain.exception;

import java.util.UUID;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(UUID id) {
        super("Doctor con ID "+ id + " no encontrado.");
    }
}
