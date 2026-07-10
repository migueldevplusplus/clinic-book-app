package com.clinicbook.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginUserCommand(@Email String email, @Size(min=8) String rawPassword) {
}
