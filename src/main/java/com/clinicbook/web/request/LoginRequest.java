package com.clinicbook.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email(message = "Email no valido") String email,
        @NotNull String password)
{ }
