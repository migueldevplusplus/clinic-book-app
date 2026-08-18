package com.clinicbook.web.response;

import java.util.UUID;

public record PatientResponse(
        String fullName,
        UUID userId)
{
}
