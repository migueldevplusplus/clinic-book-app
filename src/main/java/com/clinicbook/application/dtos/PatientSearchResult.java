package com.clinicbook.application.dtos;

import java.util.UUID;

public record PatientSearchResult(
        UUID patientId, String name, String email, String phoneNumber
) { }
