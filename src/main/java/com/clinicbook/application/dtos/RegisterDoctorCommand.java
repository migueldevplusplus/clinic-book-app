package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.Specialty;

public record RegisterDoctorCommand(String fullName,
                                    String email,
                                    String rawPassword,
                                    Specialty specialty,
                                    int consultationDurationMinutes) {
}
