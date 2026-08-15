package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.UserRole;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterPatientCommand(String fullName,
                                     String email,
                                     @Size(min=8) String rawPassword,
                                     UserRole role,
                                     LocalDate birthDate,
                                     @Size(min=10) String phoneNumber)
{

}
