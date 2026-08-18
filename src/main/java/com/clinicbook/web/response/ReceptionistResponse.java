package com.clinicbook.web.response;

import com.clinicbook.domain.enums.UserRole;

import java.util.UUID;

public record ReceptionistResponse(
        String fullName,
        UUID userId) {
}
