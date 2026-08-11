package com.clinicbook.web.response;

import com.clinicbook.domain.enums.Specialty;

import java.util.UUID;

public record DoctorResponse(
        UUID doctorId,
        String fullName,
        Specialty specialty,
        int consultationDurationMinutes
) {}
