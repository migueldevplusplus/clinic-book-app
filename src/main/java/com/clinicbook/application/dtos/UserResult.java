package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.UserRole;

import java.util.UUID;

public record UserResult(UUID userId, String fullName, UserRole role, boolean isActive) {
}
