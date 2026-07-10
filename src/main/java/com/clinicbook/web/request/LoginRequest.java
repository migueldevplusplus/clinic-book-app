package com.clinicbook.web.request;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email(message = "Email no valido") String email, String password) {
}
