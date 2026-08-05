package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.UserRole;
import jakarta.validation.constraints.Size;

public record RegisterUserCommand(String fullName,
                                  String email,
                                  @Size(min=8) String rawPassword,
                                  UserRole role) {

}
