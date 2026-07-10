package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.UserRole;

import java.util.UUID;

public record AuthResult(String token, String fullName, UUID userId, UserRole role) {
}
