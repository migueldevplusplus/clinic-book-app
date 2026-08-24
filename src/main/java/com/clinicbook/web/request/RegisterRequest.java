package com.clinicbook.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @Size(min = 2, message = "Nombre completo muy corto") String fullName,
        @Email(message = "Email inválido") String email,
        @Size(min = 8, message = "Contraseña muy corta") String rawPassword,
        LocalDate birthDate,
        @Size(min=10) String phoneNumber)
{ }
