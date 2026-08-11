package com.clinicbook.web.request;

import com.clinicbook.domain.enums.Specialty;
import jakarta.validation.constraints.*;

public record DoctorRegistrationRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String rawPassword,

        @NotNull(message = "Specialty is required")
        Specialty specialty,

        @Positive(message = "Consultation duration must be positive")
        int consultationDurationMinutes
) {}
