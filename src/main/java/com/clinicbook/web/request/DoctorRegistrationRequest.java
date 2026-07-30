package com.clinicbook.web.request;

import com.clinicbook.domain.enums.Specialty;

public record DoctorRequest(
        String fullName,
        String nationalId,
        String email,
        String rawPassword,
        Specialty specialty,
        int consultationDurationMinutes
) {}
