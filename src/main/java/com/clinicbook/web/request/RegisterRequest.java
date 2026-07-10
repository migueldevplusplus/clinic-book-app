package com.clinicbook.web.request;

import com.clinicbook.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@Size(min = 2, message = "Nombre completo muy corto") String fullName,
                              @Email(message = "Email inválido") String email,
                              @Size(min = 7, max = 9, message = "Nombre completo muy corto") String nationalId,
                              @Size(min = 8, message = "Contraseña muy corta") String rawPassword)
{

}
