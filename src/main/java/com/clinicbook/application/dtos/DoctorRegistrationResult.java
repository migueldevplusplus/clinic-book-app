package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.Specialty;

import java.util.UUID;

public record DoctorRegistrationResult(
        UUID doctorId,
        String email,
        String fullName,
        Specialty specialty
) {}
