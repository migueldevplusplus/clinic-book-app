package com.clinicbook.web.response;

import com.clinicbook.domain.enums.UserRole;

import java.util.UUID;

public record AuthResponse(
        String token,
        String fullName,
        UUID userId,
        UserRole role
) {}
